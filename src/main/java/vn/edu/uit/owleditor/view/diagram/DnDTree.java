package vn.edu.uit.owleditor.view.diagram;


import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/21/14.
 */
@JavaScript({"lib/jquery.min.js", "lib/d3.v3.min.js", "lib/dndTree.js"})
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
