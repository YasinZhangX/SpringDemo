package hello.restTemplateDemo;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Yasin Zhang
 */
@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(csf)
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    @Bean(name = "sslRestTemplate")
    public RestTemplate sslRestTemplate() {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHuaweiIotHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        //设置建立连接超时时长
        requestFactory.setConnectTimeout(1000);
        //设置传递数据超时时长
        requestFactory.setReadTimeout(1000);
        return new RestTemplate(requestFactory);
    }

    private CloseableHttpClient getHuaweiIotHttpClient() throws Exception {
        // Set the domain name to not verify
        // (Non-commercial IoT platform, no use domain name access generally.)
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
            getHuaweiIoTSSLContext(), new DefaultHostnameVerifier());
        return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    }

    /**
     * Two-Way Authentication In the two-way authentication, the client needs: 1
     * Import your own certificate for server verification; 2 Import the CA
     * certificate of the server, and use the CA certificate to verify the
     * certificate sent by the server; 3 Set the domain name to not verify
     * (Non-commercial IoT platform, no use domain name access.)
     * */
    private SSLContext getHuaweiIoTSSLContext() throws Exception {
        // 1 Import your own certificate
        String demo_base_Path = System.getProperty("user.dir");
        String selfcertpath = demo_base_Path + Constant.SELFCERTPATH;
        String trustcapath = demo_base_Path + Constant.TRUSTCAPATH;

        KeyStore selfCert = KeyStore.getInstance("pkcs12");
        selfCert.load(new FileInputStream(selfcertpath),
            Constant.SELFCERTPWD.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
        kmf.init(selfCert, Constant.SELFCERTPWD.toCharArray());

        // 2 Import the CA certificate of the server,
        KeyStore caCert = KeyStore.getInstance("jks");
        caCert.load(new FileInputStream(trustcapath), Constant.TRUSTCAPWD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(caCert);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }

}
