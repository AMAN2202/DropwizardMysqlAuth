import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaConstructorCall;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Request_Filter_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class RequestFilterTest {
    public static String class_name = "ContextRequestFilter";
    public static String header_name = "request_Id";
    static ArchCondition<JavaClass> mustSetTheGivenHeaderInTheGivenFilterClass =
            new ArchCondition<JavaClass>("must set the given header in the given filter class") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    String test_header_name = header_name;
                    boolean check = false;
                    for (JavaMethod javaMethod : item.getMethods()) {
                        for (JavaConstructorCall javaConstructorCall : javaMethod.getConstructorCallsFromSelf()) {
                            for (JavaField javaField : javaConstructorCall.getTargetOwner().getFields()) {
                                if (javaField.getFullName().matches(".*"+test_header_name+".*")) {
                                    check = true;
                                    break;
                                }
                            }
                            if (check == true) {
                                break;
                            }

                        }
                        if (check == true) {
                            break;
                        }
                    }
                    if (!check) {
                        String message = String.format(
                                "Class %s is not having a method which creates a context with %s header", item.getName(), test_header_name);
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                }
            };
    @ArchTest
    static final ArchRule every_public_function_in_res = classes().that().haveSimpleNameContaining(class_name)
                                                        .should(mustSetTheGivenHeaderInTheGivenFilterClass);

}


