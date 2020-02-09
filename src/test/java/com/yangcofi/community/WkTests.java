package com.yangcofi.community;

import java.io.IOException;

/**
 * @ClassName WkTests
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/9 15:37
 **/
public class WkTests {
    public static void main(String[] args) {
        String cmd = "c:/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com c:/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
