package org.example.empApi.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private long id;

    private String name;

    private long salary;

}
