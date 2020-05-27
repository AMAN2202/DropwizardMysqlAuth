package org.example.empApi.db;


import org.example.empApi.exception.DropwizardSkolException;
import org.example.empApi.model.Employee;

import java.util.Set;

public interface EmployeeDAO {

    Employee findById(Long id);

    Set<Employee> getAll();

    void delete(Employee employee);

    void update(Employee employee) throws DropwizardSkolException;

    Employee insert(Employee employee);


}
