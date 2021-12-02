package uk.gov.api.springboot.models.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
      BulkMetadataResponse BulkMetadataResponse = new BulkMetadataResponse();
      List<ApiMetadata> apiMetadata = new ArrayList<>();
      ApiMetadata apiMetadataObject = new ApiMetadata();
      apiMetadata.add(apiMetadataObject);
      BulkMetadataResponse.setApis(apiMetadata);

      jsonContent = jacksonTester.write(BulkMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").hasSize(1);
    }

    @Test
    void apisIsSerializedAsAnEmptyArrayWhenEmpty() throws IOException {
      List<ApiMetadata> apiMetadata = new ArrayList<>();
      BulkMetadataResponse bulkMetadataResponse = new BulkMetadataResponse();
      bulkMetadataResponse.setApis(apiMetadata);

      jsonContent = jacksonTester.write(bulkMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isEmpty();
    }

    @Test
    void apisIsNullWhenResponseIsNull() throws IOException {
      BulkMetadataResponse bulkMetadataResponse = new BulkMetadataResponse();
      bulkMetadataResponse.setApis(null);

      jsonContent = jacksonTester.write(bulkMetadataResponse);

      assertThat(jsonContent).extractingJsonPathArrayValue("apis").isNull();
    }

    @Test
    void apisIsSerializedAsAnEmptyArrayWhenNotInitialised() throws IOException {
      BulkMetadataResponse BulkMetadataResponse = new BulkMetadataResponse();

      jsonContent = jacksonTester.write(BulkMetadataResponse);

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
