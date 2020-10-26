package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController

@RequestMapping("/api/record")
public class RecordController {
    @PostMapping("/test")
    @ApiOperation(value = "녹음 파일 저장")
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

    private long datetimeTosec(LocalDateTime ldt) {
        long result = 0L;
        result += ((((((((ldt.getYear() - 2000) * 365) + ldt.getDayOfYear()) * 24) + ldt.getHour()) * 60)
                + ldt.getMinute()) * 60);
        return result;
    }

    public static Object execPython(String[] command) throws IOException, InterruptedException {
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
            System.out.println("com : "+command[i]);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        System.out.println("IIIIIIIIIII");
        int result = executor.execute(commandLine);
        System.out.println("result: " + result);
        String[] outputList = outputStream.toString().split("\n");
        for (String s : outputList) {
            System.out.println(s);
        }
        return outputList;
    }
}
