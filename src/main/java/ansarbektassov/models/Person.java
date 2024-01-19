package ansarbektassov.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotEmpty(message = "Name must be filled")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Column(name = "name")
    private String name;
    @Min(value = 10, message = "Age must be equal or greater than 10")
    @Column(name = "age")
    private int age;
    @NotEmpty(message = "Email must be filled")
    @Email(message = "Email must be valid")
    @Column(name = "email")
    private String email;

    //Country, City, Zip(6 digits)
    //USA, Los Angeles, 123456
    @Pattern(regexp = "[A-Z]\\w+., [A-Z]\\w+.*, \\d{6}",
            message = "Address must match the pattern: 'Country, City, Zip(6 digits)'")
    @Column(name = "address")
    private String address;

    public Person() {
    }

    public Person(String name, int age, String email, String address) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
