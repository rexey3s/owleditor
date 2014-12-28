package vn.edu.uit.owleditor.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import vn.edu.uit.owleditor.view.demo.DemoPanel;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
@UIScope
@VaadinView(name = DemoSheet.NAME)
public class DemoSheet extends HorizontalLayout implements View {
    public static final String NAME = "Demo";
    private ClassHierarchicalPanel hierarchicalPanel;
    private DemoPanel demoPanel;
    public DemoSheet() {
        initialise();
    }

    private void initialise() {
        hierarchicalPanel = new ClassHierarchicalPanel();
        demoPanel = new DemoPanel();
        hierarchicalPanel.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                demoPanel.setPropertyDataSource(hierarchicalPanel.getSelectedProperty());
            }
        });

        addComponents(hierarchicalPanel, demoPanel);
        setExpandRatio(hierarchicalPanel, 1.0f);
        setExpandRatio(demoPanel, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        
    }
}
