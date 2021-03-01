package com.xzy.jedis.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author xiazhengyue
 * @since 2021-03-01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JedisConstant {

    public static final int DEFAULT_PORT = 6379;

    public static final int DEFAULT_TIMEOUT = 2000;

    public static final String PONG = "PONG";
}
