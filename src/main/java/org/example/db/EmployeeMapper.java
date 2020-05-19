package org.example.db;

import org.example.model.Employee;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements ResultSetMapper<Employee> {
    @Override
    public Employee map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return  Employee.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .salary(resultSet.getLong("salary"))
                .build();
    }
}
