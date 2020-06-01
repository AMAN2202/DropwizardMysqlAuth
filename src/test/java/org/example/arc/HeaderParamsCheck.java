import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.lang.annotation.Annotation;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Header_Params_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class HeaderParamsCheck {
    private static String header_name="request_id";
    static ArchCondition<JavaClass> mustPassRequestIdHeaderUsingAnnotation =
            new ArchCondition<JavaClass>("must have @headerparams followed by request_id") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    String test_header_name=header_name;
                    for (JavaMethod javaMethod : item.getMethods()) {
                        if (javaMethod.getModifiers().contains(JavaModifier.PUBLIC)) {
                            boolean check = false;
                            Annotation[][] annotated_params =javaMethod.reflect().getParameterAnnotations();
                            for(int i=0;i<annotated_params.length;i++) {
                                for (int j = 0; j < annotated_params[i].length; j++) {

                                    if (annotated_params[i][j].toString().matches(".*HeaderParam.*"+test_header_name+".*")) {
                                        check = true;
                                        break;
                                    }

                                }
                                if (check == true) {
                                    break;
                                }
                            }

                            if (!check) {
                                String message = String.format(
                                        "Method %s is not having @HeaderParam(\""+test_header_name+"\")", javaMethod.getName());
                                events.add(SimpleConditionEvent.violated(javaMethod, message));
                            }
                        }

                    }
                }

            };
    @ArchTest
    static final ArchRule public_method_in_resources_should_pass_request_id_as_headerparam = classes().that().haveNameMatching(".*Resource")
            .and().areAnnotatedWith(javax.ws.rs.Path.class)
            .should(mustPassRequestIdHeaderUsingAnnotation);
}
