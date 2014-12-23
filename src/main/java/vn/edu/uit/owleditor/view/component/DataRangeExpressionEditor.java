package vn.edu.uit.owleditor.view.component;

import vn.edu.uit.owleditor.data.list.DataRangeShortFormContainer;
import vn.edu.uit.owleditor.utils.EditorUtils;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/20/14.
 */
public class DataRangeExpressionEditor extends VerticalLayout {
    private final AbsoluteLayout root = new AbsoluteLayout();
    private final TextArea literal = new TextArea();
    private final ComboBox suggest = new ComboBox();

    public DataRangeExpressionEditor() {
        suggest.setContainerDataSource(new DataRangeShortFormContainer());
        initialise();
    }


    private void initialise() {

        suggest.setFilteringMode(FilteringMode.CONTAINS);
        suggest.setNullSelectionAllowed(false);
        suggest.setImmediate(true);
        literal.setSizeFull();
        literal.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);


        root.addComponents(literal);

        literal.addShortcutListener(new ShortcutListener("TAB2POPUP", ShortcutAction.KeyCode.TAB, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                root.addComponent(suggest, EditorUtils.writePixelCoordinator(literal.getValue().length()));
                suggest.setValue("");
                suggest.focus();
            }
        });
        literal.addTextChangeListener(textChangeEvent -> {
            final String text = textChangeEvent.getText();
            final String lastWord = text.substring(text.lastIndexOf(" ") + 1);
            if (lastWord.length() > 1) {
                root.addComponent(suggest, EditorUtils.writePixelCoordinator(textChangeEvent.getCursorPosition()));
                suggest.setValue(lastWord);
                suggest.focus();
            }
        });
        literal.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(suggest);
            }
        });
        literal.addFocusListener(focusEvent -> root.removeComponent(suggest));

        suggest.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(suggest);
            }
        });
        suggest.addShortcutListener(new ShortcutListener("Add", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                handleAppend((String) suggest.getValue());
                root.removeComponent(suggest);
                literal.focus();
            }
        });

        addComponent(root);
        setSizeFull();
    }


    private void handleAppend(String value) {
        literal.setValue(literal.getValue().substring(0, literal.getValue().lastIndexOf(" ") + 1) + value);
    }


    public String getValue() {
        return literal.getValue();
    }

    public void setValue(String value) {
        literal.setValue(value);
    }
}
