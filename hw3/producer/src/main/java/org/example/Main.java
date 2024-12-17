package org.example;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.client.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)  {
        SimpleProducer producer = new SimpleProducer();

        producer.initTransactions();
        producer.beginTransactions();

        for (int i = 1; i < 6; i++) {
            System.out.println(" Сообщение № " + i);
            RecordMetadata rd1 = producer.send(String.valueOf(i), "Сообщение " + i + " отправлено в topic1 ", "topic1");
            RecordMetadata rd2 = producer.send(String.valueOf(i), "Сообщение " + i + " отправлено в topic2 ", "topic2");
            log.info("Сообщение отправлено в топики: " + rd1.topic() + "," + rd2.topic());
        }
        producer.commitTransactions();



        producer.beginTransactions();

        for (int i = 6; i < 8; i++) {
            System.out.println(" Сообщение № " + i);
            RecordMetadata rd1 = producer.send(String.valueOf(i), "Сообщение " + i + " будет отменено в topic1 " + i, "topic1");
            RecordMetadata rd2 = producer.send(String.valueOf(i), "Сообщение " + i + " будет отменено в topic2 " + i, "topic2");
            log.info("Сообщение отменено в топики: " + rd1.topic() + "," + rd2.topic());
            log.info(" -----------------  ");
        }

        producer.abortTransactions();
    }
}