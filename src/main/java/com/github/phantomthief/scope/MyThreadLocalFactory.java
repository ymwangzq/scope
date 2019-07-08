package com.github.phantomthief.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author w.vela
 * Created on 2019-07-03.
 */
class MyThreadLocalFactory {

    private static final Logger logger = LoggerFactory.getLogger(MyThreadLocalFactory.class);

    static <T> MyThreadLocal<T> create() {
        if (Boolean.getBoolean("USE_FAST_THREAD_LOCAL")) {
            try {
                return new NettyFastThreadLocal<>();
            } catch (Error e) {
                logger.warn("cannot use fast thread local as scope implements.");
            }
        }
        // TODO auto adaptive thread local between jdk thread local and netty fast thread local?
        return new JdkThreadLocal<>();
    }
}