package com.xzy.jedis.util;

public class JedisException extends RuntimeException {

    private static final long serialVersionUID = -9202595137430257887L;

    public JedisException(String message) {
        super(message);
    }

    public JedisException(Throwable e) {
        super(e);
    }

    public JedisException(String message, Throwable cause) {
        super(message, cause);
    }
}
