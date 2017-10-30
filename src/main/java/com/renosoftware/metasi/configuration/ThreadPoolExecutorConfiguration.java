package com.renosoftware.metasi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadPoolExecutorConfiguration class
 */
@Configuration
public class ThreadPoolExecutorConfiguration {

  @Value("${threadpool.corepoolsize:5}")
  int corePoolSize;

  @Value("${threadpool.maxpoolsize:10}")
  int maxPoolSize;

  /**
   * Thread pool task executor bean
   *
   * @return thread pool task executor
   */
  @Bean(destroyMethod = "shutdown")
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    return executor;
  }
}
