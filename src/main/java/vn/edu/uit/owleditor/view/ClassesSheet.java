package vn.edu.uit.owleditor.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import vn.edu.uit.owleditor.view.panel.ClassExpressionPanelContainer;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/5/14.
 */
@VaadinUIScope
@VaadinView(name = ClassesSheet.NAME)
public class ClassesSheet extends HorizontalLayout implements View {
    public static final String NAME = "Classes";
    private ClassHierarchicalPanel hcLayout;
    private ClassExpressionPanelContainer clLayout;


    public ClassesSheet() {
        initialise();
    }

    private void initialise() {
        hcLayout = new ClassHierarchicalPanel();
        clLayout = new ClassExpressionPanelContainer();
        TabSheet tabs = new TabSheet();
        hcLayout.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                clLayout.setPropertyDataSource(hcLayout.getSelectedProperty());
            }
        });
        tabs.setSizeFull();
        tabs.addStyleName("dashboard-tabsheet");
        tabs.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        tabs.addTab(clLayout, "Class Description");
        tabs.addTab(new Panel(), "Annotation");
        addComponent(hcLayout);
        addComponent(tabs);
        setExpandRatio(hcLayout, 1.0f);
        setExpandRatio(tabs, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
        
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
