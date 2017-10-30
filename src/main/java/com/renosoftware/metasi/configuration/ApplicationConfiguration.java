package com.renosoftware.metasi.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * ApplicationConfiguration class
 */
@Configuration
@ComponentScan(basePackages = "com.renosoftware.metasi")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

  /**
   * Configure MessageSource to lookup any validation/error message in internationalized property
   * files
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

}
