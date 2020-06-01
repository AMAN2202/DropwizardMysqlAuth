package org.example.arc;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "org.example.empApi.resources")
public class MetricsTest {
    static Config config = new Config();

    /*
    to Stores names of annotations that are missing from controller
     */
    static List<String> voilated;


    static ArchCondition<JavaClass> methodOfResourcesMustUseMetrics =
            new ArchCondition<JavaClass>("method must use annotations metrics") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    /*
                    To exclude classes form checks
                     */
                    if (checker(item.getName(), config.getExclude()))
                        return;

                    for (JavaMethod javaMethod : item.getMethods()) {


                        if (methodIsController(javaMethod) && methodNotUsingMetrics(javaMethod)) {
                            String message = String.format(
                                    "Method %s is not using metrics annotation %s", javaMethod.getFullName(), voilated);
                            events.add(SimpleConditionEvent.violated(javaMethod, message));

                        }


                    }

                }
            };
    @ArchTest
    private final ArchRule MetricsRule =
            classes().that().resideInAnyPackage("..resources..").should(methodOfResourcesMustUseMetrics);

    private static boolean methodIsController(JavaMethod javaMethod) {

        for (String annotationsToCheck : config.getControllerRequestAnnotations()) {
            /*
            to get class for name of annotations
            if c is null-> class name for annotation is not specified properly, just skip it
             */
            Class classForAnnotation = getClassForName(annotationsToCheck);

            if (classForAnnotation != null && javaMethod.isAnnotatedWith(classForAnnotation)) {
                return true;
            }

        }
        return false;
    }

    private static boolean methodNotUsingMetrics(JavaMethod javaMethod) {

        voilated = new ArrayList<>();
        for (String annotationsToCheck : config.getMetricsAnnotations()) {
            /*
            to get class for name of annotations
            if classForAnnotation is null-> class name for annotation is not specified properly, just skip it
             */
            Class classForAnnotationToCheck = getClassForName(annotationsToCheck);
            if (classForAnnotationToCheck != null && !javaMethod.isAnnotatedWith(classForAnnotationToCheck))
                voilated.add(classForAnnotationToCheck.getSimpleName());
        }

        /*
        if voilated is empty -> no voilations
         */
        return !voilated.isEmpty();

    }

    /*
    Return objrct of type class for a given string or null if unable to create class object
     */
    private static Class getClassForName(String className) {
        Class classForAnnotation = null;
        try {
            classForAnnotation = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return classForAnnotation;
        }
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
}