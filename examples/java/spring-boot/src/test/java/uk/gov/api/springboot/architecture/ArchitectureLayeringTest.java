package uk.gov.api.springboot.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@AnalyzeClasses(packages = "uk.gov.api.springboot")
class ArchitectureLayeringTest {
  private static final String INTERFACE = "..interfaces..";

//  @ArchTest
//  @SuppressWarnings("unused")
//  ArchRule layeredArchitecture = JMoleculesArchitectureRules.ensureLayeringStrict();

  @ArchTest
  @SuppressWarnings("unused")
  ArchRule mockMvcAnnotationShouldOnlyBeUsedInInterfaceLayer =
      classes()
          .that()
          .areAnnotatedWith(AutoConfigureMockMvc.class)
          .should()
          .resideInAPackage(INTERFACE);

  @ArchTest
  @SuppressWarnings("unused")
  ArchRule webMvcTestAnnotationShouldOnlyBeUsedInInterfaceLayer =
      classes().that().areAnnotatedWith(WebMvcTest.class).should().resideInAPackage(INTERFACE);

  @ArchTest
  @SuppressWarnings("unused")
  ArchRule mockMvcShouldOnlyBeUsedInInterfaceLayer =
      classes()
          .that()
          .containAnyFieldsThat(
              describe(
                  "are instances of `MockMvc`", f -> f.getRawType().isEquivalentTo(MockMvc.class)))
          .should()
          .resideInAPackage(INTERFACE);
}
