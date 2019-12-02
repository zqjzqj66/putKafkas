package com.zhiyou.putKafka;

import org.apache.tomcat.jni.Thread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Application {

    private static final String topic = "app_get";

    private static final String file="D:\\tmp\\abc.txt";

    public static void main(String[] args) {

        KafkaService ks = KafkaService.getInstance();
        int i=0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(i%200==0)
                    java.lang.Thread.sleep(10000);
                ks.send(topic, line);
                System.out.println(line);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
