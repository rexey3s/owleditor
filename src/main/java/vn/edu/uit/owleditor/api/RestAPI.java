package vn.edu.uit.owleditor.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.servlet.http.HttpSession;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/23/14.
 */
@RestController
@RequestMapping(value = "/api/")
public class RestAPI {

    
    @RequestMapping(value = "/owl/class", method = RequestMethod.GET)
    public
    @ResponseBody
    String getOWLClassTreeDiagram(HttpSession session) {
        return (new ClassHierarchicalTree((OWLEditorKit) session.getAttribute("OWLEditorKit"))).refreshTree();
    }


}
