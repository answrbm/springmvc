package ansarbektassov.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy") //dd/mm/yyyy
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || !getClass().equals(o.getClass())) return false;

        Person person = (Person) o;
        if(id != person.getId()) return false;
        if(!name.equals(person.getName())) return false;
        if(age != person.getAge()) return false;
        if(!email.equals(person.getEmail())) return false;
        if(!address.equals(person.getAddress())) return false;
        if(!dateOfBirth.equals(person.getDateOfBirth())) return false;
        if(!createdAt.equals(person.createdAt)) return false;
        return status == person.getStatus();
    }

    @Override
    public int hashCode() {
        int result = id;
        result = result * 31 + (name == null ? 0 : name.hashCode());
        result = result * 31 + age;
        result = result * 31 + (email == null ? 0 : email.hashCode());
        result = result * 31 + (address == null ? 0 : address.hashCode());
        result = result * 31 + (dateOfBirth == null ? 0 : dateOfBirth.hashCode());
        result = result * 31 + (createdAt == null ? 0 : createdAt.hashCode());
        result = result * 31 + (status == null ? 0 : status.hashCode());
        return result;
    }
}
