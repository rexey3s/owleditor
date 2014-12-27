package vn.edu.uit.owleditor.view;

import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.panel.ClassExpressionPanelContainer;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/5/14.
 */
public class ClassesSheet extends HorizontalLayout implements Property.ValueChangeListener {

    private final OWLEditorKit editorKit;
    private ClassHierarchicalPanel hcLayout;
    private ClassExpressionPanelContainer clLayout;


    public ClassesSheet() {
        editorKit = OWLEditorUI.getEditorKit();
        initialise();

    }

    private void initialise() {
        hcLayout = new ClassHierarchicalPanel(editorKit);
        clLayout = new ClassExpressionPanelContainer(editorKit);
        TabSheet tabs = new TabSheet();
        hcLayout.addValueChangeListener(this);
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
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().getValue() != null) {
            clLayout.setPropertyDataSource(hcLayout.getSelectedProperty());
        }
    }
}
