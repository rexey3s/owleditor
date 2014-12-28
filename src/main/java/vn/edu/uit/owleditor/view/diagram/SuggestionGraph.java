package vn.edu.uit.owleditor.view.diagram;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/28/14.
 */
@JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js",
        "http://d3js.org/d3.v3.min.js", "lib/dagre-d3.min.js", "lib/suggestionRenderer.js"})
public class SuggestionGraph extends AbstractJavaScriptComponent {
    public void setData(String s) {

        getState().data = s;
    }

    @Override
    protected SuggestionGraphState getState() {
        return (SuggestionGraphState) super.getState();
    }

}
