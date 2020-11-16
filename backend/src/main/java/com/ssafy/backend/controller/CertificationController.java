package com.ssafy.backend.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	public Object textCheck(@RequestPart("file") MultipartFile ff)
			throws IllegalStateException, SQLException, IOException {
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
			return "fail";
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

		String[] outputList = outputStream.toString().split("\n");
		for (int i = 0; i < outputList.length; i++) {
			outputList[i] = outputList[i].trim();
		}

		for (String string : outputList) {
		}

		String text = "impo";

		// 이름, 생년월일, 자격번호, 발급일, 내지번호
		try {
			text = execSelenium(outputList);
		} catch (Exception e) {
			return e;
		}
		System.out.println(text);

		return text;
	}

	public static String execSelenium(String[] list) {
		System.out.println("execselenium");
		WebDriver driver = null;
		String res = "";
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			if (hostname.substring(0, 7).equals("DESKTOP")) {// local
				System.setProperty("webdriver.chrome.driver",
						"C:/portfolio202/s03p31b202/backend/src/main/java/com/ssafy/backend/utils/chromedriverw.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("headless");
				driver = new ChromeDriver(options); // Driver 생성
			} else {// aws
				System.setProperty("webdriver.chrome.driver", "/var/lib/chromedriver");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("headless");
				options.addArguments("no-sandbox");
				options.addArguments("disable-dev-shm-usage");
				driver = new ChromeDriver(options); // Driver 생성
			}

			driver.get("https://www.q-net.or.kr/qlf006.do?id=qlf00601&gSite=Q&gId=");
			WebElement name = driver.findElement(By.id("hgulNm"));
			name.clear();
			name.sendKeys(list[0]);
			WebElement birth = driver.findElement(By.id("resdNo1"));
			birth.clear();
			birth.sendKeys(list[1]);
			WebElement lcsNo = driver.findElement(By.id("lcsNo"));
			lcsNo.clear();
			lcsNo.sendKeys(list[2]);
			WebElement qualExpDt = driver.findElement(By.id("qualExpDt"));
			qualExpDt.clear();
			qualExpDt.sendKeys(list[3]);
			WebElement lcsMngNo = driver.findElement(By.id("lcsMngNo"));
			lcsMngNo.clear();
			lcsMngNo.sendKeys(list[4].replaceAll("-", ""));

			WebElement element = driver.findElement(By.xpath("//form[@name='infoBoxs']/div[2]/div/button[2]"));
			// System.out.println(element.getText());
			element.click();
			Thread.sleep(500);
			WebElement check = driver.findElement(By.xpath("//div[@id='PInfo']/div/table/tbody/tr/td"));
			res = check.getText();
		} catch (Throwable e) {
			driver.close();
			return "fail";
		}
		driver.close();
		System.out.println(res);
		return res;
	}

}
