import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
@ArchTag(value = "Logback_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class LogbackTest {
    static DescribedPredicate<JavaClass> classesHavingLoggingMethods =
            new DescribedPredicate<JavaClass>("has a logging method") {
                @Override
                public boolean apply(JavaClass input) {
                    boolean check=false;

                    for(JavaMethod javaMethod:input.getMethods()){
                        for(JavaMethodCall javaMethodCall:javaMethod.getMethodCallsFromSelf()){
                            if(javaMethodCall.getDescription().matches(".*Logger.*")){
                                check=true;
                                break;
                            }
                        }
                        if(check)
                            break;
                    }
                    return check;
                }
            };
    static ArchCondition<JavaClass> all_Log_Methods_Should_Be_From_slf4j_or_logback =
            new ArchCondition<JavaClass>("call slf4j logging methods") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    for (JavaMethod javaMethod : item.getMethods()) {
                        for (JavaMethodCall javaMethodCall : javaMethod.getMethodCallsFromSelf()) {
                            if (javaMethodCall.getDescription().matches(".*Logger.*")) {
                                if (!(javaMethodCall.getDescription().matches(".*org.slf4j.Logger.*")||javaMethodCall.getDescription().matches(".*ch.qos.logback.*"))) {
                                    String message = String.format(
                                            "Class %s is having a logging method call %s in method %s which is not from slf4j or logback package", item.getName(),javaMethodCall.getName(),javaMethod.getName());
                                    events.add(SimpleConditionEvent.violated(item, message));
                                }
                            }
                        }

                    }
                }
            };


    @ArchTest
    static final ArchRule slf4j_log_methods = classes().that(classesHavingLoggingMethods)
            .should(all_Log_Methods_Should_Be_From_slf4j_or_logback);

}
