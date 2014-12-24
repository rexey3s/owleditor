package vn.edu.uit.owleditor.view.demo;


import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import org.json.JSONObject;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/21/14.
 */
@JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js", "http://d3js.org/d3.v3.min.js", "hierarchy.js", "hierarchy.json"})
public class JSDiagram extends AbstractJavaScriptComponent {

    public String getValue() {
        return getState().value;
    }

    public void setValue(String value) {
        getState().value = value;

    }

    public void setData(JSONObject data) {
        getState().data = data;
    }


    @Override
    protected JSDiagramState getState() {
        return (JSDiagramState) super.getState();
    }
}
