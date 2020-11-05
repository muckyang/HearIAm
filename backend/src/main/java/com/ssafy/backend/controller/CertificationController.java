package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.ssafy.backend.utils.AuthSelenium;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cert")
public class CertificationController {

	@PostMapping("/imgCheck")
	@ApiOperation(value = "자격증 OCR")
	public Object textCheck(@RequestPart("file") MultipartFile ff) throws IllegalStateException, SQLException, IOException {
		System.out.println("Certification Check Python Call");

		String Rec = ff.getOriginalFilename().toLowerCase();
        long nowtime = datetimeTosec(LocalDateTime.now());
        
        File file;

        String hostname = InetAddress.getLocalHost().getHostName();
        if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            file = new File(System.getProperty("user.dir") + "\\\\frontend\\public\\images\\" + nowtime + Rec);
        } else {// aws
            file = new File("/var/lib/jenkins/workspace/front/frontend/public/images/" + nowtime + Rec);
		}
		
		if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

		ff.transferTo(file);

		String[] command = new String[3];
		command[0] = "python";
		
		// 경로 확인
		if (hostname.substring(0, 7).equals("DESKTOP")) {// local
			command[1] = "./backend/AI/ocr.py";
		} else {// aws
			command[1] = "/var/lib/jenkins/workspace/front/backend/AI/ocr.py";
		}

		// 파일 이름
		command[2] = file.getName();
		try {
			return execPython(command);
		} catch (Exception e) {
			return "sfail";
		}
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
		
		int result = executor.execute(commandLine);
		System.out.println("result: " + result);

		String[] outputList = outputStream.toString().split("\n");
		for (int i = 0; i < outputList.length; i++) {
			outputList[i] = outputList[i].trim();
		}

		for (String string : outputList) {
			System.out.println(string);
		}

		String text = "impo";
		// 이름, 생년월일, 자격번호, 발급일, 내지번호
		try {
			text = AuthSelenium.execSelenium(outputList);
			text = "adadadadadadadadadadad";
		} catch (Exception e) {
			text = "qweqweFail";
		}
		System.out.println(text);

		return text;
	}

	
}
