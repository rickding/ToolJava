package com.common;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello common!");
        System.out.println(HttpUtil.sendHttpGet("http://www.baidu.com", null));
    }
}
