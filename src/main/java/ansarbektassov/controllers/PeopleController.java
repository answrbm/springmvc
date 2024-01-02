package ansarbektassov.controllers;

import ansarbektassov.dao.PersonDAO;
import ansarbektassov.models.Person;
import ansarbektassov.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonDAO personDAO;
    @Autowired
    private PersonValidator validator;

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
    public String create(@ModelAttribute("person") @Validated Person person, BindingResult bindingResult) {
        validator.validate(person,bindingResult);
        if(bindingResult.hasErrors()) {
            return "people/new";
        }
        personDAO.save(person);
        return "redirect: /springmvc/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person",personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Validated Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        validator.validate(person,bindingResult);
        if(bindingResult.hasErrors()) {
            return "people/edit";
        }
        personDAO.update(id,person);
        return "redirect: /springmvc/people/"+id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect: /springmvc/people";
    }

}
