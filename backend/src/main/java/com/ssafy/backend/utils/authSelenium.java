package com.ssafy.backend.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class authSelenium {

	public static void main(String[] args) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
			driver = new ChromeDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			driver.get("https://www.q-net.or.kr/qlf006.do?id=qlf00601&gSite=Q&gId=");
			WebElement name = driver.findElement(By.id("hgulNm"));
			name.clear();
			name.sendKeys("김영남");
			WebElement birth = driver.findElement(By.id("resdNo1"));
			birth.clear();
			birth.sendKeys("930617");
			WebElement lcsNo = driver.findElement(By.id("lcsNo"));
			lcsNo.clear();
			lcsNo.sendKeys("17201070932Z");
			WebElement qualExpDt = driver.findElement(By.id("qualExpDt"));
			qualExpDt.clear();
			qualExpDt.sendKeys("20180717");
			WebElement lcsMngNo = driver.findElement(By.id("lcsMngNo"));
			lcsMngNo.clear();
			lcsMngNo.sendKeys("1801063311");
			
			WebElement element = driver.findElement(By.xpath("//form[@name='infoBoxs']/div[2]/div/button[2]"));
			System.out.println(element.getText());
			element.click();
			Thread.sleep(500);
			WebElement check = driver.findElement(By.xpath("//div[@id='PInfo']/div/table/tbody/tr/td"));
			System.out.println(check.getText());
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}

	}

}
