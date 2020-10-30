package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.backend.model.ConRoom;
import com.ssafy.backend.model.Emotion;
import com.ssafy.backend.repository.ConRoomRepository;
import com.ssafy.backend.repository.EmotionRepository;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    ConRoomRepository conRoomRepository;

    public static LinkedList<String> list = new LinkedList<>();

    @PostMapping("/test")
    @ApiOperation(value = "녹음파일저장, STT ,WordCloud")
    public Object fileTest(@RequestPart("file") MultipartFile ff) throws IllegalStateException, IOException {
        list.clear();
        String Rec = ff.getOriginalFilename().toLowerCase();
        long nowtime = datetimeTosec(LocalDateTime.now());
        
        System.out.println("녹음파일명 :" + System.getProperty("user.dir") + nowtime + Rec);
        
        File file;

        String hostname = InetAddress.getLocalHost().getHostName();
        if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            file = new File(System.getProperty("user.dir") + "\\\\frontend\\public\\record\\" + nowtime + Rec);
        } else {// aws
            file = new File("/var/lib/jenkins/workspace/front/frontend/public/record/" + nowtime + Rec);
        }
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ff.transferTo(file);

        String[] command = new String[3];
        command[0] = "python";
        if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            command[1] = "./backend/AI/SpeechToText2.py";
        } else {// aws
            command[1] = "/var/lib/jenkins/workspace/front/backend/AI/SpeechToText2.py";
        }
        command[2] = file.getName();

        list.add(file.getName());

        try {
            return new ResponseEntity<>(execPython(command), HttpStatus.OK) ;
        } catch (Exception e) {
            return new ResponseEntity<>(list, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRecord")
    @ApiOperation(value = "녹화상담 대기 리스트 불러오기")
    private Object ReadRecordings(){
        String waiting = "waiting";
        List<ConRoom> conlist = conRoomRepository.findByStatus(waiting);
        System.out.println(conlist.size());
        return conlist; 
    }

    @PostMapping("/assigned/{num}/{mentor}")
    @ApiOperation(value = "녹화상담 진행")
    private Object AssignedRecord(@PathVariable long num, @PathVariable long mentor) {
        String progress = "progress";
        ConRoom conRoom = conRoomRepository.findByNum(num);
        conRoom.setStatus(progress);
        conRoom.setMentor(mentor);
        conRoomRepository.save(conRoom);
        return 0;
    }
    
    private long datetimeTosec(LocalDateTime ldt) {
        long result = 0L;
        result += ((((((((ldt.getYear() - 2000) * 365) + ldt.getDayOfYear()) * 24) + ldt.getHour()) * 60)
        + ldt.getMinute()) * 60 + ldt.getSecond());
        return result;
    }
    
    public static Object execPython(String[] command) throws IOException, InterruptedException {

        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        executor.execute(commandLine);
        System.out.println("STT OK!");

        String[] outputList = outputStream.toString().split("\n");
        String str = "";
        for (String s : outputList) {
            str += s + " ";
        }

        // 경로 확인
        String hostname = InetAddress.getLocalHost().getHostName();
        if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            command[1] = "./backend/AI/text_wordcloud.py";
        } else {// aws
            command[1] = "/var/lib/jenkins/workspace/front/backend/AI/text_wordcloud.py";
        }
        command[2] = str;
        commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }
        
        outputStream = new ByteArrayOutputStream();
        pumpStreamHandler = new PumpStreamHandler(outputStream);
        executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        System.out.println(str);
        executor.execute(commandLine);

        outputList = outputStream.toString().split("\n");
        for (String s : outputList) {
            list.add(s.trim());
        }

        list.add("none");
        list.add("none");
        list.add("none");
        System.out.println("WordCloud OK!");

        return list;
    }

    @PostMapping("/regist")
    @ApiOperation(value = "녹음 상담 등록")
    public Object emotionSave(@RequestBody ConRoom request) throws IOException, SQLException {
        try {
            ConRoom conRoom = new ConRoom();
            if(request.getMentor() == 1L) {
                conRoom.setMentor(1L);
                conRoom.setStatus("waiting");
            } else {
                conRoom.setMentor(request.getMentor());
                conRoom.setStatus("progress");
            }
            conRoom.setMentee(request.getMentee());
            conRoom.setTitle(request.getTitle());
            conRoom.setRecordDir(request.getRecordDir());
            conRoom.setWordcloudImg(request.getWordcloudImg());
            conRoom.setKeyword1(request.getKeyword1());
            conRoom.setKeyword2(request.getKeyword2());
            conRoom.setKeyword3(request.getKeyword3());
            conRoomRepository.save(conRoom);
            return new ResponseEntity<>(conRoom.getNum(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    EmotionRepository emotionRepository;

    @PostMapping("/emotion")
    @ApiOperation(value = "녹음 상담 감정 등록")
    public Object emotionUpload(@RequestBody Emotion request) throws SQLException, IOException {
        try {
            System.out.println(request.toString());
            Emotion emotion = new Emotion();
            emotion.setNum(request.getNum());
            emotion.setAngry(request.getAngry());
            emotion.setDisgusted(request.getDisgusted());
            emotion.setFearful(request.getFearful());
            emotion.setHappy(request.getHappy());
            emotion.setNeutral(request.getNeutral());
            emotion.setSad(request.getSad());
            emotion.setSurprised(request.getSurprised());
            emotionRepository.save(emotion);
            return new ResponseEntity<>("emotion succ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRecordDetail/{num}")
    @ApiOperation(value = "녹화상담 디테일")
    private Object getRecordDetail(@PathVariable Long num){
        ConRoom conroom = conRoomRepository.findByNum(num);
        return conroom;
    }

    //답변 저장하기
    @PostMapping("/sendAnswer/{num}")
    @ApiOperation(value = "답변 저장")
    private Object sendAnswer(@PathVariable Long num, @RequestBody String answer){
        ConRoom conroom = conRoomRepository.findByNum(num);
        System.out.println(answer);
        System.out.println(answer);
        System.out.println(answer);
        conroom.setAnswer(answer);
        conroom.setStatus("finish");
        conRoomRepository.save(conroom);
        return conroom;
    }
}
