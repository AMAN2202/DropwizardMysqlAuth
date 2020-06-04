import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "HeliosProxy_Test")
@AnalyzeClasses(packages="org.example",importOptions = {ImportOption.DoNotIncludeJars.class,
        ImportOption.DoNotIncludeTests.class,ImportOption.DoNotIncludeArchives.class})
public class HeliosProxyFilterTest {

    static ArchCondition<JavaClass> add_helios_proxy_servlet_filter_in_run_method =
            new ArchCondition<JavaClass>("add_helios_proxy_servlet_filter") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    boolean addServletFilter=false,heliosProxyServletFilterConstructor=false,heliosProxyConfiguration=false;
                    for (JavaMethod javaMethod : item.getMethods()) {
                        if (javaMethod.getName().matches(".*run.*")) {
                            for (JavaCall javaCall : javaMethod.getCallsFromSelf()) {
                                if(javaCall.getDescription().matches(".*ServletEnvironment.addFilter.*")){
                                    addServletFilter=true;
                                }
                                if(javaCall.getDescription().matches(".*constructor.*HeliosProxyServletFilter.*")){
                                    heliosProxyServletFilterConstructor=true;
                                }
                                if(javaCall.getDescription().matches(".*getProxyConfiguration.*")){
                                    heliosProxyConfiguration=true;
                                }

                            }
                        }
                    }
                    if(!addServletFilter) {
                        String message = String.format(
                                "No servlet filter is added in run() of %s class ", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                    if(!heliosProxyServletFilterConstructor){
                        String message = String.format(
                                "No object of HeliosProxyServletFilter is being created in run() of%s class ", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                    if(!heliosProxyConfiguration){
                        String message = String.format(
                                "ProxyConfiguration is not being accessed in run() of %s class ", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                }
            };



    @ArchTest
    final ArchRule application_should_add_helios_proxy_servlet_filter = classes().that()
            .haveSimpleNameContaining("Application")
            .should(add_helios_proxy_servlet_filter_in_run_method);

}
