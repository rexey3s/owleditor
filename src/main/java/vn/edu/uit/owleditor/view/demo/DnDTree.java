package vn.edu.uit.owleditor.view.demo;


import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/21/14.
 */
@JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js", "http://d3js.org/d3.v3.min.js", "lib/dndTree.js"})
public class DnDTree extends AbstractJavaScriptComponent {

    public String getData() {
        return getState().data;
    }


    public void setData(String data) {
        getState().data = data;
    }

    @Override
    protected DnDTreeState getState() {
        return (DnDTreeState) super.getState();
    }

}
