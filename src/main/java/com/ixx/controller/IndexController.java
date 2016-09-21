package com.ixx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 主控制器
 * Created by ixx on 16/9/12.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                //处理文本
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                }
                return file.getName();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "上传文件不能为空!";
        }
    }

}
