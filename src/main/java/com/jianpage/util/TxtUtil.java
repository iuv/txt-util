package com.jianpage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文本处理主类
 * Created by ixx on 16/9/29.
 */
public class TxtUtil {

    private static final Logger log = LoggerFactory.getLogger(TxtUtil.class);

    private static final Properties rules = new Properties();

    /**
     * 获取配置文件配置(如果有的话)
     */
    static {
        try {
            ClassLoader CL = TxtUtil.class.getClass().getClassLoader();
            String confPath = "txtutil.properties";
            InputStream inn;
            if (CL != null) {
                inn = CL.getResourceAsStream(confPath);
            } else {
                inn = ClassLoader.getSystemResourceAsStream(confPath);
            }
            if (inn != null) {
                rules.load(inn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册规则
     *
     * @param key  规则key
     * @param rule 规则内容(json格式,可使用首页生成)
     */
    public static void regRule(String key, String rule) {
        rules.put(key, rule);
    }

    /**
     *
     * @param ruleKey 规则key
     * @param template 要输出的模板内容
     * @param lineList 要处理的数据集合
     * @return 按模板将处理后的数据输出List
     */
    public static List<String> handle(String ruleKey, String template, List<String> lineList){
        String rule = rules.getProperty(ruleKey);
        if(StringUtil.allIsNotEmptys(ruleKey, template, rule) && lineList != null && lineList.size()>0){
            List<String> retList = new ArrayList<String>(lineList.size());
            for (String tmp : lineList) {
                retList.add(handleBase(rule, template, tmp));
            }
            return retList;
        } else {
            log.error(" param is null ruleKey:"+ruleKey+",rule:"+rule+", template:"+template+", lineList.size:"+ lineList.size());
            return null;
        }
    }

    /**
     * 调用规则方法
     * @param ruleKey 规则key
     * @param template 要输出的模板内容
     * @param line 要处理的数据一行
     * @return 按模板将处理后的数据输出
     */
    public static String handle(String ruleKey, String template, String line) {
        String rule = rules.getProperty(ruleKey);
        if(StringUtil.allIsNotEmptys(ruleKey, template, line, rule)){
            return handleBase(rule, template, line);
        } else {
            log.error(" param is null ruleKey:"+ruleKey+",rule:"+rule+", template:"+template+", line"+ line);
            return null;
        }
    }

    /**
     * 处理基础方法
     * @param rule
     * @param template
     * @param line
     * @return
     */
    private static String handleBase(String rule, String template, String line) {
        JSONObject jo = JSON.parseObject(rule);
        Map<String, String> all = new HashMap<>();
        List<String> keys = new ArrayList<>();
        //主规则不能为空
        if (jo.getString("root") != null) {
            String[] lines = line.split(jo.getString("root"));
            int i = 0;
            //处理主规则拆分数据
            for (String s : lines) {
                all.put("$(" + i + ")", s);
                keys.add("$(" + i + ")");
                i++;
            }
            //获取子规则
            if (jo.getJSONArray("z") != null) {
                JSONArray ja = jo.getJSONArray("z");
                String[] zs = new String[ja.size()];
                ja.toArray(zs);
                for (String tmp : zs) {
                    if (tmp.indexOf(")") > 0 && tmp.indexOf(")") < tmp.length()-1) {
                        String lineKey = tmp.substring(0, tmp.indexOf(")"));
                        String zrule = tmp.substring(tmp.indexOf(")") + 1);
                        String zline = all.get(lineKey + ")");
                        if (zline == null) {
                            continue;
                        }
                        String[] zlines = zline.split(zrule);
                        //处理子规则拆分数据
                        int zi = 0;
                        for (String s : zlines) {
                            all.put(lineKey + "." + zi + ")", s);
                            keys.add(lineKey + "." + zi + ")");
                            zi++;
                        }
                    }
                }
                //替换输出模板
                for (int i1 = keys.size() - 1; i1 >= 0; i1--) {
                    String key = keys.get(i1);
                    String val = all.get(key);
                    key = key.replace("$", "\\$").replace("(", "\\(").replace(")", "\\)");
                    template = template.replaceAll(key, val);
                }
            }
        }
        return template;
    }
}
