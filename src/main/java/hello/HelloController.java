package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yasin
 */
@Controller
public class HelloController {

    @GetMapping("/greetingHtml")
    public String greetingHtml(@RequestParam(name = "name", required = true, defaultValue = "World") String name,
                               Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
