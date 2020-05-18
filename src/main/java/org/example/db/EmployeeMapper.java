package org.example.db;

import org.example.model.Employee;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements ResultSetMapper<Employee> {
    @Override
    public Employee map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Employee(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("salary")
        );
    }
}
