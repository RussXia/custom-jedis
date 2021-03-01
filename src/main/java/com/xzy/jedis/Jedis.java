package com.xzy.jedis;

import java.io.IOException;

/**
 * @author xiazhengyue
 * @since 2021-02-26
 */
public class Jedis {

    private Client client = null;

    public Jedis(String host) {
        client = new Client(host);
    }

    public Jedis(String host, int port) {
        client = new Client(host, port);
    }

    public Jedis(String host, int port, int timeout) {
        client = new Client(host, port);
        client.setTimeout(timeout);
    }

    public void connect() throws IOException {
        client.connect();
    }

    public void disconnect() throws IOException {
        client.disconnect();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public String ping() {
        client.ping();
        return client.getStatusCodeReply();
    }

    public String set(String key, String value) {
        client.set(key, value);
        return client.getStatusCodeReply();
    }

    public String get(String key) {
        client.sendCommand("GET", key);
        return client.getBulkReply();
    }
}
