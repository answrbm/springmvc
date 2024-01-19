package ansarbektassov.util;

import ansarbektassov.dao.PersonDAO;
import ansarbektassov.models.Person;
import ansarbektassov.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleService.findByEmail(person.getEmail()).isPresent())
            errors.rejectValue("email","","Person with such email already exists");
    }
}
