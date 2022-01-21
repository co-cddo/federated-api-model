package uk.gov.api.springboot.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.base.DescribedPredicate.equalTo;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AnalyzeClasses(packages = "uk.gov.api")
class JMoleculesRulesTest {

  //  @ArchTest ArchRule dddRules = JMoleculesDddRules.all(); // (2)
  //  @ArchTest ArchRule onion = JMoleculesArchitectureRules.ensureOnionSimple(); // (2)

  // alternatively
  @ArchTest
  // (3)
  void detectsViolations(JavaClasses classes) {

    EvaluationResult result = // JMoleculesDddRules.all().evaluate(classes);
        JMoleculesArchitectureRules.ensureLayering().evaluate(classes);
    result.getFailureReport().getDetails().forEach(s -> System.out.println(s));

    String error =
        "Expected no violations, but instead there were:\n\n- "
            + String.join("\n- ", result.getFailureReport().getDetails());

    assertThat(result.hasViolation()).overridingErrorMessage(error).isFalse();
  }

  @ArchTest
  ArchRule r =
      classes()
          .that()
          .areAnnotatedWith(AutoConfigureMockMvc.class)
          .should()
          .resideInAPackage("..interfaces..");

  @ArchTest
  ArchRule r_ =
      classes()
          .that()
          .containAnyFieldsThat(
              describe(
                  "are instances of `MockMvc`", f -> f.getRawType().isEquivalentTo(MockMvc.class)))
          .should()
          .resideInAPackage("..interfaces..");

  @ArchTest
  ArchRule no =
      classes()
          .that()
          .haveNameNotMatching(".*Test.*")
          .and()
          .areTopLevelClasses()
          .and()
          .containAnyFieldsThat(
              describe(
                  "are Autowired by Spring",
                  f -> f.tryGetAnnotationOfType(Autowired.class).isPresent()))
          .should()
          .containNumberOfElements(equalTo(0));
}
