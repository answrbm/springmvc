package ansarbektassov.controllers;

import ansarbektassov.dao.PersonDAO;
import ansarbektassov.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonDAO personDAO;

    @GetMapping
    public String index(Model model) {
        //������� ���� ����� �� DAO � ��������� �� ����������� �� �������������
        model.addAttribute("people",personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //������� ������ �������� �� DAO � ��������� �� ����������� �� �������������
        model.addAttribute("person",personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person);
        return "redirect: /springmvc/people/"+person.getId();
    }

}
