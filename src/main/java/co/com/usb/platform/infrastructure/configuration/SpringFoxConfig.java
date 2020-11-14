package co.com.usb.platform.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SpringFoxConfig
 *
 * @author USB
 * @version 1.0
 * @since 2020-11-14
 *
 */

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.any()).paths(PathSelectors.any())
        .build();
  }

}
