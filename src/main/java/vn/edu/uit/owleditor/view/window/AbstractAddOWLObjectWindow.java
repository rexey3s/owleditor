package vn.edu.uit.owleditor.view.window;


import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 18/11/2014.
 */
public abstract class AbstractAddOWLObjectWindow<T extends OWLLogicalEntity> extends Window {


    protected final TextField nameField = new TextField();
    protected OWLEntityAddHandler<T> adder;
    private OWLEntityActionHandler handler;
    private Boolean isSub;

    public AbstractAddOWLObjectWindow() {
        initialize();
    }

    public AbstractAddOWLObjectWindow(@Nonnull OWLEntityActionHandler handler,
                                      @Nonnull OWLEntityAddHandler<T> adder,
                                      @Nonnull Boolean isSub) {
        this.handler = handler;
        this.adder = adder;
        this.isSub = isSub;
        initialize();
    }

    private void initialize() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(300.0f, Unit.PIXELS);
        setHeight(00.0f, Unit.PIXELS);
        setContent(buildContent());
    }

    private Component buildContent() {
        final VerticalLayout result = new VerticalLayout();
        result.setMargin(true);
        result.setSpacing(true);
        FormLayout form = new FormLayout();
        nameField.focus();
        nameField.setValue("");
        form.addComponent(nameField);
        result.addComponent(form);
        result.addComponent(buildFooter());

        return result;
    }

    private Component buildFooter() {
        final HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.addClickListener(getSaveListener());
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;

    }

    protected Button.ClickListener getSaveListener() {
        return clicked -> {
            try {
                nameField.validate();
                if (isSub)
                    handler.afterAddSubSaved(adder.addingEntity((T) nameField.getConvertedValue()));
                else
                    handler.afterAddSiblingSaved(adder.addingEntity((T) nameField.getConvertedValue()));
                close();
            } catch (Validator.InvalidValueException ex) {
                Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        };
    }


}
