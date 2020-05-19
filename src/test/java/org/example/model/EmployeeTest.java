package com.sahajjain.model;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final Employee employee = new Employee(1,"Sahaj Jain", 100000);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/Employee.json"), Employee.class));

        assertThat(MAPPER.writeValueAsString(employee)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Employee employee = new Employee(1,"Sahaj Jain", 100000);

        assertThat(MAPPER.readValue(fixture("fixtures/Employee.json"), Employee.class))
                .isEqualTo(employee);
    }
}
