package uk.gov.api.springboot.application;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.api.springboot.domain.model.Registry;

@Component
public class NoopRegistry implements Registry {
  @Override
  public List<Entry> retrieveAll() {
    return Collections.emptyList();
  }
}
