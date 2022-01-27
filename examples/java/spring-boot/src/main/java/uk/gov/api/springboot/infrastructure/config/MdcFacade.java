package uk.gov.api.springboot.infrastructure.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MdcFacade {

  public static final String CORRELATION_ID = "correlation-id";

  public void put(String key, String value) {
    MDC.put(key, value);
  }

  public void clear() {
    MDC.clear();
  }
}
