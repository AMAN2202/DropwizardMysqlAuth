package org.example.empApi.service;

import org.example.empApi.db.EmployeeDAO;
import org.example.empApi.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeService {

    @Test
    public void getAllEmployee() {

        EmployeeDAO employeeDAO = mock(EmployeeDAO.class);

        HashSet<Employee> s = new HashSet<>();
        s.add(Employee.builder().name("aman").id(1).salary(100).build());
        s.add(Employee.builder().name("lol").id(1).salary(100).build());

        when(employeeDAO.getAll()).thenReturn(s);
        assertEquals(s, employeeDAO.getAll());
    }

    @Test
    public void getById() {

        EmployeeDAO employeeDAO = mock(EmployeeDAO.class);

        Employee e = Employee.builder().name("aman").id(1).salary(100).build();
        when(employeeDAO.findById(1L)).thenReturn(e);
        assertEquals(employeeDAO.findById(1L), e);
    }

}
