package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;


public class UserDaoJDBCImpl {

    public static final String createUserTable = "CREATE TABLE IF NOT EXISTS Users" +
            "(id INT AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(40) NOT NULL, " +
            "lastName VARCHAR(40) NOT NULL, " +
            "age INT NOT NULL)";

    public static final String dropUserTable = "DROP TABLE IF EXISTS Users";
    public static final String saveUserTable = "INSERT INTO Users(name, lastName, age) VALUES (?,?,?)";
    public static final String removeUserByIdTable = "DELETE FROM Users WHERE id = ?";
    public static final String getAllUsersTable = "SELECT id, name, lastName, age FROM Users";
    public static final String cleanUsersTableTable = "TRUNCATE TABLE Users";



    public UserDaoJDBCImpl() {
    }


    public void createUsersTable() {

    }


    public void dropUsersTable() {

    }


    public void saveUser(String name, String lastName, byte age) {

    }



    public void removeUserById(long id) {

    }


    public List<User> getAllUsers() {

        return null;
    }


    public void cleanUsersTable() {

    }
}