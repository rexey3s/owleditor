package vn.edu.uit.owleditor.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.exceptions.SWRLRuleException;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.SWRLAPIRuleSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.view.window.buildAddRuleWindow;
import vn.edu.uit.owleditor.view.window.buildEditRuleWindow;

import java.util.Optional;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/9/2014.
 */
@UIScope
@SpringView(name = RuleSheet.NAME)
public class RuleSheet extends VerticalLayout implements Action.Handler, View {
    public static final String NAME = "Rules";
    private static final Logger LOG = LoggerFactory.getLogger(RuleSheet.class);
    private static final Action ADD = new Action("Add");
    private static final Action EDIT = new Action("Edit");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD, REMOVE, EDIT};


    private final SWRLAPIRuleSource selectedRow = new SWRLAPIRuleSource();

    private final OWLEditorKit editorKit;

    private final SWRLAPIOWLOntology activeOntology;
    private final Container rulesContainer;

    public RuleSheet() {
        editorKit = OWLEditorUI.getEditorKit();
        activeOntology = editorKit.getSWRLActiveOntology();
        rulesContainer = buildRulesContainer();
        OWLEditorEventBus.register(this);
        init();
    }

    private void init() {
        Button addRule = new Button("Add rule");
        addRule.setIcon(FontAwesome.PLUS_CIRCLE);
        addRule.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addRule.addClickListener(click -> UI.getCurrent().addWindow(new buildAddRuleWindow(rule ->
                editorKit.getDataFactory().getRuleAddEvent(rule, activeOntology.getOWLOntology())
        )));
        Table rulesTable = new Table();
        rulesTable.setContainerDataSource(rulesContainer);
        rulesTable.setSelectable(true);
        rulesTable.addActionHandler(this);
        rulesTable.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                selectedRow.setValue((SWRLAPIRule) event.getProperty().getValue());
            }
        });
        rulesTable.setSizeFull();
        setMargin(true);
        addStyleName(ValoTheme.LAYOUT_CARD);
        addComponent(addRule);
        addComponent(rulesTable);
        setExpandRatio(rulesTable, 1);
        setSizeFull();
    }

    @SuppressWarnings({"unchecked"})
    private Container buildRulesContainer() {
        final Container container = new IndexedContainer();
        container.addContainerProperty("Rule name", String.class, "");
        container.addContainerProperty("Content", String.class, "");
        container.addContainerProperty("Comment", String.class, "");
        container.addContainerProperty("Active", Boolean.class, null);
        editorKit.getSWRLActiveOntology().getSWRLRules().forEach(rule -> {
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
            UI.getCurrent().addWindow(new buildAddRuleWindow(rule ->
                        editorKit.getDataFactory().getRuleAddEvent(rule, activeOntology.getOWLOntology())
            ));
        } else if (action == EDIT) {

            try {
                UI.getCurrent().addWindow(new buildEditRuleWindow(
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
                        OWLEditorEventBus.post(editorKit.getDataFactory().getRuleRemoveEvent(
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
    public void handleRuleAddEvent(OWLEditorEvent.RuleAddEvent event) {
        try {
            Optional<SWRLAPIRule> rule = activeOntology.getSWRLRule(event.getAxiom().getRuleName());
            handleAddRule(rule.get());
        } catch (SWRLRuleException e) {
            Notification.show("Cannot add rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
            LOG.error(e.getMessage(), this);
        }
            
    }


    @Subscribe
    public void handleRuleRemoveEvent(OWLEditorEvent.RuleRemoveEvent event) {
        activeOntology.deleteSWRLRule(event.getAxiom().getRuleName());

        try {
            Optional<SWRLAPIRule> rule = activeOntology.getSWRLRule(event.getAxiom().getRuleName());
            Notification.show("Cannot remove rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
            LOG.error("Cannot remove rule ", rule.get());
        } catch (SWRLRuleException e) {
            Notification.show("Successfully removed rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
            rulesContainer.removeItem(event.getAxiom());

            LOG.info(e.getMessage(), this);
        }

    }

    @Subscribe
    public void handleRuleModifyEvent(OWLEditorEvent.RuleModifyEvent event) {
        /*
        activeOntology.deleteSWRLRule(event.getOldAxiom().getRuleName());

        try {
            SWRLAPIRule rule = activeOntology.getSWRLRule(event.getAxiom().getRuleName());
            Notification.show("Cannot remove rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
            LOG.error("Cannot remove rule ", rule);
        } catch (SWRLRuleException e) {
            SWRLAPIRule rule = activeOntology.getSWRLRule(event.getNewAxiom().getRuleName());
            handleAddRule(rule);
            Notification.show("Successfully removed rule " + event.getAxiom().getRuleName(),
                    Notification.Type.TRAY_NOTIFICATION);
            rulesContainer.removeItem(event.getAxiom());

            LOG.info(e.getMessage(), this);
        }
        */

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        
    }
}
