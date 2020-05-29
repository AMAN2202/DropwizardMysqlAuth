package com.sahajjain.rules;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class sampleCheck {

    @PublicAPI(usage = ACCESS)
     public static final ArchRule check1=classes().that().haveSimpleName("EmployeeDAOImpl")
            .should().beInterfaces();

}