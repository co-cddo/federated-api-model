package uk.gov.api.springboot.models.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class ApiMetadataTest {

  @Autowired private JacksonTester<ApiMetadata> jacksonTester;

  private JsonContent<ApiMetadata> jsonContent;

  @Nested
  class Serialization {

    @Test
    void apiVersionIsSerialized() throws IOException {
      ApiMetadata apiMetadata = new ApiMetadata();
      apiMetadata.setApiVersion("v1-v2");

      jsonContent = jacksonTester.write(apiMetadata);

      assertThat(jsonContent).extractingJsonPathStringValue("api-version").isEqualTo("v1-v2");
    }

    @Test
    void dataIsSerialized() throws IOException {
      Data data = new Data();
      ApiMetadata apiMetadata = new ApiMetadata();
      apiMetadata.setData(data);

      jsonContent = jacksonTester.write(apiMetadata);

      assertThat(jsonContent).hasJsonPathMapValue("data");
    }
  }

  @Nested
  class Deserialization {

    @Disabled(
        "We don't want to do this until we're parsing or we're going to "
            + " rely on a library to do this for us.")
    @Test
    void toDo() {
      fail("Not yet implemented");
    }
  }
}
