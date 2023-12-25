package ansarbektassov.dao;

import ansarbektassov.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private static int peopleCount;
    private static final String URL = "jdbc:postgresql://localhost:5432/ansarbektassov";
    private static final String USERNAME = "ansarbektassov";
    private static final String PASSWORD = "1";

    private static final Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM person;";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public Person show(int id) {
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE id = ?");
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            person = new Person();

            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public int save(Person person) {
        peopleCount++;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO person VALUES(?,?,?,?);");
            statement.setInt(1,peopleCount);
            statement.setString(2,person.getName());
            statement.setInt(3,person.getAge());
            statement.setString(4,person.getEmail());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peopleCount;
    }

    public void update(int id, Person updatedPerson) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE person SET name = ?, age = ?, email = ? WHERE id = ?;");
            preparedStatement.setString(1,updatedPerson.getName());
            preparedStatement.setInt(2,updatedPerson.getAge());
            preparedStatement.setString(3,updatedPerson.getEmail());
            preparedStatement.setInt(4,id);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("DELETE FROM person WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
