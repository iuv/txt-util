package com.ixx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                //处理文本
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(execute(rule, template, line)).append("\n");
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

    public String execute(String rule, String template, String line){
        JSONObject jo = JSON.parseObject(rule);
        Map<String, String> all = new HashMap<>();
        List<String> keys = new ArrayList<>();
        //主规则不能为空
        if(jo.getString("root") != null){
           String[] lines = line.split(jo.getString("root"));
            int i = 0;
            //处理主规则拆分数据
            for (String s : lines) {
                all.put("$("+i+")", s);
                keys.add("$("+i+")");
                i++;
            }
            //获取子规则
            if(jo.getJSONArray("z") != null){
                JSONArray ja = jo.getJSONArray("z");
                String[] zs = new String[ja.size()];
                ja.toArray(zs);
                for (String tmp : zs) {
                    if(tmp.indexOf(")") > 0) {
                        String lineKey = tmp.substring(0, tmp.indexOf(")"));
                        String zrule = tmp.substring(tmp.indexOf(")") + 1);
                        String zline = all.get(lineKey + ")");
                        if(zline == null) {
                            continue;
                        }
                        String[] zlines = zline.split(zrule);
                        //处理子规则拆分数据
                        int zi = 0;
                        for (String s : zlines) {
                            all.put(lineKey+"."+zi+")", s);
                            keys.add(lineKey+"."+zi+")");
                            zi++;
                        }
                    }
                }
                //替换输出模板
                for (int i1 = keys.size() - 1; i1 >= 0; i1--) {
                    String key = keys.get(i1);
                    String val = all.get(key);
                    key = key.replace("$","\\$").replace("(","\\(").replace(")","\\)");
                    template = template.replaceAll(key, val);
                }
            }
        }
        return template;
    }

}
