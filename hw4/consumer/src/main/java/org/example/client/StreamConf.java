package org.example.client;

import org.apache.kafka.streams.StreamsConfig;

import java.util.HashMap;
import java.util.Map;

public class StreamConf {
    private final String HOST = "localhost:19092";
    private final String ID = "stream-client1";
    private final Map<Object, Object> streamsConfig;

    public StreamConf() {
        streamsConfig = new HashMap<>();
        streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, ID);
    }

    public StreamsConfig getStreamsConfig() {
        return new StreamsConfig(streamsConfig);
    }
}
