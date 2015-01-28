package vn.edu.uit.owleditor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/28/15.
 */
@Controller
@RequestMapping(value = "/")
public class DefaultIndex {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/UI";
    }
}
