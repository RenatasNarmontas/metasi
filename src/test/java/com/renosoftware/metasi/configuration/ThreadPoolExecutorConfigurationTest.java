package com.renosoftware.metasi.configuration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolExecutorConfigurationTest {

  @Test
  public void threadPoolTaskExecutorTest() {
    ThreadPoolExecutorConfiguration threadConfig = new ThreadPoolExecutorConfiguration();
    Object object = threadConfig.threadPoolTaskExecutor();
    assertThat(object).isInstanceOf(ThreadPoolTaskExecutor.class);
  }

}
