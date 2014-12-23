package vn.edu.uit.owleditor.view;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.demo.DemoPanel;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;
import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
public class DemoSheet extends HorizontalLayout implements Property.ValueChangeListener {
    private final OWLEditorKit editorKit;
    private ClassHierarchicalPanel hierarchicalPanel;
    private DemoPanel demoPanel;
    public DemoSheet() {
        editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        initialise();
    }

    private void initialise() {
        hierarchicalPanel = new ClassHierarchicalPanel(editorKit);
        demoPanel = new DemoPanel(editorKit);
        hierarchicalPanel.addValueChangeListener(this);

        addComponents(hierarchicalPanel, demoPanel);
        setExpandRatio(hierarchicalPanel, 1.0f);
        setExpandRatio(demoPanel, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }


    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().getValue() != null) {
            demoPanel.setPropertyDataSource(hierarchicalPanel.getSelectedProperty());
        }
    }
}
