package org.example;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Configuration")
public class ConfigurationTest {

    @Test
    public void defaultNameTest()
    {
        CollabWorkDWConfiguration c=new CollabWorkDWConfiguration();
        assertEquals("Stranger",c.getDefaultName(),"Default name is \"Stranger\"");
    }

    @Test
    public void templateTest()
    {
        CollabWorkDWConfiguration c=new CollabWorkDWConfiguration();
        c.setTemplate("defaultTemplate");
        assertEquals("defaultTemplate",c.getTemplate(),"Template is \"defaultTemplate\"");
    }

}
