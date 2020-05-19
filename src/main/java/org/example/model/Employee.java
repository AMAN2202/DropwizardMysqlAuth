package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Employee {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private long salary;

}
