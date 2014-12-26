package vn.edu.uit.owleditor.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.SWRLAPIRuleSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.view.window.buildAddRuleWindow;
import vn.edu.uit.owleditor.view.window.buildEditRuleWindow;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/9/2014.
 */
public class RuleSheet extends HorizontalLayout implements Action.Handler, Property.ValueChangeListener {

    private static final Action ADD = new Action("Add");
    private static final Action EDIT = new Action("Edit");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD,
            EDIT, REMOVE};


    private final SWRLAPIRuleSource selectedRow = new SWRLAPIRuleSource();

    private final OWLEditorKit editorKit;

    private final SWRLAPIOWLOntology activeOntology;
    private final Container rulesContainer;
    private Table rulesTable;

    public RuleSheet() {
        editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        activeOntology = editorKit.getSWRLActiveOntology();
        rulesContainer = buildRulesContainer();
        OWLEditorUI.getGuavaEventBus().register(this);
        init();
    }

    private void init() {
        rulesTable = new Table();
        rulesTable.setContainerDataSource(rulesContainer);
        rulesTable.setSelectable(true);
        rulesTable.addActionHandler(this);
        rulesTable.addValueChangeListener(this);
        rulesTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
        rulesTable.setSizeFull();
        setMargin(true);
        addStyleName(ValoTheme.LAYOUT_CARD);
        addComponent(rulesTable);
        setSizeFull();
    }

    @SuppressWarnings({"unchecked"})
    private Container buildRulesContainer() {
        final Container container = new IndexedContainer();
        container.addContainerProperty("Rule name", String.class, "");
        container.addContainerProperty("Content", String.class, "");
        container.addContainerProperty("Comment", String.class, "");
        container.addContainerProperty("Active", Boolean.class, null);
        editorKit.getSWRLActiveOntology().getSWRLAPIRules().forEach(rule -> {
            if (!rule.isSQWRLQuery()) {
                container.addItem(rule);
                container.getContainerProperty(rule, "Rule name").setValue(rule.getRuleName());
                container.getContainerProperty(rule, "Content").setValue(editorKit.getRuleRenderer().renderSWRLRule(rule));
                container.getContainerProperty(rule, "Comment").setValue(rule.getComment());
                container.getContainerProperty(rule, "Active").setValue(rule.isActive());
            }
        });
        return container;
    }

    @Override
    public Action[] getActions(Object o, Object o1) {
        return ACTIONS;
    }

    @Override
    public void handleAction(Action action, Object target, Object sender) {
        if (action == ADD) {
            UI.getCurrent().addWindow(new buildAddRuleWindow(editorKit, rule ->
                        editorKit.getDataFactory().getRuleAddEvent(rule, activeOntology.getOWLOntology())
            ));
        } else if (action == EDIT) {
            try {
                UI.getCurrent().addWindow(new buildEditRuleWindow(
                        editorKit,
                        selectedRow,
                        rule -> editorKit.getDataFactory()
                                .getRuleMofifyEvent(
                                        rule, selectedRow.getValue(), activeOntology.getOWLOntology())
                ));
            } catch (NullPointerException ex) {
                Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        } else if (action == REMOVE) {

            ConfirmDialog.show(UI.getCurrent(), dialog -> {
                if (dialog.isConfirmed()) {
                    try {
                        OWLEditorUI.getGuavaEventBus().post(editorKit.getDataFactory().getRuleRemoveEvent(
                                selectedRow.getValue(), activeOntology.getOWLOntology()));
                        dialog.close();
                    } catch (NullPointerException ex) {
                        Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }
                } else {
                        dialog.close();
                }
            });


        }

    }

    @SuppressWarnings({"unchecked"})
    private void handleAddRule(SWRLAPIRule rule) {
        rulesContainer.addItem(rule);
        rulesContainer.getContainerProperty(rule, "Rule name").setValue(rule.getRuleName());
        rulesContainer.getContainerProperty(rule, "Content").setValue(editorKit.getRuleRenderer().renderSWRLRule(rule));
        rulesContainer.getContainerProperty(rule, "Comment").setValue(rule.getComment());
        rulesContainer.getContainerProperty(rule, "Active").setValue(rule.isActive());

    }

    @Subscribe
    public void afterRuleAdded(OWLEditorEvent.RuleAdded event) {

        if (editorKit.getActiveOntology().containsAxiomIgnoreAnnotations(event.getAxiom().getAxiomWithoutAnnotations())) {
            handleAddRule(event.getAxiom());
            Notification.show("Successfully added rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
        } else
            Notification.show("Cannot add rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
    }


    @Subscribe
    public void afterRuleRemoved(OWLEditorEvent.RuleRemoved event) {
        activeOntology.deleteSWRLRule(event.getAxiom().getRuleName());

        if (!editorKit.getActiveOntology().containsAxiomIgnoreAnnotations(event.getAxiom().getAxiomWithoutAnnotations())) {
            rulesContainer.removeItem(event.getAxiom());
            Notification.show("Successfully removed rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
        } else
            Notification.show("Cannot remove rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
    }

    @Subscribe
    public void afterRuleModified(OWLEditorEvent.RuleModified event) {
        activeOntology.deleteSWRLRule(event.getOldAxiom().getRuleName());

        if (!editorKit.getActiveOntology().containsAxiomIgnoreAnnotations(event.getOldAxiom().getAxiomWithoutAnnotations())
                && editorKit.getActiveOntology().containsAxiomIgnoreAnnotations(event.getNewAxiom().getAxiomWithoutAnnotations())) {
            rulesContainer.removeItem(event.getOldAxiom());
            handleAddRule(event.getNewAxiom());
            Notification.show("Successfully modified rule ",
                    Notification.Type.TRAY_NOTIFICATION);
        } else
            Notification.show("Cannot modify rule " + event.getOldAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getProperty().getValue() != null) {
            selectedRow.setValue((SWRLAPIRule) valueChangeEvent.getProperty().getValue());
        }
    }
}
