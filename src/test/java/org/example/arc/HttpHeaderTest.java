import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages="org.example.resources")
public class HttpHeaderTest {
    static ArchCondition<JavaClass> mustHaveAHttpHeaderParameter =
            new ArchCondition<JavaClass>("must have a HttpHeader variable passed as parameter") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    for (JavaMethod javaMethod : item.getMethods()) {
                        if (javaMethod.getModifiers().contains(JavaModifier.PUBLIC)) {
                            boolean check = false;
                            for (JavaClass javaClass : javaMethod.getRawParameterTypes()) {
                                if (javaClass.getSimpleName().contains("HttpHeaders")) {
                                    check = true;
                                    break;
                                }
                            }
                            if (!check) {
                                String message = String.format(
                                        "Method %s is not having HttpHeader type parameter", javaMethod.getName());
                                events.add(SimpleConditionEvent.violated(javaMethod, message));
                            }

                        }
                    }

                }

            };

                @ArchTest
                static final ArchRule every_public_function_in_resource_class_should_have_HttpHeader_type_parameter = classes().that().haveNameMatching(".*Resource")
                        .should(mustHaveAHttpHeaderParameter);
            }




