package org.example;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.example.empApi.config.DropwizardAuthConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Tag("Configuration")
@ExtendWith(DropwizardExtensionsSupport.class)
public class ConfigurationTest {

    @Test
    public void defaultNameTest() {

        DropwizardAuthConfiguration c = new DropwizardAuthConfiguration();
        c.setDefaultName("Stranger");
        assertEquals("Stranger", c.getDefaultName(), "Default name is \"Stranger\"");
    }

    @Test
    public void templateTest() {
        DropwizardAuthConfiguration c = new DropwizardAuthConfiguration();
        c.setTemplate("defaultTemplate");
        assertEquals("defaultTemplate", c.getTemplate(), "Template is \"defaultTemplate\"");
    }

}
