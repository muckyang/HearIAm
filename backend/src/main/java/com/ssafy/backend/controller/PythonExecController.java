package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
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
@RequestMapping("/api/python")
public class PythonExecController {
    
    @GetMapping("/pythonCheck/{text}")
	@ApiOperation(value = "파이썬 파일 실행 확인")
	public Object textCheck(@PathVariable String text) throws SQLException, IOException {
		// LinkedList<String> text_res;
		System.out.println("Text Check Python Call");
		String[] command = new String[3];
		command[0] = "python";
		// 경로 확인
		String hostname = InetAddress.getLocalHost().getHostName();
		if (hostname.substring(0, 7).equals("DESKTOP")) {// local
			command[1] = "./backend/AI/text_wordcloud.py";
			System.out.println("in");
		} else {// aws
			command[1] = "../AI/textCheck.py";
		}
		command[2] = text;
		try {
			return execPython(command);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// return new ResponseEntity<>(null, HttpStatus.OK);
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
		for(String s : outputList){
			System.out.println(s);
		}
		return outputList;
	}
}
