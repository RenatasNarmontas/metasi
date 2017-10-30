package com.renosoftware.metasi;

import static org.assertj.core.api.Assertions.assertThat;

import com.renosoftware.metasi.controller.ApplicationController;
import com.renosoftware.metasi.controller.RestResultController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

  @Autowired
	private ApplicationController applicationController;

  @Autowired
  private RestResultController restResultController;

	@Test
	public void contextLoads() throws Exception {
	  assertThat(applicationController).isNotNull();
	  assertThat(restResultController).isNotNull();
	}

}
