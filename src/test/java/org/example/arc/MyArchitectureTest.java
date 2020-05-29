package org.example.arc;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.swagger.annotations.Api;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "org.example.empApi")
public class MyArchitectureTest {


    @ArchTest
    public static final ArchRule resorces_shouldbe_annotated_with_api = classes().that().resideInAnyPackage("..resorces..").should().beAnnotatedWith(Api.class);

    //    Two stars captures an arbitrary number of packages, so each combination of packages (like api.foo, api.bar.baz) will become it's own slice and will thus be   checked for cycles.
    @ArchTest
    public static final ArchRule cycleTest = slices().matching("org.example.empApi.(*)..").should().beFreeOfCycles();
    @ArchTest
    static final ArchRule dao_must_reside_in_a_db_package =
            classes().that().haveNameMatching(".*DAO").should().resideInAPackage("..db..")
                    .as("DAOs should reside in a package '..dao..'");
    @ArchTest
    static final ArchRule services_must_reside_in_a_service_package =
            classes().that().haveNameMatching(".*service").or().haveNameMatching(".*Service").should().resideInAPackage("..service..")
                    .as("Servicess should reside in a package '..service..'");
    @ArchTest
    static final ArchRule configuration_must_reside_in_a_config_package =
            classes().that().haveNameMatching(".*Configuration").should().resideInAPackage("..config..")
                    .as("Configuration should reside in a package '..config..'");

    @ArchTest
    static final ArchRule resources_must_reside_in_a_resources_package =
            classes().that().haveNameMatching(".*Resource").should().resideInAPackage("..resources..")
                    .as("Resource should reside in a package '..resources..'");

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .layer("Controllers").definedBy("org.example.empApi.resources..")
            .layer("Services").definedBy("org.example.empApi.service..")
            .layer("Persistence").definedBy("org.example.empApi.db..")
            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers", "Services")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");
    @ArchTest
    static final ArchRule services_should_not_depend_on_resources =
            noClasses().that().resideInAPackage("..service..")
                    .should().dependOnClassesThat().resideInAPackage("..resources..");


    // 'access' catches only violations by real accesses, i.e. accessing a field, calling a method
    // 'dependOn' catches a wider variety of violations, e.g. having fields of type, having method parameters of type, extending type ...
    @ArchTest
    static final ArchRule dao_should_not_depend_on_services =
            noClasses().that().resideInAPackage("..db..")
                    .should().dependOnClassesThat().resideInAPackage("..service..");
    @ArchTest
    static final ArchRule services_should_only_be_depended_on_other_services_or_db =
            classes().that().resideInAPackage("..service..")
                    .should().dependOnClassesThat().resideInAnyPackage("..db..", "..service..", "..java..");
    @ArchTest
    private final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    private void no_access_to_standard_streams_as_method(JavaClasses classes) {
        noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
    }


}