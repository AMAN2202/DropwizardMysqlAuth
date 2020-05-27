package org.example.empApi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public static void serializesToJSON() throws Exception {
        final Employee employee = new Employee(1, "Sahaj Jain", 100000);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/Employee.json"), Employee.class));

        assertTrue(MAPPER.writeValueAsString(employee).equals(expected));
    }
//
//    @Test
//    public void deserializesFromJSON() throws Exception {
//        final Employee employee = new Employee(1,"Sahaj Jain", 100000);
//
//        assertThat(MAPPER.readValue(fixture("fixtures/Employee.json"), Employee.class))
//                .isEqualTo(employee);
//    }
}
