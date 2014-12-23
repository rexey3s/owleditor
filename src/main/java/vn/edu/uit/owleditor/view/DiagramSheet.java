package vn.edu.uit.owleditor.view;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.demo.JSDiagram;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
public class DiagramSheet extends VerticalLayout {

    public DiagramSheet() {
        OWLEditorKit editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        JSDiagram diagram = new JSDiagram(editorKit.getActiveOntology());
        final HorizontalLayout diagramContainer = new HorizontalLayout();
        addStyleName("diagram-container");
        diagramContainer.addComponent(diagram);
        diagramContainer.setSizeFull();
        addComponent(diagramContainer);
        setSizeFull();
    }
}
