package uk.gov.api.springboot.models.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import models.metadata.ApiMetadata;
import models.metadata.BulkMetadataResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class BulkMetadataResponseTest {

  @Autowired private JacksonTester<BulkMetadataResponse> jacksonTester;

  private JsonContent<BulkMetadataResponse> jsonContent;

  @Nested
  class Serialization {

    @Test
    void apisIsSerializedWhenPresent() throws IOException {
      var response = new BulkMetadataResponse();
      var apiMetadata = new ArrayList<ApiMetadata>();
      var apiMetadataObject = new ApiMetadata();
      apiMetadata.add(apiMetadataObject);
      response.setApis(apiMetadata);

      jsonContent = jacksonTester.write(response);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").hasSize(1);
    }

    @Test
    void apisIsSerializedAsAnEmptyArrayWhenEmpty() throws IOException {
      var apiMetadata = new ArrayList<ApiMetadata>();
      var response = new BulkMetadataResponse();
      response.setApis(apiMetadata);

      jsonContent = jacksonTester.write(response);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isEmpty();
    }

    @Test
    void apisIsNullWhenResponseIsNull() throws IOException {
      var response = new BulkMetadataResponse();
      response.setApis(null);

      jsonContent = jacksonTester.write(response);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isNull();
    }

    @Test
    void apisIsSerializedAsAnEmptyArrayWhenNotInitialised() throws IOException {
      var response = new BulkMetadataResponse();

      jsonContent = jacksonTester.write(response);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isEmpty();
    }

    @Test
    void apiVersionIsSerialized() throws IOException {
      var response = new BulkMetadataResponse();
      response.setApiVersion(BulkMetadataResponse.ApiVersion.API_GOV_UK_V_1_ALPHA);

      jsonContent = jacksonTester.write(response);

      assertThat(jsonContent)
          .extractingJsonPathStringValue("api-version")
          .isEqualTo("api.gov.uk/v1alpha");
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
