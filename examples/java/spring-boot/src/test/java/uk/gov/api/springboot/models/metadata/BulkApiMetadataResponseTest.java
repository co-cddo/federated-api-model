package uk.gov.api.springboot.models.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class BulkApiMetadataResponseTest {

  @Autowired private JacksonTester<BulkApiMetadataResponse> jacksonTester;

  private JsonContent<BulkApiMetadataResponse> jsonContent;

  @Nested
  class Serialization {

    @Test
    void apisIsSerializedWhenPresent() throws IOException {
      BulkApiMetadataResponse bulkApiMetadataResponse = new BulkApiMetadataResponse();
      List<ApiMetadata> apiMetadata = new ArrayList<>();
      ApiMetadata apiMetadataObject = new ApiMetadata();
      apiMetadata.add(apiMetadataObject);
      bulkApiMetadataResponse.setApis(apiMetadata);

      jsonContent = jacksonTester.write(bulkApiMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").hasSize(1);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void apisIsSerializedAsAnEmptyArrayWhenNullOrEmpty(List<ApiMetadata> apiMetadata)
        throws IOException {
      BulkApiMetadataResponse bulkApiMetadataResponse = new BulkApiMetadataResponse();
      bulkApiMetadataResponse.setApis(apiMetadata);

      jsonContent = jacksonTester.write(bulkApiMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isEmpty();
    }

    @Test
    void apisIsSerializedAsAnEmptyArrayWhenNotInitialised() throws IOException {
      BulkApiMetadataResponse bulkApiMetadataResponse = new BulkApiMetadataResponse();

      jsonContent = jacksonTester.write(bulkApiMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isEmpty();
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
