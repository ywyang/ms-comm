package demo.ms.common.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaodawei on 2017/3/27.
 */
@ConditionalOnMissingBean(SwaggerConfig.class)
@EnableOAuth2Client
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.oauth.base-url}")
    private String oAuthBaseUri;

    @Value("${swagger.oauth.client-id}")
    private String clientId;

    @Value("${swagger.oauth.client-secret}")
    private String clientSecret;

    @Value("${swagger.service-name}")
    private String serviceName;

    @Value("${swagger.service-desc}")
    private String serviceDesc;

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(oAuth2()))
                .securityContexts(Arrays.asList(securityContext()))
                .useDefaultResponseMessages(false);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(serviceName)
                .description(serviceDesc)
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        SecurityConfiguration securityConfiguration = new SecurityConfiguration(clientId,clientSecret,"","","",
                ApiKeyVehicle.HEADER,""," ");
        return securityConfiguration;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope readScope = new AuthorizationScope("read_scope", "读取权限");
        AuthorizationScope writeScope = new AuthorizationScope("write_scope", "写入权限");
        AuthorizationScope adminScope = new AuthorizationScope("admin_scope", "管理权限");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = readScope;
        authorizationScopes[1] = writeScope;
        authorizationScopes[2] = adminScope;
        return Arrays.asList(new SecurityReference("oauth2schema", authorizationScopes));
    }

    private List<AuthorizationScope> scopes(){
        return Arrays.asList(
                new AuthorizationScope("read_scope","读取权限"),
                new AuthorizationScope("write_scope","写入权限"),
                new AuthorizationScope("admin_scope","管理权限"));
    }

    private SecurityScheme oAuth2(){

        String TOKEN_REQUEST_URL = oAuthBaseUri+"/oauth/authorize";
        String TOKEN_URL = oAuthBaseUri+"/oauth/token";

//        GrantType grantType = new AuthorizationCodeGrantBuilder()
//                .tokenEndpoint(new TokenEndpoint(TOKEN_URL, "access_token"))
//                .tokenRequestEndpoint(new TokenRequestEndpoint(TOKEN_REQUEST_URL, clientId, clientSecret ))
//                .build();

        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(TOKEN_URL);

        return new OAuth("oauth2schema",scopes(),Arrays.asList(grantType));
    }
}
