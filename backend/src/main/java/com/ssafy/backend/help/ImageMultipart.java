package com.ssafy.backend.help;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ImageMultipart {
    @PostMapping("/makeImageSrc")
    @ApiOperation(value = "이미지 저장")
    public String fileTest(@RequestPart("file") MultipartFile img) throws IllegalStateException, IOException {
        String originFilename = img.getOriginalFilename();
        String fileName = originFilename.substring(0, originFilename.lastIndexOf("."));

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += ".png";
        System.out.println(fileName);
        String hostname = InetAddress.getLocalHost().getHostName();
        // System.out.println(hostname);
        File file = null;
        if (hostname.substring(0, 7).equals("DESKTOP")) {// local
            file = new File("C:\\AI108\\s03p23b108\\frontend\\public\\contents\\" + fileName);
        } else {// aws
            file = new File("/var/lib/jenkins/workspace/Gitlab/frontend/public/contents/" + fileName);
        }
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        img.transferTo(file);
        return file.getName();
    }

}
