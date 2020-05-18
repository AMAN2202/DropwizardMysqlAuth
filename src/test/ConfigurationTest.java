package test;

import org.example.DropwizardAuthConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Configuration")
public class ConfigurationTest {

    @Test
    public void defaultNameTest()
    {
        DropwizardAuthConfiguration c=new DropwizardAuthConfiguration();
        assertEquals("Stranger",c.getDefaultName(),"Default name is \"Stranger\"");
    }

    @Test
    public void templateTest()
    {
        DropwizardAuthConfiguration c=new DropwizardAuthConfiguration();
        c.setTemplate("defaultTemplate");
        assertEquals("defaultTemplate",c.getTemplate(),"Template is \"defaultTemplate\"");
    }

}