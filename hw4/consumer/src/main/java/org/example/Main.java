package org.example;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.SessionWindow;
import org.apache.kafka.streams.state.Stores;
import org.example.client.StreamConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        var builder = new StreamsBuilder();
        StreamConf streamConf = new StreamConf();
        Serde<String> serde = Serdes.String();
        KStream<String, String> stream = builder.stream("events", Consumed.with(serde,serde));

        var duration = Duration.ofMinutes(5);
        var result = stream.groupBy((k,v) -> k, Grouped.with(serde, serde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(duration)).count();

        result.suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded())).toStream().filter((k,v) -> v != null).foreach((k,v) -> log.info("window = " +  k.window().toString() + " window = " +  k.window().endTime() +  " key = " +  k.key() + " count = " +  v));


        try (KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamConf.getStreamsConfig())){
            kafkaStreams.start();

            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}