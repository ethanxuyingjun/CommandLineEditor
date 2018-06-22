package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EDLineEditorTest {
    public static void main(String[] args) {
        EDLineEditorTest t = new EDLineEditorTest();
        //t.test1();
        //t.test2();
        t.test3();

    }

    public void test1 () {
        String s = "abc user abc user abc user";
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile("abc").matcher(s);
        int mIdx = 0;
        while(slashMatcher.find()) {
            System.out.println(slashMatcher.group());
            mIdx++;
            System.out.println(slashMatcher.start());
        }
    }

    public void test2 () {
        String cmdLine = "'a,'b+1j";
        String replStr = cmdLine.replaceAll("'[a-zA-Z]","**");
        System.out.println(replStr);
    }

    public void test3 () {
        String cmdLine = "'aw test";
        String replStr = cmdLine.substring(1,2);
        System.out.println(replStr);
    }
}
