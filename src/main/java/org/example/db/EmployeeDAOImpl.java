package org.example.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.DropwizardSkolException;
import org.example.model.Employee;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDAOImpl extends AbstractDAO<Employee> implements EmployeeDAO {
    @Inject
    public EmployeeDAOImpl(SessionFactory factory) {
        super(factory);
    }

    public Employee findById(Long id) {
        return (Employee) currentSession().get(Employee.class, id);
    }

    public Set<Employee> getAll() {
        return new HashSet<Employee>(currentSession().createCriteria(Employee.class).list());
    }

    public void delete(Employee employee) {
        currentSession().delete(employee);
    }

    public void update(Employee employee) throws DropwizardSkolException {
        try {
            Employee emp = findById(employee.getId());
        }
        catch (Exception ex)
        {
            throw  new DropwizardSkolException(404,"Employee not found");
        }
        finally {
            currentSession().evict(employee);
        }
    }

    public Employee insert(Employee employee) {
        return persist(employee);
    }


}
