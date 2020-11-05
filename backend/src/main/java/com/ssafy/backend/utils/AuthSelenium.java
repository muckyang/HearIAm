package com.ssafy.backend.utils;

import java.net.InetAddress;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class AuthSelenium {

   public static String execSelenium(String[] list) {
      System.out.println("execselenium");
      WebDriver driver = null;
      String res ="";
      try {
         String hostname = InetAddress.getLocalHost().getHostName();
         if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            System.setProperty("webdriver.chrome.driver", "C:/portfolio202/s03p31b202/backend/src/main/java/com/ssafy/backend/utils/chromedriverw.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            driver = new ChromeDriver(options); // Driver 생성
         } else {// aws
            res="setProperty";
            System.setProperty("webdriver.chrome.driver", "/var/lib/jenkins/workspace/front/backend/src/main/java/com/ssafy/backend/utils/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("no-sandbox");
            options.addArguments("disable-dev-shm-usage");
            res = "chrome Driver ";
            driver = new ChromeDriver(options); // Driver 생성
         }
         
         res = "get ";
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
         lcsMngNo.sendKeys(list[4].replaceAll("-",""));
         
         WebElement element = driver.findElement(By.xpath("//form[@name='infoBoxs']/div[2]/div/button[2]"));
         // System.out.println(element.getText());
         res = "click";
         element.click();
         Thread.sleep(500);
         WebElement check = driver.findElement(By.xpath("//div[@id='PInfo']/div/table/tbody/tr/td"));
         // System.out.println(check.getText());
         res = check.getText();
      } catch (Throwable e) {
      System.err.println("fail");
      driver.close();
      return res;
   } 
      driver.close();
      return res;
   }

}