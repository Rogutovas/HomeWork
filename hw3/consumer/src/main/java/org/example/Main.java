package org.example;

import org.example.client.SimpleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        try (SimpleConsumer simpleConsumer = new SimpleConsumer()){
            simpleConsumer.subscribeTopic(List.of("topic1", "topic2"));
            simpleConsumer.executeFunction(r -> {
                System.out.println("Обработан offset " + r.offset() + " key = " + r.key() + " value = " + r.value());
                return "";
            }, "test", Duration.ofMillis(1000), 60000);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}