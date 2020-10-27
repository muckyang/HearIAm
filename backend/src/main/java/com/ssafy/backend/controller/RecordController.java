package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

import com.ssafy.backend.model.Record;
import com.ssafy.backend.model.RecordAssign;
import com.ssafy.backend.repository.RecordAssignRepository;
import com.ssafy.backend.repository.RecordRepository;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/api/record")
public class RecordController {

    @Autowired
    RecordRepository recordRepository;
    
    @Autowired
    RecordAssignRepository recordAssignRepository;
    
    @PostMapping("/test")
    @ApiOperation(value = "녹음파일저장, STT ,WordCloud")
    public Object fileTest(@RequestPart("file") MultipartFile ff) throws IllegalStateException, IOException {

        String Rec = ff.getOriginalFilename().toLowerCase();
        long nowtime = datetimeTosec(LocalDateTime.now());
        // System.out.println(System.getProperty("user.dir") +
        // "\\\\frontend\\src\\assets\\record\\"+ nowtime + Rec);
        System.out.println("녹음파일명 :" + System.getProperty("user.dir") + nowtime + Rec);

        // File file = new File("C:\\Users\\multicampus\\Desktop\\image\\"+ nowtime +
        // Rec);
        File file = new File(System.getProperty("user.dir") + "\\\\frontend\\src\\assets\\record\\" + nowtime + Rec);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ff.transferTo(file);
        
        String[] command = new String[3];
        command[0] = "python";
        command[1] = "./backend/AI/SpeechToText2.py";
        command[2] = file.getName();	

		try {
			return execPython(command);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // return file.getName();
    }
    @PostMapping("/create")
    @ApiOperation(value = "녹화상담등록")
    private Object CreateRecord(@RequestBody Record request){
        Record record = new Record();
        record.setTitle(request.getTitle());
        record.setFilename(request.getFilename());
        record.setMentee(request.getMentee());
    
        recordRepository.save(record);
        return  0;
    }

    @PostMapping("/assigned")
    @ApiOperation(value = "녹화상담 담당하기")
    private Object AssignedRecord(@RequestBody RecordAssign request){

        RecordAssign recordassign = new RecordAssign();
        recordassign.setRecordId(request.getRecordId());
        recordassign.setMento(request.getMento());
    
        recordAssignRepository.save(recordassign);
        return  0;
    }


    private long datetimeTosec(LocalDateTime ldt) {
        long result = 0L;
        result += ((((((((ldt.getYear() - 2000) * 365) + ldt.getDayOfYear()) * 24) + ldt.getHour()) * 60)
                + ldt.getMinute()) * 60+ ldt.getSecond());
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
        String str =  "";
        for (String s : outputList) {
           str += s+" ";
        }

		// 경로 확인
		String hostname = InetAddress.getLocalHost().getHostName();
		if (hostname.substring(0, 7).equals("DESKTOP")) {// local
			command[1] = "./backend/AI/text_wordcloud.py";
			System.out.println("in");
		} else {// aws
			command[1] = "../AI/textCheck.py";
		}
        command[2] = str;
		try {
            System.out.println(str);
			return execPython(command);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

    }
}
