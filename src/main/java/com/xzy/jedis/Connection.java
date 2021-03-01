package com.xzy.jedis;

import com.xzy.jedis.util.JedisConstant;
import com.xzy.jedis.util.JedisException;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author xiazhengyue
 * @since 2021-02-26
 */
@Data
public class Connection {

    private String host;
    private int port = JedisConstant.DEFAULT_PORT;
    private Socket socket;
    private Protocol protocol = new Protocol();
    private OutputStream outputStream;
    private InputStream inputStream;
    private int timeout = JedisConstant.DEFAULT_TIMEOUT;

    public Connection(String host) {
        super();
        this.host = host;
    }

    public Connection(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    protected Connection sendCommand(String name, String... args)
            throws JedisException {
        if (!isConnected()) {
            throw new JedisException("Please connect Jedis before using it.");
        }
        protocol.sendCommand(outputStream, name, args);
        return this;
    }

    public void connect() throws UnknownHostException, IOException {
        if (!isConnected()) {
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                throw new JedisException(ex);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }

    protected String getStatusCodeReply() throws JedisException {
        return protocol.getStatusCodeReply(inputStream);
    }

    public String getBulkReply() throws JedisException {
        return protocol.getBulkReply(inputStream);
    }
}
