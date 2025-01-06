package org.example;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.client.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)  {
        SimpleProducer producer = new SimpleProducer();

        for (int i = 1; i < 10000; i++) {

            RecordMetadata rd1 = producer.send(String.valueOf(i), String.valueOf(i), "test");

            log.info("Сообщение" + i + " отправлено в топик: " + rd1.topic());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}