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

import javax.ws.rs.client.Client;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.example.empApi.service")
public class HystrixTest {

    static DescribedPredicate<JavaClass> classHavingDAOField =
            new DescribedPredicate<JavaClass>("have a DAO") {
                @Override
                public boolean apply(JavaClass input) {


                    for (JavaField javaField : input.getFields()) {
                        if (javaField.getRawType().getName().contains("DAO"))
                            return true;
                    }


                    for (JavaMethod javaMethod : input.getMethods()) {
                        for (JavaCall javaCall : javaMethod.getCallsFromSelf()) {
                            if (javaCall.getTargetOwner().getName().contains("DAO")) {
                                return true;
                            }
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
                        if (javaField.getRawType().getSimpleName().equals(Client.class.getSimpleName()))
                            return true;
                    }

                    return false;
                }
            };


    static ArchCondition<JavaClass> mustUseHystrix =
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


    private static boolean methodMakingDbCall(JavaCall javaCall) {
        return javaCall.getTargetOwner().getName().contains("DAO");
    }

    private static boolean methodCallingAPi(JavaCall javaCall) {
//        System.out.println(javaCall.getTargetOwner().getSimpleName()+" "+Client.class.getSimpleName());
        return javaCall.getTargetOwner().getSimpleName().equals(Client.class.getSimpleName());
    }

    private static boolean methodNotUsingHystrix(JavaMethod javaMethod) {
        return !javaMethod.isAnnotatedWith(HystrixCommand.class);
    }



    @ArchTest
    private final ArchRule Hystrix_rule =
            classes().that(classHavingApiCall).or(classHavingDAOField).should(mustUseHystrix);
}