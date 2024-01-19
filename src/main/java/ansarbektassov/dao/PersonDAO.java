package ansarbektassov.dao;

import ansarbektassov.models.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Person p", Person.class)
                .getResultList();
    }

    @Transactional
    public Person show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class,id);
    }

    @Transactional
    public Optional<Person> show(String email) {
        return index().stream().filter(person -> person.getEmail().equals(email)).findAny();
    }

    @Transactional
    public void save(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person personToUpdate = session.get(Person.class,id);
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setAge(updatedPerson.getAge());
        personToUpdate.setEmail(updatedPerson.getEmail());
        personToUpdate.setAddress(updatedPerson.getAddress());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person personToRemove = session.get(Person.class,id);
        session.remove(personToRemove);
    }

    //Тестируем производительность пакетной вставки
    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO person(name,age,email,address) VALUES(?,?,?,?);",
                    person.getName(),person.getAge(),person.getEmail(),person.getAddress());
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
            people.add(new Person(
                    "Person"+(i+1),
                    10+i,
                    "person"+(i+1)+"@mail.com","address"+(i+1)));

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
