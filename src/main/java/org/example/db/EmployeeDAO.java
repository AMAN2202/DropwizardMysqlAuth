package org.example.db;

import org.example.model.Employee;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Set;

public interface EmployeeDAO {

    @SqlUpdate("insert into employee (name,salary) values (:name, :salary)")
    void addEmployee(@Bind("name") String name, @Bind("salary") long salary);

    @SqlQuery("select id,name,salary  from employee where id = :id")
    @Mapper(EmployeeMapper.class)
    Employee getEmployee(@Bind("id") long id);

    @SqlQuery("select  id,name,salary  from employee")
    @Mapper(EmployeeMapper.class)
    Set<Employee> getAllEmployee();


    @SqlUpdate("update employee set name=:name, salary=:salary where id=:id")
    void updateEmployee(@Bind("name") String name, @Bind("salary") long salary, @Bind("id") long id);

    @SqlUpdate("delete from employee  where id=:id")
    void deleteEmployee(@Bind("id") long id);


}
