package ansarbektassov.dao;

import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person;",new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?",
                        new BeanPropertyRowMapper<>(Person.class), new Object[]{id})
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name,age,email) VALUES(?,?,?);",
                person.getName(),person.getAge(),person.getEmail());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?,age=?,email=? WHERE id=?;",
                updatedPerson.getName(),updatedPerson.getAge(),updatedPerson.getEmail(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?;",id);
    }

    //Тестируем производительность пакетной вставки
    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO person VALUES(?,?,?,?);",
                    person.getId(),person.getName(),person.getAge(),person.getEmail());
        }

        long after = System.currentTimeMillis();

        long with = after-before;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/ansarbektassov/Documents/AlishevSpring/springmvc/src/main/resources/time.txt",true));
            bufferedWriter.write("Without batch update:" + with);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Without batch update time:" + (after - before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for(int i = 0; i < 1000; i++)
            people.add(new Person(i+1,
                    "Person"+(i+1),
                    10+i,
                    "person"+(i+1)+"@mail.com"));

        return people;
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();
        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Person person = people.get(i);
                ps.setInt(1,person.getId());
                ps.setString(2,person.getName());
                ps.setInt(3,person.getAge());
                ps.setString(4,person.getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();

        long with = after-before;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/ansarbektassov/Documents/AlishevSpring/springmvc/src/main/resources/time.txt",true));
            bufferedWriter.write("With batch update:" + with);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Batch update time: " + (after-before));
    }
}
