package vn.edu.uit.owleditor.view.component;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.swrlapi.exceptions.InvalidSWRLRuleNameException;
import vn.edu.uit.owleditor.data.list.SWRLAtomShortFormContainer;
import vn.edu.uit.owleditor.utils.EditorUtils;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class SWRLRuleEditor extends VerticalLayout {
    private final AbsoluteLayout root = new AbsoluteLayout();
    private final TextArea input = new TextArea();
    private final ComboBox suggestBox = new ComboBox();
    private final TextField ruleName = new TextField();
    private final TextField ruleComment = new TextField();

    public SWRLRuleEditor() {
        suggestBox.setContainerDataSource(new SWRLAtomShortFormContainer());
        initialise();
    }

    public String getRuleName() throws InvalidSWRLRuleNameException {
        return ruleName.getValue();
    }

    public void setRuleName(String s) {
        ruleName.setValue(s);
    }

    public String getRuleComment() {
        return ruleComment.getValue();
    }

    public void setRuleComment(String s) {
        ruleComment.setValue(s);
    }

    private void initialise() {
        root.addComponents(input);
        root.setSizeFull();
        final VerticalLayout wrapper = new VerticalLayout();
        wrapper.addStyleName(ValoTheme.LAYOUT_WELL);
        input.setSizeFull();
        input.setInputPrompt("SWRL Rule");
        ruleName.addValidator(new AbstractStringValidator("You must enter the rule name") {
            @Override
            protected boolean isValidValue(String s) {
                return !s.isEmpty();
            }
        });

        ruleName.setWidth("100%");
        ruleName.setInputPrompt("Rule name");
        ruleComment.setWidth("100%");
        ruleComment.setInputPrompt("Comment");
        wrapper.addComponents(ruleName, ruleComment, root);
        wrapper.setExpandRatio(ruleName, 1);
        wrapper.setExpandRatio(ruleComment, 1);
        wrapper.setExpandRatio(root, 8);
        wrapper.setHeight("100%");

        input.addShortcutListener(new ShortcutListener("TAB2POPUP", ShortcutAction.KeyCode.TAB, null) {
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
            }
        });
        input.addFocusListener(focusEvent -> root.removeComponent(suggestBox));

        suggestBox.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(suggestBox);
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

        addComponent(wrapper);
        setSizeFull();
    }


    private void handleAppend(String value) {
        input.setValue(input.getValue().substring(0, input.getValue().lastIndexOf(" ") + 1) + value);
    }


    public String getValue() {
        return input.getValue();
    }

    public void setValue(String value) {
        input.setValue(value);
    }

}
