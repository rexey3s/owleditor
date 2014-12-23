package vn.edu.uit.owleditor.view.window;


import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.event.OWLExpressionUpdateHandler;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/25/2014.
 */
public abstract class AbstractOWLExpressionEditorWindow<A extends OWLObject> extends Window {

    protected final OWLEditorKit editorKit;
    private final VerticalLayout root = new VerticalLayout();
    private final TabSheet tabs = new TabSheet();
    protected Property<A> currentExpression;
    protected OWLExpressionAddHandler<A> addExpression;
    protected OWLExpressionUpdateHandler<A> modifyExpression;

    public AbstractOWLExpressionEditorWindow(@Nonnull OWLEditorKit eKit) {
        this.editorKit = eKit;
        initialize();
    }
    /**
     * Add mode
     * @param eKit
     * @param addExpression
     */
    public AbstractOWLExpressionEditorWindow(@Nonnull OWLEditorKit eKit,
                                             @Nonnull OWLExpressionAddHandler<A> addExpression) {
        this.editorKit = eKit;
        this.addExpression = addExpression;
        initialize();
    }

    /**
     * @param eKit
     * @param currentExpression
     */
    public AbstractOWLExpressionEditorWindow(@Nonnull OWLEditorKit eKit,
                                             @Nonnull Property<A> currentExpression) {
        this.editorKit = eKit;
        this.currentExpression = currentExpression;
        initialize();
    }

    /**
     * Edit mode
     * @param eKit
     * @param currentExpression
     * @param modifyExpression
     */
    public AbstractOWLExpressionEditorWindow(@Nonnull OWLEditorKit eKit,
                                             @Nonnull Property<A> currentExpression,
                                             @Nonnull OWLExpressionUpdateHandler<A> modifyExpression) {
        this.editorKit = eKit;
        this.currentExpression = currentExpression;
        this.modifyExpression = modifyExpression;
        initialize();
    }


    private void initialize() {
        setCaption("Class Expression Edit Tools");
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(500.0f, Unit.PIXELS);
        setHeight(500.0f, Unit.PIXELS);
        setContent(buildContent());

    }


    private Component buildContent() {

        tabs.setHeight(100.0f, Unit.PERCENTAGE);
        root.addStyleName(ValoTheme.LAYOUT_CARD);
        root.addComponent(tabs);
        root.addComponent(buildFooter());
        root.setExpandRatio(tabs, 1.0f);
        root.setSizeFull();
        return root;
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

    protected abstract Button.ClickListener initSaveListener();

    public void addMoreTab(Component tab, String caption) {
        tabs.addTab(tab, caption);
    }

    public Component getSelectedTab() {
        return tabs.getSelectedTab();
    }

    public void addTabStyle(String... styles) {
        for (String s : styles) {
            tabs.addStyleName(s);
        }
    }

}
