package vn.edu.uit.owleditor.view.window;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.data.list.OWLNamedIndividualContainer;
import vn.edu.uit.owleditor.view.IndividualsSheet;
import vn.edu.uit.owleditor.view.panel.ObjectPropertyHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public abstract class ObjectPropertyAssertionEditorWindow extends Window {

    protected final ObjectPropertyHierarchicalPanel hierarchy;
    protected final IndividualsSheet.IndividualList owlIndividualList;
    private final HorizontalLayout main = new HorizontalLayout();
    private final VerticalLayout root = new VerticalLayout();

    public ObjectPropertyAssertionEditorWindow() {
        hierarchy = new ObjectPropertyHierarchicalPanel();
        owlIndividualList = new IndividualsSheet.IndividualList();
        owlIndividualList.setContainerDataSource(new OWLNamedIndividualContainer(OWLEditorUI.getEditorKit().getActiveOntology()));
        initialise();
    }

    private void initialise() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(600.0f, Unit.PIXELS);
        setHeight(600.0f, Unit.PIXELS);
        setCaption("Object Property Assertions Editor");
        main.addComponents(hierarchy, owlIndividualList);
        main.addStyleName(ValoTheme.LAYOUT_WELL);
        main.setSizeFull();
        root.setHeight(100.0f, Unit.PERCENTAGE);
        root.addStyleName(ValoTheme.SPLITPANEL_LARGE);
        root.addComponent(main);
        root.addComponent(buildFooter());
        root.setExpandRatio(main, 1.0f);
        root.setSizeFull();
        setContent(root);
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);

        save.addClickListener(initSaveListener());

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }

    abstract Button.ClickListener initSaveListener();


}
