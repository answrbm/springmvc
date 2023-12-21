package ansarbektassov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/hello")
    public String helloPage(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "surname", required = false) String surname,
                            Model model) {
        model.addAttribute("message","Hello, " + name + " " + surname);
        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String goodbyePage() {
        return "first/goodbye";
    }

    @GetMapping("/calculator")
    public String calculator(@RequestParam(value = "a") int a,
                             @RequestParam(value = "b") int b,
                             @RequestParam(value = "action") String action,
                             Model model) {
        String answer;
        switch (action) {
            case "multiplication" -> {
                answer = String.valueOf(a*b);
                action = "*";
            }
            case "division" -> {
                answer = String.valueOf(a/b);
                action = "/";
            }
            case "addition" -> {
                answer = String.valueOf(a+b);
                action = "+";
            }
            case "subtraction" -> {
                answer = String.valueOf(a-b);
                action = "-";
            }
            default -> {
                answer = "Invalid operator";
            }
        };
        model.addAttribute("answer","a"+action+"b="+answer);
        return "first/calculator";
    }
}
