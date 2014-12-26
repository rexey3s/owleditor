package vn.edu.uit.owleditor.view;

import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.panel.DataPropertyExpressionPanelContainer;
import vn.edu.uit.owleditor.view.panel.DataPropertyHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/22/2014.
 */
public class DataPropertiesSheet extends HorizontalLayout implements Property.ValueChangeListener {

    private final OWLEditorKit editorKit;
    private DataPropertyHierarchicalPanel hcLayout;
    private DataPropertyExpressionPanelContainer panels;


    public DataPropertiesSheet() {
        editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        initialise();
    }

    private void initialise() {
        hcLayout = new DataPropertyHierarchicalPanel(editorKit);
        panels = new DataPropertyExpressionPanelContainer(editorKit);
        TabSheet tabs = new TabSheet();
        hcLayout.addValueChangeListener(this);
        tabs.setSizeFull();
        tabs.addStyleName("dashboard-tabsheet");
        tabs.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        tabs.addTab(panels, "Data Property Description");
        tabs.addTab(new Panel(), "Annotation");
//        tabs.addStyleName(ValoTheme.LAYOUT_CARD);
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
            panels.setPropertyDataSource(hcLayout.getSelectedProperty());
        }
    }
}
