import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
@Slf4j
@ArchTag(value = "Header_Params_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class HeaderParamsCheck {
    static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    static JsonNode json;

    static {
        try {
            json = mapper.readTree(new File("src\\test\\java\\headers_config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static List<String> headers_name = new ArrayList<>();
    private static List<String> optional_headers_name=new ArrayList<>();
    static
    {
        for (JsonNode node : json.get("headers").get("headers_name")) {
            headers_name.add(node.asText());
        }
        for (JsonNode node : json.get("headers").get("optional_headers_name")) {
            optional_headers_name.add(node.asText());
        }

    }
    static ArchCondition<JavaClass> mustPassRequestIdHeaderUsingAnnotation =
            new ArchCondition<JavaClass>("must have @headerparams followed by request_id") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {

                    for (JavaMethod javaMethod : item.getMethods()) {
                        if (javaMethod.getModifiers().contains(JavaModifier.PUBLIC)) {

                            for (String test_header_name : headers_name) {
                                boolean check_header = false;
                                Annotation[][] annotated_params = javaMethod.reflect().getParameterAnnotations();
                                for (int i = 0; i < annotated_params.length; i++) {
                                    for (int j = 0; j < annotated_params[i].length; j++) {
                                        if (annotated_params[i][j].toString().matches(".*HeaderParam.*" + test_header_name + ".*")) {
                                            check_header = true;
                                            break;
                                        }
                                    }
                                    if (check_header == true) {
                                        break;
                                    }
                                }
                                if (!check_header) {
                                    if(!optional_headers_name.contains(test_header_name)) {
                                        String message = String.format(
                                                "Method %s is not having @HeaderParam(\"" + test_header_name + "\")", javaMethod.getName());
                                        events.add(SimpleConditionEvent.violated(javaMethod, message));
                                    }
                                    else{
                                        log.warn(test_header_name+" is not being passed as parameter in "+javaMethod.getName()+" in "+item.getName());
                                    }
                                }
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
