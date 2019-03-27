package demo.ms.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

@ConditionalOnMissingBean(TokenConfig.class)
@Configuration
public class TokenConfig {

    @Value("${security.oauth2.resource.jwt.keyValue}")
    private String publicKey;

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() throws IOException {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * JWT的token生成方式
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

//        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//        defaultAccessTokenConverter.setUserTokenConverter(new UserInfoAuthenticationConverter());
//        converter.setAccessTokenConverter(defaultAccessTokenConverter);

        converter.setVerifierKey(publicKey);

        return converter;
    }
}
