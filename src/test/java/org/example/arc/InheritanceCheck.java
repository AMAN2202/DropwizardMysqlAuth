import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Inheritance_Check")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class InheritanceCheck {
    static DescribedPredicate<JavaClass> are_ResourceInterfaces =
            new DescribedPredicate<JavaClass>("is resource interface") {
                @Override
                public boolean apply(JavaClass input) {
                    return input.isInterface()&&input.getName().matches(".*Resource.*");
                }
            };
    static DescribedPredicate<JavaClass> extend_Exception_class =
            new DescribedPredicate<JavaClass>("extends exception class") {
                @Override
                public boolean apply(JavaClass input) {

                    return input.getSuperClass().getClass().getName().matches(".*Exception.*");
                }
            };

    @ArchTest
    static  final ArchRule resources_should_be_implemented_by_impl=classes().that()
            .implement(are_ResourceInterfaces)
            .should().haveSimpleNameContaining("Impl");

    @ArchTest
    static  final ArchRule exception_should_be_extended_by_exception_classes=classes().that(extend_Exception_class)
            .should().haveSimpleNameContaining("Exception");


}
