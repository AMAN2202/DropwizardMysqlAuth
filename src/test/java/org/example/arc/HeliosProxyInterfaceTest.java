import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@ArchTag(value = "Helios_Proxy_implementation_Test")
@AnalyzeClasses(packages = {"com.flipkart.fpg.helios.proxy.lib", "org.example"})
public class HeliosProxyInterfaceTest {
    static DescribedPredicate<JavaClass> are_Interfaces =
            new DescribedPredicate<JavaClass>("is interface") {
                @Override
                public boolean apply(JavaClass input) {
                    return input.isInterface();
                }
            };
    static ArchCondition<JavaClass> be_Implemented =
            new ArchCondition<JavaClass>("interface must be implemented") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    boolean check=false;
                    for (Dependency dependency : item.getDirectDependenciesToSelf()) {
                        if(dependency.getDescription().matches(".*<org.example.*implements interface.*")){
                            check=true;
                        }
                    }
                    if(!check){
                        String message = String.format(
                                "Interface %s is not being implemented ", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                }

            };

    @ArchTest
    static final ArchRule every_public_function_in_res = classes().that(are_Interfaces).and().haveSimpleName("HeliosProxy")
            .and().resideInAPackage("")
            .should(be_Implemented);


}



