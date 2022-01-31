package uk.gov.api.springboot.models.metadata.v1alpha;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import uk.gov.api.models.metadata.v1alpha.ErrorResponse;

@JsonTest
public class ErrorResponseTest {

  @Autowired private JacksonTester<ErrorResponse> jacksonTester;

  private JsonContent<ErrorResponse> jsonContent;

  @Nested
  class Serialization {

    @Test
    void errorIsSerialized() throws IOException {
      var errorResponse = new ErrorResponse();
      errorResponse.setError(ErrorResponse.Error.INVALID_REQUEST);

      jsonContent = jacksonTester.write(errorResponse);

      assertThat(jsonContent).extractingJsonPathStringValue("error").isEqualTo("invalid_request");
    }

    @Test
    void errorDescriptionIsSerialized() throws IOException {
      var errorResponse = new ErrorResponse();
      errorResponse.setErrorDescription("An error");

      jsonContent = jacksonTester.write(errorResponse);

      assertThat(jsonContent)
          .extractingJsonPathStringValue("error-description")
          .isEqualTo("An error");
    }
  }
}
