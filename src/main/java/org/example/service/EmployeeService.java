package org.example.service;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.db.EmployeeDAO;
import org.example.model.Employee;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class EmployeeService {
    @Inject
    private  EmployeeDAO employeeDAO;


    public Set<Employee> getAllEmployee() {
        Set<Employee> employeeSet = employeeDAO.getAll();
        return employeeSet;
    }

    public Employee getEmployee(long id) throws NullPointerException {
        Employee employee = employeeDAO.findById(id);
        if (employee.equals(null))
            throw new NullPointerException();
        return employee;
    }

    public void addEmployee(Employee employee) {
        employeeDAO.insert(employee);
    }

    public void updateEmployee(Employee employee) throws NullPointerException {
        Employee emp = employeeDAO.findById(employee.getId());
        if (emp.equals(null))
            throw new NullPointerException();
        employeeDAO.update(employee);
    }

    public void deleteEmployee(long id) throws NullPointerException {
        Employee emp = employeeDAO.findById(id);

        if (emp.equals(null)) {
            throw new NullPointerException();
        }
        employeeDAO.delete(emp);
    }

}
