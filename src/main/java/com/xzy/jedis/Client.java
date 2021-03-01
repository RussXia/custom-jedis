package com.xzy.jedis;

/**
 * @author xiazhengyue
 * @since 2021-02-26
 */
public class Client extends Connection {

    public Client(String host) {
        super(host);
    }

    public Client(String host, int port) {
        super(host, port);
    }

    public void ping() {
        sendCommand("PING");
    }

    public void set(String key, String value) {
        sendCommand("SET", key, value);
    }

    public void setnx(String key, String value) {
        sendCommand("SETNX", key, value);
    }

    public void setex(String key, int seconds, String value) {
        sendCommand("SETEX", key, String.valueOf(seconds), value);
    }

    public void get(String key) {
        sendCommand("GET", key);
    }
}
