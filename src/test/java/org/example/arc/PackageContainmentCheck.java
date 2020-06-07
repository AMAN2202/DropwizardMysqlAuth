import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "DAO_Check")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class PackageContainmentCheck {
    @ArchTest
    static final ArchRule DAOs_Must_Be_In_db= classes().that().haveNameMatching(".*DAO")
                                             .should().resideInAPackage("..db..")
                                             .as("DAOs should reside in a package '..db..'");

    @ArchTest
    static final ArchRule Resources_Must_Be_In_resources= classes().that().haveNameMatching(".*Resource")
                                                         .should().resideInAPackage("..resources..")
                                                         .as("Resources should reside in a package '..resource..'");

    @ArchTest
    static final ArchRule Services_Must_Be_In_service= classes().that().haveNameMatching(".*Service")
            .should().resideInAPackage("..service..")
            .as("Resources should reside in a package '..service..'");

    @ArchTest
    static final ArchRule Exception_Classes_Must_Be_In_exception= classes().that().haveNameMatching(".*Exception")
            .should().resideInAPackage("..exception..")
            .as("Exception classes should reside in a package '..exception..'");

    @ArchTest
    static final ArchRule Api_must_be_in_resources= classes().that().
            areAnnotatedWith(io.swagger.annotations.Api.class).should().
            resideInAPackage("..resources..")
            .as("Apis annotated classes should reside in a package '..resources..'");

    @ArchTest
    static final ArchRule Entity_Must_Be_In_Model= classes().that().
            areAnnotatedWith(javax.persistence.Entity.class).should().resideInAPackage("..model..")
            .as("Entities should reside in a package '..model..'");


    @ArchTest
    static final ArchRule Resources_Must_Be_Reside_In_resources= classes().that().
            areAnnotatedWith(javax.ws.rs.Path.class).should().resideInAPackage("..resources..")
            .as("Path annotated classes should reside in a package '..resources..'");

    @ArchTest
    static final ArchRule service_package_should_only_contain_service_class=classes().that().resideInAPackage("..service..")
                                                                            .should().haveNameMatching(".*Service")
                                                                            .as("service package should only contain services");

    @ArchTest
    static final ArchRule db_package_should_only_contain_dao_class=classes().that().resideInAPackage("..db..")
            .should().haveNameMatching(".*DAO.*")
            .as("db package should only contain dao related classes");

    @ArchTest
    static final ArchRule resource_package_should_only_contain_resource_class=classes().that().resideInAPackage("..resource..")
            .should().haveNameMatching(".*Resource.*")
            .as("resource package should only contain resource related classes");

}
