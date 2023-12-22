package ansarbektassov.controllers;

import ansarbektassov.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonDAO personDAO;

    @GetMapping
    public String index(Model model) {
        //Получим всех людей из DAO и передадим на отображение на представление
        model.addAttribute("people",personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //Получим одного человека из DAO и передадим на отображение на представление
        model.addAttribute("person",personDAO.show(id));
        return "people/show";
    }

}
