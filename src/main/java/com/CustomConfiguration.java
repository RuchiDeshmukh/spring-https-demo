package com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomConfiguration {

	@Bean
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException,
			CertificateException, MalformedURLException, IOException, UnrecoverableKeyException {

		SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(new URL("file:src/main/resources/demo.jks"), "password".toCharArray())
				.build();
		
		//bypass hostname verification
		final SSLConnectionSocketFactory sslsf;
		sslsf = new SSLConnectionSocketFactory(sslContext,
		        NoopHostnameVerifier.INSTANCE);

		HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(sslsf).build();

		
		org.apache.hc.client5.http.impl.classic.CloseableHttpClient c5 = HttpClients.custom()
				.setConnectionManager(connectionManager).evictExpiredConnections()
				.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(c5);
		RestTemplate restTemplate = new RestTemplate(factory);

		return restTemplate;
	}
}