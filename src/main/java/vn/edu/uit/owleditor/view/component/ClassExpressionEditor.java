package vn.edu.uit.owleditor.view.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.data.list.OWLEntitiesShortFormContainer;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.utils.OWLEditorData;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/27/2014.
 */
public class ClassExpressionEditor extends VerticalLayout {
    private final AbsoluteLayout root = new AbsoluteLayout();
    private final TextArea input = new TextArea();
    private final ComboBox entitiesList = new ComboBox();

    public ClassExpressionEditor() {
        entitiesList.setContainerDataSource(
                new OWLEntitiesShortFormContainer(OWLEditorUI.getEditorKit()));
        initialise();
    }


    private void initialise() {

        entitiesList.setItemIconPropertyId(OWLEditorData.OWLEntityIcon);
        entitiesList.setFilteringMode(FilteringMode.CONTAINS);
        entitiesList.setNullSelectionAllowed(false);
        entitiesList.setImmediate(true);
        input.setSizeFull();
        input.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);


        root.addComponents(input);

        input.addShortcutListener(new ShortcutListener("TAB2POPUP", ShortcutAction.KeyCode.INSERT, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                root.addComponent(entitiesList, EditorUtils.writePixelCoordinator(input.getValue().length()));
                entitiesList.setValue("");
                entitiesList.focus();
            }
        });
//        input.addTextChangeListener(textChangeEvent -> {
//            final String text = textChangeEvent.getText();
//            final String lastWord = text.substring(text.lastIndexOf(" ") + 1);
//            if (lastWord.length() > 1) {
//                root.addComponent(entitiesList, EditorUtils.writePixelCoordinator(textChangeEvent.getCursorPosition()));
//                entitiesList.setValue(lastWord);
//                entitiesList.focus();
//            }
//        });
        input.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(entitiesList);
            }
        });
        input.addFocusListener(focusEvent -> root.removeComponent(entitiesList));

        entitiesList.addShortcutListener(new ShortcutListener("Cancel", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                root.removeComponent(entitiesList);
            }
        });
        entitiesList.addShortcutListener(new ShortcutListener("Add", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                handleAppend((String) entitiesList.getValue());                        
                root.removeComponent(entitiesList);
                input.focus();
            }
        });

        addComponent(root);
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
