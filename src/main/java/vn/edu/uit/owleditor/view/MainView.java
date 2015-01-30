package vn.edu.uit.owleditor.view;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
@VaadinUIScope
@VaadinComponent
public class MainView extends HorizontalLayout {
    public final static String NAME = "mainView";
    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);
    final TabSheet root = new TabSheet();

    final OWLEditorKit editorKit;
    public MainView() {
        editorKit = OWLEditorUI.getEditorKit();
        root.addTab(new ClassesSheet(), "Classes");
        root.addTab(new ObjectPropertiesSheet(), "Object Properties");
        root.addTab(new DataPropertiesSheet(), "Data Properties");
        root.addTab(new IndividualsSheet(), "Individuals");
        root.addTab(new RuleSheet(), "SWRL Rules");
        root.addTab(new DemoSheet(), "Hỗ trợ phân loại");
        DiagramSheet diagramSheet = new DiagramSheet();
        root.addTab(diagramSheet, "Diagram");
        root.addSelectedTabChangeListener(event -> {
            Component selectedTab = event.getTabSheet().getSelectedTab();
            if (selectedTab instanceof DiagramSheet || selectedTab instanceof DemoSheet) {
                if (!editorKit.getReasonerStatus()) {
                    editorKit.setReasonerStatus(true);
                    OWLEditorEventBus.post(new OWLEditorEvent.ReasonerToggleEvent(
                            editorKit.getReasonerStatus(), null));
                }
            }
            if (selectedTab instanceof DiagramSheet) {               
                ((DiagramSheet) event.getTabSheet().getSelectedTab()).reloadAll();
            }

        });
        root.setSizeFull();
        root.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        addComponent(root);
        setSizeFull();
    }


}
