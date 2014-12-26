package vn.edu.uit.owleditor.view.component;


import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObject;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.OWLObjectSource;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;
import vn.edu.uit.owleditor.event.OWLExpressionUpdateHandler;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/17/2014.
 */
public abstract class AbstractEditableOWLObjectLabel<T extends OWLObject>
        extends HorizontalLayout implements OWLLabel<T> {

    protected final Label label = new Label();
    protected final OWLEditorKit editorKit;
    protected OWLObjectSource<T> expressionSource;
    protected OWLExpressionRemoveHandler removeExpression;
    protected OWLExpressionUpdateHandler<T> modifyExpression;

    public AbstractEditableOWLObjectLabel(@Nonnull OWLEditorKit eKit,
                                          @Nonnull OWLObjectSource<T> expressionSource,
                                          @Nonnull OWLExpressionRemoveHandler removeExpression,
                                          @Nonnull OWLExpressionUpdateHandler<T> modifyExpression) {
        this.editorKit = eKit;
        this.expressionSource = expressionSource;
        this.removeExpression = removeExpression;
        this.modifyExpression = modifyExpression;
        initialise();
        label.setPropertyDataSource(this.expressionSource);
    }


    private void initialise() {
        label.addStyleName(ValoTheme.LABEL_COLORED);
        label.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        label.setSizeUndefined();

        Panel lbPanel = new Panel();
        lbPanel.setWidth("100%");
        lbPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        lbPanel.addStyleName("expression-label");
        lbPanel.setContent(label);
        addComponents(lbPanel, buildToolBar());
        setExpandRatio(lbPanel, 1);
        setComponentAlignment(lbPanel, Alignment.MIDDLE_LEFT);
        setWidth(100.0f, Unit.PERCENTAGE);
    }

    private Component buildToolBar() {
        final MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        tools.addStyleName("icon-only");
        tools.addItem("", FontAwesome.TIMES, click ->
                        ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", msgBox -> {
                            if (msgBox.isConfirmed()) {
                                if (removeExpression != null)
                                    OWLEditorUI.getGuavaEventBus().post(removeExpression
                                            .removingExpression());

                            } else {
                                msgBox.close();
                            }
                        })
        );

        tools.addItem("", FontAwesome.COG, click -> initModifiedAction());

        return tools;
    }

    public String getValue() {
        return label.getValue();
    }

    @Override
    public OWLObjectSource<T> getPropertyDataSource() {
        return expressionSource;
    }


    public abstract void initModifiedAction();

}
