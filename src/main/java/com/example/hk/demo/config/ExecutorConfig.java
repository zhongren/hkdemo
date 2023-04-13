package com.example.hk.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2023年04月13日15:47:35
 * @EnableAsync 表示开启对异步任务的支持，可以放在springboot的启动类上，也可以放在自定义线程池的配置类上
 **/
@Slf4j
@Configuration
public class ExecutorConfig {

    // 获取服务器的cpu个数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();// 获取cpu个数
    private static final int COUR_SIZE = CPU_COUNT * 2;
    private static final int MAX_COUR_SIZE = CPU_COUNT * 4;


    @Bean(name = "cameraExecutor")
    public ThreadPoolTaskExecutor cameraExecutor() {
        log.info("start cameraExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(COUR_SIZE);
        //配置最大线程数
        executor.setMaxPoolSize(MAX_COUR_SIZE);
        //配置队列大小 Default is Integer.MAX_VALUE
        executor.setQueueCapacity(6000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("cameraExecutor-");
        /**
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //当调度器shutdown被调用时等待当前被调度的任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        //初始化执行器
        executor.initialize();
        return executor;
    }


}