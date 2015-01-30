package vn.edu.uit.owleditor.view.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import vn.edu.uit.owleditor.data.list.SWRLAtomShortFormContainer;
import vn.edu.uit.owleditor.utils.EditorUtils;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/30/15.
 */
public class DLSafeRuleEditor extends VerticalLayout {
    private final AbsoluteLayout root = new AbsoluteLayout();
    private final TextArea input = new TextArea();
    private final ComboBox suggestBox = new ComboBox();

    public DLSafeRuleEditor() {
        suggestBox.setContainerDataSource(new SWRLAtomShortFormContainer());
        initialise();
    }

    private void initialise() {

        root.addComponents(input);
        root.setSizeFull();
        input.setSizeFull();
        input.setInputPrompt("SWRL Rule");

        input.addShortcutListener(new ShortcutListener("TAB2POPUP", ShortcutAction.KeyCode.INSERT, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (target instanceof TextArea) {
                    TextArea textArea = (TextArea) target;
                    final String text = textArea.getValue();
                    final String lastWord = text.substring(text.lastIndexOf(" ") + 1);

                    root.addComponent(suggestBox, EditorUtils.writePixelCoordinator(text.length()));
                    suggestBox.setValue(lastWord);
                    suggestBox.focus();
                    suggestBox.focus();
                }
            }
        });
//        input.addTextChangeListener(textChangeEvent -> {
//            final String text = textChangeEvent.getText();
//            final String lastWord = text.substring(text.lastIndexOf(" ") + 1);
//            if (lastWord.length() > 1) {
//                root.addComponent(suggestBox, EditorUtils.writePixelCoordinator(text.length()));
//                suggestBox.setValue(lastWord);
//                suggestBox.focus();
//            }
//        });
        input.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(suggestBox);
                input.focus();
            }
        });
        input.addFocusListener(focusEvent -> root.removeComponent(suggestBox));

        suggestBox.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(suggestBox);
                input.focus();
            }
        });
        suggestBox.addShortcutListener(new ShortcutListener("Add", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                handleAppend((String) suggestBox.getValue());
                root.removeComponent(suggestBox);

                input.focus();
            }
        });

        addComponent(root);
        setSizeFull();
    }

    private void handleAppend(String value) {
        input.setValue(input.getValue().substring(0, input.getValue().lastIndexOf(" ") + 1) + value);
    }
}
