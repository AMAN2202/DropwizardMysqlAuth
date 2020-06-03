import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

import java.io.File;
import java.io.IOException;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Registered_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class Registered {

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
    static ArchCondition<JavaClass> mustBeRegisteredInTheApplicationClass =
            new ArchCondition<JavaClass>(" be registered in the application class") {
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
    static final ArchRule filter_class_should_be_registered = classes().that().haveSimpleNameContaining(class_name)
            .should(mustBeRegisteredInTheApplicationClass);
}
