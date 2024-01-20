package ansarbektassov.services;

import ansarbektassov.models.Person;
import ansarbektassov.models.Status;
import ansarbektassov.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    @Transactional
    public void save(Person personToCreate) {
        personToCreate.setCreatedAt(LocalDateTime.now());
        personToCreate.setStatus(Status.SINGLE);
        peopleRepository.save(personToCreate);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        foundPerson.ifPresent(peopleRepository::delete);
    }
}
