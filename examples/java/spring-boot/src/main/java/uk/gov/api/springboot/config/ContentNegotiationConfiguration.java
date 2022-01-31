package uk.gov.api.springboot.config;

import me.jvt.spring.ContentNegotiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;

@Configuration
public class ContentNegotiationConfiguration {

  @Bean
  public ContentNegotiator contentNegotiator(ContentNegotiationManager contentNegotiationManager) {
    return new ContentNegotiator(contentNegotiationManager, "application/vnd.uk.gov.api.v1alpha+json");
  }

}
