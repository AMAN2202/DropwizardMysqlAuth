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
import org.slf4j.impl.StaticLoggerBinder;

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
    static ArchCondition<JavaClass> all_Log_Methods_Should_Be_From_logback =
            new ArchCondition<JavaClass>("call slf4j logging methods") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    for (JavaMethod javaMethod : item.getMethods()) {
                        for (JavaMethodCall javaMethodCall : javaMethod.getMethodCallsFromSelf()) {
                            boolean check=true;
                            if (javaMethodCall.getDescription().matches(".*Logger.*")) {
                                if (javaMethodCall.getDescription().matches(".*org.slf4j.Logger.*")) {
                                    final StaticLoggerBinder binder = StaticLoggerBinder.getSingleton();
                                    if (!binder.getLoggerFactory().toString().matches(".*ch.qos.logback.*")) {
                                        check = false;
                                    }
                                } else if (!javaMethodCall.getDescription().matches(".*ch.qos.logback.*")) {
                                    check = false;
                                }
                            }
                            if(!check){
                                String message = String.format(
                                        "Class %s is having a logging method call %s in method %s which is not from logback ", item.getName(),javaMethodCall.getName(),javaMethod.getName());
                                events.add(SimpleConditionEvent.violated(item, message));
                            }
                        }

                    }
                }
            };


    @ArchTest
    static final ArchRule db_package_should_only_contain_dao_class=classes().that(classesHavingLoggingMethods)
                                                                    .should(all_Log_Methods_Should_Be_From_logback);


}
