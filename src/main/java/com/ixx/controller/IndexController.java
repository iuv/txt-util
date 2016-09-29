package com.ixx.controller;

import com.ixx.util.TxtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 主控制器
 * Created by ixx on 16/9/12.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public  @ResponseBody  String upload(@RequestParam("file") MultipartFile file, @RequestParam("rule") String rule,
                                       @RequestParam("template") String template, HttpServletResponse response) {
        if (!file.isEmpty()&& !StringUtils.isEmpty(rule) && !StringUtils.isEmpty(template)) {
            try {
                System.out.println(rule);
                System.out.println(template);
                //注册规则
                TxtUtil.regRule("m", rule);
                //处理文本
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(TxtUtil.handle("m", template, line)).append("\n");
                }
                System.out.println(sb.toString());
                //将处理过的内容返回下载
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=result.txt");
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(sb.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                br.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        } else {
            return "上传文件不能为空!";
        }
    }

}
