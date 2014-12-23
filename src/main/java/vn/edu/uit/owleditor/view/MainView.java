package vn.edu.uit.owleditor.view;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.VaadinComponent;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */

@VaadinComponent
public class MainView extends HorizontalLayout {

    final TabSheet root = new TabSheet();

    public MainView() {

        root.addTab(new ClassesSheet(), "Classes");
        root.addTab(new ObjectPropertiesSheet(), "Object Properties");
        root.addTab(new DataPropertiesSheet(), "Data Properties");
        root.addTab(new IndividualsSheet(), "Individuals");
        root.addTab(new RuleSheet(), "SWRL Rules");
        root.addTab(new DemoSheet(), "Demo 1");
        root.addTab(new DiagramSheet(), "Diagram");
        root.setSizeFull();
        root.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        addComponent(root);
        setSizeFull();
    }
}
