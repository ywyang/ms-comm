package demo.ms.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean(JwtFilterConfig.class)
@ServletComponentScan("demo.ms.common.filter")
@Configuration
public class JwtFilterConfig {
}
