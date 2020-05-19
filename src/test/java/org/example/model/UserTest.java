package com.sahajjain.model;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final User user = new User("sahaj@jain.com", "jain123");

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/User.json"), User.class));

        assertThat(MAPPER.writeValueAsString(user)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final User user = new User("sahaj@jain.com", "jain123");

        assertThat(MAPPER.readValue(fixture("fixtures/User.json"), User.class))
                .isEqualTo(user);
    }
}
