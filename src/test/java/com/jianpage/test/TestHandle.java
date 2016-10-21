package com.jianpage.test;

import com.jianpage.util.TxtUtil;
import org.junit.Test;

/**
 * Created by ixx on 2016/10/21.
 */
public class TestHandle {
    @Test
    public void testHandle(){
        TxtUtil.regRule("a","{\"z\":[\"$(1) \"],\"root\":\" \"}");
        System.out.println(TxtUtil.handle("a","123$(1)4$(1.0)56789","a b c  d e f  1 2 3"));
    }
}
