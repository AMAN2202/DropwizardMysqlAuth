import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@Slf4j
@ArchTag(value = "Request_Filter_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class RequestFilterTest {
    static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    static JsonNode json;

    static {
        try {
            json = mapper.readTree(new File("src\\test\\java\\headers_config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String class_name=json.get("headers").get("classname").asText();
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

    static ArchCondition<JavaClass> mustSetTheGivenHeaderInTheGivenFilterClass =
            new ArchCondition<JavaClass>("must set the given header in the given filter class") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    for(String test_header_name:headers_name){
                        boolean check = false;
                        for (JavaMethod javaMethod : item.getMethods()) {
                            for (JavaConstructorCall javaConstructorCall : javaMethod.getConstructorCallsFromSelf()) {
                                for (JavaField javaField : javaConstructorCall.getTargetOwner().getFields()) {
                                    if (javaField.getFullName().matches(".*" + test_header_name + ".*")) {
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
                            if(!optional_headers_name.contains(test_header_name)) {
                                String message = String.format("Class %s is not having a method which creates a context with %s header", item.getName(), test_header_name);
                                events.add(SimpleConditionEvent.violated(item, message));
                            }
                            else{
                                log.warn(test_header_name+" is not being set by filter class");
                            }
                        }

                    }


                }
            };
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
            .should(mustSetTheGivenHeaderInTheGivenFilterClass)
            .andShould(mustBeRegisteredInTheApplicationClass);


}


