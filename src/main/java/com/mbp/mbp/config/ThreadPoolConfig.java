package com.mbp.mbp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration

public class ThreadPoolConfig {
    @Value("${booking.thread.pool-size}")
    private int poolSize;


    @Bean
    public ExecutorService bookingExecutor() {
        return Executors.newFixedThreadPool(poolSize);
    }
}
