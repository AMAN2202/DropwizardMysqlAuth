package org.example.arc;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.example.empApi.service")
public class HystrixTest {
    static Config config = new Config();


    static DescribedPredicate<JavaClass> classHavingDAOField =
            new DescribedPredicate<JavaClass>("have a DAO") {
                @Override
                public boolean apply(JavaClass input) {


                    for (JavaField javaField : input.getFields()) {

                        String filedName = javaField.getRawType().getName();
                        if (checker(filedName, config.getDaoList()))
                            return true;
                    }


                    for (JavaMethod javaMethod : input.getMethods()) {

                        for (JavaCall javaCall : javaMethod.getCallsFromSelf()) {


                            String className = javaCall.getTargetOwner().getName();
                            if (checker(className, config.getDaoList()))
                                return true;
                        }
                    }

                    return false;
                }
            };


    static DescribedPredicate<JavaClass> classHavingApiCall =
            new DescribedPredicate<JavaClass>("have a Api call") {
                @Override
                public boolean apply(JavaClass input) {

                    for (JavaField javaField : input.getFields()) {
                        if (checker(javaField.getRawType().getSimpleName(), config.getClientLibrary()))
                            return true;
                    }
                    for (JavaMethod javaMethod : input.getMethods()) {

                        for (JavaCall javaCall : javaMethod.getCallsFromSelf()) {


                            String className = javaCall.getTargetOwner().getName();
                            if (checker(className, config.getClientLibrary()))
                                return true;
                        }
                    }

                    return false;
                }
            };


    static ArchCondition<JavaClass> methodMustUseHystrixHavingDBCallOrApiCAll =
            new ArchCondition<JavaClass>("method must use @Hystrixcommand annotations") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {

                    for (JavaMethod javaMethod : item.getMethods()) {


                        for (JavaCall javaCall : javaMethod.getCallsFromSelf()) {

                            if ((methodMakingDbCall(javaCall) || methodCallingAPi(javaCall)) && methodNotUsingHystrix(javaMethod)) {
                                String message = String.format(
                                        "Method %s is not using @HystrixCommand annotation", javaCall.getOrigin().getFullName());
                                events.add(SimpleConditionEvent.violated(javaCall, message));

                            }

                        }
                    }

                }
            };
    @ArchTest
    private final ArchRule Hystrix_rule =
            classes().that(classHavingApiCall).or(classHavingDAOField).should(methodMustUseHystrixHavingDBCallOrApiCAll);

    private static boolean methodMakingDbCall(JavaCall javaCall) {
        return checker(javaCall.getTargetOwner().getName(), config.getDaoList());
    }

    private static boolean methodCallingAPi(JavaCall javaCall) {
        return checker(javaCall.getTargetOwner().getSimpleName(), config.getClientLibrary());
    }

    private static boolean checker(String name, List<String> list) {

        for (String possibleCriteria : list) {
            Pattern r = Pattern.compile(possibleCriteria);
            Matcher m = r.matcher(name);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    private static boolean methodNotUsingHystrix(JavaMethod javaMethod) {
        return !javaMethod.isAnnotatedWith(HystrixCommand.class);
    }
}