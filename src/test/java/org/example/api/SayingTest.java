package org.example.api;

import org.example.empApi.model.Saying;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SayingTest {

    @Test
    public void defaultNameTest() {
        Saying s = new Saying(1, "testData");
        assertEquals(1, s.getId(), "ID should be equal to 1");
    }

    @Test
    public void templateTest() {
        Saying s = new Saying(1, "testData");
        assertEquals("testData", s.getContent(), "content should be equal to \"testData\"");
    }
}
