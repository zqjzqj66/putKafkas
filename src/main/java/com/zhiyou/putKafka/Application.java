package com.zhiyou.putKafka;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.lang.Thread;

public class Application {

    private static final BoneProperty appProperty = new BoneProperty();
    //获取topic
    private static final String topic = appProperty.getString("spark.kafka.topics");
    //获取读取的文件名
    private static final String fileName = appProperty.getString("file.name");

    public static void main(String[] args) {

        KafkaService ks = KafkaService.getInstance();
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                //每发送200条停顿20秒，防止数据量过大，造成kafka堵塞。
                if (i % 200 == 0) {
                    Thread.sleep(20000);
                }
                ks.send(topic, line);
                System.out.println(line);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test1() {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
