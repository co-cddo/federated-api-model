package uk.gov.api.springboot.infrastructure.models.metadata.v1alpha;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
class DataTest {

  @Autowired private JacksonTester<Data> jacksonTester;

  private JsonContent<Data> jsonContent;

  @Nested
  class Serialization {
    @BeforeEach
    void setup() throws IOException {
      var data = new Data();
      data.setName("some name");
      data.setDescription("some description");
      data.setUrl(URI.create("https://www.example.foo"));
      data.setContact("some contact");
      data.setOrganisation("some organisation");
      data.setDocumentationUrl(URI.create("https://www.example.bar"));
      jsonContent = jacksonTester.write(data);
    }

    @Test
    void nameIsSerialized() {
      assertThat(jsonContent).extractingJsonPathStringValue("name").isEqualTo("some name");
    }

    @Test
    void descriptionIsSerialized() {
      assertThat(jsonContent)
          .extractingJsonPathStringValue("description")
          .isEqualTo("some description");
    }

    @Test
    void urlIsSerialized() {
      assertThat(jsonContent)
          .extractingJsonPathStringValue("url")
          .isEqualTo("https://www.example.foo");
    }

    @Test
    void contactIsSerialized() {
      assertThat(jsonContent).hasJsonPathStringValue("contact", "some contact");
    }

    @Test
    void organisationIsSerialized() {
      assertThat(jsonContent).hasJsonPathStringValue("organisation", "some organisation");
    }

    @Test
    void documentationUrlIsSerialized() {
      assertThat(jsonContent).hasJsonPathStringValue("documentation-url", "some documentation url");
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
