package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

// import com.ssafy.backend.utils.AuthSelenium;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/certCK")
public class CertificationController {

	// @Autowired
	// AuthSelenium authSelenium;

	@GetMapping("/imgCheck/{img}")
	@ApiOperation(value = "자격증 OCR")
	public Object textCheck(@PathVariable String img) throws SQLException, IOException {
		System.out.println("Certification Check Python Call");
		String[] command = new String[3];
		command[0] = "python";
		// 경로 확인
		String hostname = InetAddress.getLocalHost().getHostName();

		if (hostname.substring(0, 7).equals("DESKTOP")) {// local
			command[1] = "./backend/AI/ocr.py";
		} else {// aws
			command[1] = "../AI/ocr.py";
		}

		// 파일 이름
		command[2] = img;

		return 1;
		// try {
		// 	return execPython(command);
		// } catch (Exception e) {
		// 	return "fail";
		// }
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

		// 이름, 생년월일, 자격번호, 발급일, 내지번호
		// String text = authSelenium.execSelenium(outputList);
		// System.out.println(text);

		return outputList;
	}

	
}
