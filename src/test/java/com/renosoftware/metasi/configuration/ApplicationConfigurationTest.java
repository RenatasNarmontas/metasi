package com.renosoftware.metasi.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.context.MessageSource;

public class ApplicationConfigurationTest {

  @Test
  public void messageSourceTest() {
    ApplicationConfiguration appConfig = new ApplicationConfiguration();
    Object object = appConfig.messageSource();
    assertThat(object).isInstanceOf(MessageSource.class);
  }

}
