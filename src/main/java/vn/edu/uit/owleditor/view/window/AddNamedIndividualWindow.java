package vn.edu.uit.owleditor.view.window;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.list.OWLNamedIndividualContainer;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;

/**
 * Created by Chuong Dang on 18/11/2014.
 */
public class AddNamedIndividualWindow extends Window {

    private final ComboBox individualsBox = new ComboBox("Individuals");
    private OWLExpressionAddHandler<OWLNamedIndividual> addExpression;

    public AddNamedIndividualWindow(OWLExpressionAddHandler<OWLNamedIndividual> addExpression) {
        this.addExpression = addExpression;
        OWLEditorKit eKit = (OWLEditorKit)
                UI.getCurrent().getSession().getAttribute("kit");

        OWLNamedIndividualContainer container = eKit.getDataFactory().getOWLIndividualListContainer();

        individualsBox.setContainerDataSource(container);

        initialize();

    }

    private void initialize() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(300.0f, Unit.PIXELS);
        setContent(buildContent());
        center();
    }

    private Component buildContent() {
        final VerticalLayout result = new VerticalLayout();
        result.setMargin(true);
        result.setSpacing(true);
        final FormLayout form = new FormLayout();
        individualsBox.focus();
        individualsBox.setNullSelectionAllowed(false);
        individualsBox.setNewItemsAllowed(false);
        individualsBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        individualsBox.setItemCaptionPropertyId(OWLEditorData.OWLNamedIndividualName);
        form.addComponent(individualsBox);
        result.addComponent(form);
        result.addComponent(buildFooter());

        return result;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addClickListener(event -> {
            OWLNamedIndividual newIndividual = (OWLNamedIndividual) individualsBox.getValue();
            if (addExpression != null)
                OWLEditorEventBus.post(addExpression.addingExpression(newIndividual));
            close();
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;
    }




}
