package vn.edu.uit.owleditor.view;

import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import vn.edu.uit.owleditor.view.demo.DemoPanel;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
public class DemoSheet extends HorizontalLayout implements Property.ValueChangeListener {
    private ClassHierarchicalPanel hierarchicalPanel;
    private DemoPanel demoPanel;
    public DemoSheet() {
        initialise();
    }

    private void initialise() {
        hierarchicalPanel = new ClassHierarchicalPanel();
        demoPanel = new DemoPanel();
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
