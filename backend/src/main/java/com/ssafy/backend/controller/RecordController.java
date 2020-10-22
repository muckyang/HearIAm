package com.ssafy.backend.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/record")
public class RecordController {
    @PostMapping("/record")
    public String fileTest(@RequestPart("file") MultipartFile ff) throws IllegalStateException, IOException {

        System.out.println(ff.toString());
        String forSaveImg = ff.getOriginalFilename().toLowerCase();
        System.out.println(forSaveImg.toString());
        long nowtime = datetimeTosec(LocalDateTime.now());
        // System.out.println(System.getProperty("user.dir") + "\\frontend\\src\\assets\\record\\");
        File file = new File("C:\\Users\\multicampus\\Desktop\\image\\"+ nowtime + forSaveImg);
        // File file = new File("/home/ubuntu/frontend/src/assets/images/" + nowtime + forSaveImg);
        // File file = new File("/var/lib/jenkins/workspace/s03-final/S03P31B202/frontend/src/assets/images/" + nowtime + forSaveImg);
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ff.transferTo(file);
        System.out.println(file.getName());

        return file.getName();

    }
    
    private long datetimeTosec(LocalDateTime ldt) {
        long result = 0L;
        result += ((((((((ldt.getYear() - 2000) * 365) + ldt.getDayOfYear()) * 24) + ldt.getHour()) * 60)
                + ldt.getMinute()) * 60);
        return result;
    }

}
