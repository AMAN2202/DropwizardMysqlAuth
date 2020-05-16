package org.example.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;
import java.util.Set;

public interface UserDAO {
    @SqlUpdate("create table user (email varchar(20) primary key, password varchar(100))")
    void createUserTable();

    @SqlUpdate("create table roles (email varchar(20) primary key, roles varchar(100))")
    void createRolesTable();

    @SqlUpdate("insert into user (email, name) values (:email, :password)")
    void addUser(@Bind("email") String email, @Bind("password") String password);

    @SqlQuery("select email , password from user where email = :email")
    @Mapper(UserMapper.class)
    User getUser(@Bind("email") String email);

    @SqlQuery("select role from roles where email= :email ")
    Set<String> getRoles(@Bind("email") String email);

    @SqlUpdate("insert into roles (email, role) values (:email, :role)")
    void addRole(@Bind("email") String email, @Bind("role") String role);


}
