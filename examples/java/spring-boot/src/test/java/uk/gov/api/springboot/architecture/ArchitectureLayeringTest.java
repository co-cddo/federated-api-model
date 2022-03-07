package uk.gov.api.springboot.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.describe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.jmolecules.architecture.onion.classical.ApplicationServiceRing;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AnalyzeClasses(packages = "uk.gov.api.springboot")
class ArchitectureLayeringTest {
  private static final String APPLICATION_RING = "..application..";
  private static final String INFRASTRUCTURE_RING = "..infrastructure..";
  private static final String DOMAIN_RING = "..domain..";

  @ArchTest
  void onionArchitecture(JavaClasses classes) {
    JMoleculesArchitectureRules.ensureOnionClassical().check(classes);
  }

  @ArchTest
  void mockMvcAnnotationShouldOnlyBeUsedInInfrastructureRing(JavaClasses classes) {
    classes()
        .that()
        .areAnnotatedWith(AutoConfigureMockMvc.class)
        .should()
        .resideInAPackage(INFRASTRUCTURE_RING)
        .check(classes);
  }

  @ArchTest
  void webMvcTestAnnotationShouldOnlyBeUsedInInfrastructureRing(JavaClasses classes) {
    classes()
        .that()
        .areAnnotatedWith(WebMvcTest.class)
        .should()
        .resideInAPackage(INFRASTRUCTURE_RING)
        .check(classes);
  }

  @ArchTest
  void springBootTestAnnotationShouldOnlyBeUsedInApplicationOrInfrastructureRing(
      JavaClasses classes) {
    classes()
        .that()
        .areAnnotatedWith(SpringBootTest.class)
        .should()
        .resideInAnyPackage(APPLICATION_RING, INFRASTRUCTURE_RING)
        .check(classes);
  }

  @ArchTest
  void springBootApplicationAnnotationShouldOnlyBeUsedInApplicationRing(JavaClasses classes) {
    classes()
        .that()
        .areAnnotatedWith(SpringBootApplication.class)
        .should()
        .resideInAPackage(APPLICATION_RING)
        .orShould()
        .beAnnotatedWith(ApplicationServiceRing.class)
        .check(classes);
  }

  @ArchTest
  void mockMvcShouldOnlyBeUsedInInfrastructureRing(JavaClasses classes) {
    classes()
        .that()
        .containAnyFieldsThat(
            describe(
                "are instances of `MockMvc`", f -> f.getRawType().isEquivalentTo(MockMvc.class)))
        .should()
        .resideInAPackage(INFRASTRUCTURE_RING)
        .check(classes);
  }

  @ArchTest
  void domainRingIsSelfSufficient(JavaClasses classes) {
    classes()
        .that()
        .haveNameNotMatching(".*Test")
        .and()
        .areNotAnnotatedWith(Nested.class)
        .and()
        .resideInAPackage(DOMAIN_RING)
        .should()
        .onlyAccessClassesThat()
        .resideInAnyPackage(DOMAIN_RING, "java..", "org.slf4j..")
        .check(classes);
  }
}
