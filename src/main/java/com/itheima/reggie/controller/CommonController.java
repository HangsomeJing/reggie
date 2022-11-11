package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    /**
     * 用处理 下载上传功能
     */

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info("file:{}",file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));

        String filename= UUID.randomUUID().toString()+suffix;
        log.info("filename:{}",filename);

        File file1 = new File(basePath + filename);
        if (!file1.exists()){
            file1.mkdirs();
        }

        file.transferTo(file1);

        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)  {
        //IO流有些模糊了。。。。
        try {

            //当 图片找不到时直接退出
            File file=new File(basePath+name);
            if (!file.exists()) return;

            FileInputStream fileInputStream = new FileInputStream(file);

            ServletOutputStream outputStream = response.getOutputStream();

            byte[] bytes=new byte[1024];
            int len=0;

            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
