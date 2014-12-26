package vn.edu.uit.owleditor.view.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObject;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.data.property.OWLObjectSource;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public abstract class AbstractNonEditableOWLObjectLabel<T extends OWLObject>
        extends HorizontalLayout implements OWLLabel<T> {

    protected final Label label = new Label();
    protected OWLObjectSource<T> expressionSource;
    private OWLExpressionRemoveHandler removeExpression;


    public AbstractNonEditableOWLObjectLabel(@Nonnull OWLObjectSource<T> expressionSource,
                                             OWLExpressionRemoveHandler removeExpression) {
        this.expressionSource = expressionSource;
        this.removeExpression = removeExpression;
        label.setPropertyDataSource(this.expressionSource);
        initialise();
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

        return tools;
    }

    public String getValue() {
        return label.getValue();
    }

    @Override
    public OWLObjectSource<T> getPropertyDataSource() {
        return expressionSource;
    }


}