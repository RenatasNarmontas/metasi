package com.renosoftware.metasi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Test
  public void shouldUploadFile() throws Exception {
    ClassPathResource resource = new ClassPathResource("testfile.txt", getClass());
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
    map.add("file", resource);
    ResponseEntity<String> response = this.restTemplate.postForEntity("/", map, String.class);
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getLocation().toString()).startsWith("http://localhost:" + this.port + "/");
  }
}
