package uk.gov.api.springboot.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.base.DescribedPredicate.equalTo;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;

@AnalyzeClasses(packages = "uk.gov.api.springboot")
class ArchUnitTest {
  @ArchTest
  @SuppressWarnings("unused")
  ArchRule requireFinalFields = classesThatAreNotTests().should().haveOnlyFinalFields();

  @ArchTest
  @SuppressWarnings("unused")
  ArchRule requireNoAutowiredFieldInjection =
      classesThatAreNotTests()
          .and()
          .containAnyFieldsThat(
              describe(
                  "are Autowired by Spring",
                  f -> f.tryGetAnnotationOfType(Autowired.class).isPresent()))
          .should()
          .containNumberOfElements(equalTo(0));

  private GivenClassesConjunction classesThatAreNotTests() {
    return classes().that().haveNameNotMatching(".*Test").and().areNotAnnotatedWith(Nested.class);
  }
}
