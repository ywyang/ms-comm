package demo.ms.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    ClientHttpRequestInterceptor interceptor =
            (ClientHttpRequestInterceptor) (httpRequest, bytes,clientHttpRequestExecution)
                    -> {

                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)
                        SecurityContextHolder.getContext().getAuthentication().getDetails();
                String tokenType = oAuth2AuthenticationDetails.getTokenType();
                String tokenValue = oAuth2AuthenticationDetails.getTokenValue();
                HttpHeaders headers = httpRequest.getHeaders();
                headers.add("Authorization",String.format("%s %s",tokenType,tokenValue));
                return clientHttpRequestExecution.execute(httpRequest,bytes);
            };

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplateBuilder()
                    .additionalInterceptors(interceptor)
                    .build();
    }
}
