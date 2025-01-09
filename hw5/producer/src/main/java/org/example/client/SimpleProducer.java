package org.example.client;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleProducer {
    private KafkaProducer<String, String> producer;
    private static final Logger log = LoggerFactory.getLogger(SimpleProducer.class);

    public SimpleProducer() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer(props);
    }

    public RecordMetadata send(String key, String value, String topic) {

        ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, key, value);

        if (this.producer == null) {
            log.warn("Producer not found");

            return null;
        }

        try {
            return producer.send(record).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initTransactions() {
        if (this.producer == null) {
            log.warn("Producer not found");
            return;
        }

        producer.initTransactions();
    }

    public void beginTransactions() {
        if (this.producer == null) {
            log.warn("Producer not found");
            return;
        }

        producer.beginTransaction();
    }

    public void abortTransactions() {
        if (this.producer == null) {
            log.warn("Producer not found");
            return;
        }

        producer.abortTransaction();
    }

    public void commitTransactions() {
        if (this.producer == null) {
            log.warn("Producer not found");
            return;
        }

        producer.commitTransaction();
    }
}
