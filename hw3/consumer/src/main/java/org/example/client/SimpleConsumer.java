package org.example.client;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class SimpleConsumer implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(SimpleConsumer.class);

    KafkaConsumer<String, String> consumer;

    public SimpleConsumer() {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer1");

        consumer = new KafkaConsumer<>(props);
        log.info("Consumer created");
    }

    public void subscribeTopic(String topic) {
        consumer.subscribe(Arrays.asList(topic));
        log.info("Consumer subscribe on " + topic);
    }

    public void subscribeTopic(List<String> topics) {
        consumer.subscribe(topics);
        log.info("Consumer subscribe on " + topics.toString());
    }
    

    public ConsumerRecords<String, String> poll(Duration duration) {
        return consumer.poll(duration);
    }

    /**
     * Е
     * @param func - функция над записью
     * @param duration
     * @param timeout - Если меньше 1 - бесконечно, иначе остановится после timeout ms.
     */
    public void executeFunction(Function<ConsumerRecord<String, String>, String> func,String topic, Duration duration, long timeout) {
        long start = System.currentTimeMillis();

        while (timeout < 1 || (System.currentTimeMillis() - start < timeout)) {
            ConsumerRecords<String, String> records = consumer.poll(duration);
            records.forEach(func::apply);
        }
    }

    @Override
    public void close() throws IOException {
        consumer.close();
        log.info("Consumer closed");
    }
}
