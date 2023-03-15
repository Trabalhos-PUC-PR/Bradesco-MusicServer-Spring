package br.pucpr.musicserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class GlobalConfig {

    @Bean
    @ConfigurationProperties("thread.pool")
    public ThreadPoolTaskExecutor threadPool(){
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public Executor taskExecutor(ThreadPoolTaskExecutor executor){
        executor.initialize();
        return executor;
    }

}
