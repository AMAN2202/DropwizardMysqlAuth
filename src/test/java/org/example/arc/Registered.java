import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaConstructorCall;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Registered_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class Registered {
    public static String class_name = "ContextRequestFilter";
    static ArchCondition<JavaClass> mustBeRegisteredInTheApplicationClass =
            new ArchCondition<JavaClass>("the given class must be registered in the application class") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {

                    boolean check = false;
                    for (JavaConstructorCall javaConstructorCall : item.getConstructorCallsToSelf()) {
                        if (javaConstructorCall.getOrigin().getFullName().contains("Application.run")) {
                                check = true;
                                break;

                        }
                        if (check == true) {
                            break;
                        }
                    }
                    if (!check) {
                        String message = String.format(
                                "Class %s is not being registered in application class", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                }
            };
    @ArchTest
    static final ArchRule every_public_function_in_res = classes().that().haveSimpleNameContaining(class_name)
            .should(mustBeRegisteredInTheApplicationClass);
}
