package org.example.db;


import io.dropwizard.hibernate.AbstractDAO;
import org.example.DropwizardSkolException;
import org.example.model.Employee;
import org.hibernate.SessionFactory;

import java.util.HashSet;
import java.util.Set;

public interface EmployeeDAO  {

     Employee findById(Long id);

     Set<Employee> getAll();

     void delete(Employee employee);

     void update(Employee employee) throws DropwizardSkolException;

     Employee insert(Employee employee);


}
