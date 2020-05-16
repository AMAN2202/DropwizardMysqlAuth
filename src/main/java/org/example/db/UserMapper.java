package org.example.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {

    @Override
    public User map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        return new User(resultSet.getString("email"),
                resultSet.getString("password"));
    }
}
