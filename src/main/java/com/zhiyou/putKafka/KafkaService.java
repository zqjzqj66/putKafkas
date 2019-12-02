package com.zhiyou.putKafka;



import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaService implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private static KafkaProducer producer = null;
    private static KafkaService instance = new KafkaService();

    private KafkaService() {
        try {
            BoneProperty appProperty = new BoneProperty();
            Properties prop = new Properties();
            prop.setProperty("metadata.broker.list", StringUtils.join(appProperty.getList("kafka.bootstrap.servers"), ","));
            prop.setProperty("bootstrap.servers", StringUtils.join(appProperty.getList("kafka.bootstrap.servers"), ","));
            prop.setProperty("acks", "1");
            prop.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            prop.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            String maxRequstSize = appProperty.getString("kafka.max.request.size");
            if (StringUtils.isNotBlank(maxRequstSize)) {
                prop.setProperty("max.request.size", maxRequstSize);
            }

            producer = new KafkaProducer(prop);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public KafkaService clone() {
        return getInstance();
    }

    public static KafkaService getInstance() {
        return instance;
    }

    public void send(final String topic, final String message)  {
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, message);
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        KafkaService.logger.error("Kafka send failed: Failed to send message {} to topic {}", message, topic);
                    }

                }
            }).get();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}
