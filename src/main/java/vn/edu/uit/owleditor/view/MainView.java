package vn.edu.uit.owleditor.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;
import vn.edu.uit.owleditor.core.OWLEditorKit;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
@UIScope
@VaadinView(name = MainView.NAME)
public class MainView extends HorizontalLayout implements View {
    public final static String NAME = "mainView";
    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);
    final TabSheet root = new TabSheet();
    @Autowired
    OWLEditorKit editorKit;

    public MainView() {
        LOG.info(editorKit.getActiveOntology().toString());
        root.addTab(new ClassesSheet(), "Classes");
        root.addTab(new ObjectPropertiesSheet(), "Object Properties");
        root.addTab(new DataPropertiesSheet(), "Data Properties");
        root.addTab(new IndividualsSheet(), "Individuals");
        root.addTab(new RuleSheet(), "SWRL Rules");
        root.addTab(new DemoSheet(), "Demo 1");
        DiagramSheet diagramSheet = new DiagramSheet();
        root.addTab(diagramSheet, "Diagram");
        root.addSelectedTabChangeListener(event -> {
            if (event.getTabSheet().getSelectedTab() instanceof DiagramSheet) {
                ((DiagramSheet) event.getTabSheet().getSelectedTab()).reload();
            }
        });
        root.setSizeFull();
        root.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        addComponent(root);
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Notification.show("Welcome to Web ontology editor");
    }
}
