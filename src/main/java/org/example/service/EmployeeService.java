package org.example.service;

import org.example.db.EmployeeDAO;
import org.example.model.Employee;

import java.util.Set;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }


    public Set<Employee> getAllEmployee() {
        Set<Employee> employeeSet = employeeDAO.getAllEmployee();
        return employeeSet;
    }

    public Employee getEmployee(long id) throws NullPointerException {
        Employee employee = employeeDAO.getEmployee(id);
        if (employee.equals(null))
            throw new NullPointerException();
        return employee;
    }

    public void addEmployee(Employee employee) {
        employeeDAO.addEmployee(employee.getName(), employee.getSalary());
    }

    public void updateEmployee(Employee employee) throws NullPointerException {
        Employee emp;
        emp = employeeDAO.getEmployee(employee.getId());
        if (emp.equals(null)) {
            throw new NullPointerException();
        }
        employeeDAO.updateEmployee(employee.getName(), employee.getSalary(), employee.getId());
    }

    public void deleteEmployee(long id) throws NullPointerException {
        Employee emp = employeeDAO.getEmployee(id);

        if (emp.equals(null)) {
            throw new NullPointerException();
        }
        employeeDAO.deleteEmployee(id);
    }

}
