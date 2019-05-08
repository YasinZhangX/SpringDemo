package hello.restTemplateDemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Yasin Zhang
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class restTemplateTest {

    @Autowired
    private RestTemplate sslRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testSSLRestTemplate() {
        String url = "https://free-api.heweather.com/v5/forecast?city=CN101080101&key=5c043b56de9f4371b0c7f8bee8f5b75e";
        String resp = restTemplate.getForObject(url, String.class);
        System.out.println(resp);
    }

    @Test
    public void testHuaweiRestTemplate() {
        String appId = Constant.APPID;
        String secret = Constant.SECRET;
        String urlLogin = Constant.APP_AUTH;

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("appId", appId);
        param.add("secret", secret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
        ResponseEntity<String> response = sslRestTemplate.postForEntity(urlLogin, request, String.class);
        System.out.println(response.getBody());
    }

}
