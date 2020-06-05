import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ArchTag(value = "Cryptex_Test")
@AnalyzeClasses(packages = {"CryptexPackage", "org.example"})
public class CryptexTest {
    @Test
    public void isPackagePresent() throws Exception {
        assertEquals(new ClassFileImporter().importPackages("..CryptexPackage..").isEmpty(),false,"Cryptex Library is not added as dependency" );

    }
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
    static ArchCondition<JavaClass> must_initialize_cryptex_client =
            new ArchCondition<JavaClass>("cryptex client must be declared and initialized") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                    boolean check=false,cryptex_client_field=false;
                    for (JavaField javaField:item.getFields()) {
                        if(javaField.getRawType().getDescription().matches(".*CryptexClient.*")){
                            cryptex_client_field=true;
                            break;
                        }
                    }
                    if(!cryptex_client_field){
                        String message = String.format(
                                "Class %s implements interface but does not have CryptexClient type data member ", item.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                    else{
                        for(Dependency dependency:item.getDirectDependenciesFromSelf()){
                            if(dependency.getDescription().matches(".*constructor.*CryptexClientImpl.*Builder.*")){
                                check=true;
                                break;
                            }
                        }
                        if(!check){
                            String message = String.format(
                                    "Class %s has CryptexClient type data member but does not initialize it ", item.getName());
                            events.add(SimpleConditionEvent.violated(item, message));
                        }
                    }
                }

            };
    @ArchTest
    static final ArchRule interface_in_cryptex_lib_must_be_implemented = classes().that(are_Interfaces).and().haveSimpleNameContaining("CryptextClient")
            .and().resideInAPackage("")
            .should(be_Implemented);
    @ArchTest
    static final ArchRule class_implmenting_cryptex_client_interface_should_declare_and_initialize_cryptex_client_variable =
            classes().that().implement("CryptexClient")
            .should(must_initialize_cryptex_client);
}
