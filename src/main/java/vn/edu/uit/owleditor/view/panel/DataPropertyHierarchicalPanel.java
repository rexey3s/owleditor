package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.hierarchy.AbstractOWLObjectHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLDataPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.property.OWLDataPropertySource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToOWLDataPropertyConverter;
import vn.edu.uit.owleditor.utils.validator.OWLDataPropertyValidator;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/22/2014.
 */
public class DataPropertyHierarchicalPanel extends AbstractHierarchyPanel<OWLDataProperty> {
    // Actions for the context menu
    private static final Action ADD_SUB = new Action("Add Sub DataProperty");
    private static final Action ADD_SIBLING = new Action("Add Sibling DataProperty");
    private static final Action FUNCTIONAL_MARK = new Action("Mark as Functional");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD_SUB,
            ADD_SIBLING, FUNCTIONAL_MARK, REMOVE};

    private final OWLDataPropertyTree tree = new OWLDataPropertyTree();

    public DataPropertyHierarchicalPanel() {
        super();
        buildComponents();
        tree.addActionHandler(this);
    }

    private void buildComponents() {

        Panel treePanel = new Panel();
        treePanel.setContent(tree);
        treePanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        treePanel.setSizeFull();
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.addStyleName("hierarchy-view-toolbar");
        setCaption("DataProperties Hierarchy");
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        caption.setSizeUndefined();
        toolbar.addComponents(caption, buildMenuBar());
        toolbar.setExpandRatio(caption, 1.0f);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
        addStyleName(ValoTheme.LAYOUT_CARD);
        addStyleName("hierarchy-view");
        addComponent(toolbar);
        addComponent(treePanel);
        setExpandRatio(treePanel, 1.0f);
        setSizeFull();
    }

    private Component buildMenuBar() {

        final MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuBar.MenuItem act = tools.addItem("", FontAwesome.COG, null);
        reasonerToggle = act.addItem("Start reasoner", select -> startReasonerClickListener());
        reasonerToggle.setCheckable(true);
        act.addItem("Add Sub DataProperty", select -> handleSubItemCreate());
        act.addItem("Add Sibling DataProperty", select -> handleSiblingItemCreate());
        act.addItem("Remove", select -> handleItemRemove());
        return tools;
    }

    
    private boolean checkTopDataProp(OWLDataPropertySource prop) {
        return prop.getValue().isOWLTopDataProperty();
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void handleAction(Action action, Object sender, Object target) {

        if (action == ADD_SUB) {
            handleSubItemCreate();
        } else if (action == ADD_SIBLING) {
            handleSiblingItemCreate();
        } else if (action == REMOVE) {
            handleItemRemove();
        } else if (action == FUNCTIONAL_MARK) {
            Boolean checked = (Boolean)
                    tree.getContainerProperty(getSelectedItem().getValue(),
                            OWLEditorData.OWLFunctionalProperty).getValue();
            tree.getContainerProperty(getSelectedItem().getValue(), OWLEditorData.OWLFunctionalProperty)
                    .setValue(!checked);
        }
    }

    @Override
    public OWLDataPropertySource getSelectedItem() {
        return tree.getSelectedItem();
    }

    @Override
    public void handleSubItemCreate() {
        if (getSelectedItem().getValue() != null) {
            UI.getCurrent().addWindow(new buildAddDataPropertyWindow(tree,
                    s -> new OWLEditorEvent.SubDataPropertyAddEvent(s,
                            tree.getSelectedItem().getValue()), true));
        } else Notification.show("Notice",
                "Please select a Super DataProperty for your DataProperty", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleSiblingItemCreate() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a Sibling DataProperty for your DataProperty", Notification.Type.WARNING_MESSAGE);
        else if (!checkTopDataProp(tree.getSelectedItem()))
            UI.getCurrent().addWindow(new buildAddDataPropertyWindow(
                    tree,
                    s -> new OWLEditorEvent.SiblingDataPropertyAddEvent(s, tree.getSelectedItem().getValue()),
                    false));
        else Notification.show("Warning",
                    "You can not create any sibling for TopDataProperty", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleItemRemove() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a DataProperty to remove", Notification.Type.WARNING_MESSAGE);
        else if (!checkTopDataProp(tree.getSelectedItem()))

            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", components1 -> {
                if (components1.isConfirmed()) {
                    tree.handleRemoveEntityEvent(new OWLEditorEvent.DataPropertyRemoveEvent(
                            tree.getSelectedItem().getValue()));
                } else {
                    components1.close();
                }
            });

        else Notification.show("Notice",
                    "You can not remove TopDataProperty", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void addValueChangeListener(Property.ValueChangeListener listener) {
        tree.addValueChangeListener(listener);
    }

    @Deprecated
    public void addListener(Property.ValueChangeListener listener) {
        tree.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(Property.ValueChangeListener listener) {
        tree.removeValueChangeListener(listener);
    }

    @Deprecated
    public void removeListener(Property.ValueChangeListener listener) {
        tree.removeValueChangeListener(listener);
    }

    @Subscribe
    public void handleAfterSubDataPropertyOfAxiomAddEvent(OWLEditorEvent.afterSubDataPropertyOfAxiomAddEvent event) {
        event.getAxiom().accept(tree.getTreeDataContainer().getOWLAxiomAdder());
        tree.expandItem(event.getOwner());
    }
    
    public class OWLDataPropertyTree extends Tree implements TreeKit<OWLDataPropertySource>,
            OWLEntityActionHandler<OWLEditorEvent.SubDataPropertyAddEvent, OWLEditorEvent.SiblingDataPropertyAddEvent, OWLEditorEvent.DataPropertyRemoveEvent> {

        private final OWLDataPropertyHierarchicalContainer dataContainer;

        private final OWLDataPropertySource currentProperty = new OWLDataPropertySource();

        public OWLDataPropertyTree() {

            dataContainer = editorKit.getDataFactory().getOWLDataPropertyHierarchicalContainer();

//            editorKit.getModelManager().addOntologyChangeListener(changes -> {
//
//                for (OWLOntologyChange chg : changes) {
//                    chg.accept(dataContainer.getOWLOntologyChangeVisitor());
//                }
//            });

            setContainerDataSource(dataContainer);

            addValueChangeListener(this);

            dataContainer.addValueChangeListener(valueChangeEvent -> {
                if (valueChangeEvent.getProperty().getType() == Boolean.class) {
                    dataContainer.toggle((Boolean) valueChangeEvent.getProperty().getValue(),
                            editorKit.getOWLDataFactory().getOWLFunctionalDataPropertyAxiom(getSelectedItem().getValue()));
                    dataContainer.checkFunctionalIcon(getSelectedItem().getValue());

                    Notification.show(OWLEditorKitImpl.getShortForm(
                                    getSelectedItem().getValue())
                                    + " is functional: "
                                    + valueChangeEvent.getProperty().getValue(),
                            Notification.Type.TRAY_NOTIFICATION);
                }
            });

//            OWLEditorEventBus.register(this);
            setItemIconPropertyId(OWLEditorData.OWLEntityIcon);
            setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId(OWLEditorData.OWLDataPropertyName);

        }


        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (event.getProperty().getValue() != null) {
                currentProperty.setValue((OWLDataProperty) event.getProperty().getValue());
            }

        }

        @Override
        public void handleAddSubEntityEvent(OWLEditorEvent.SubDataPropertyAddEvent event) {
            OWLAxiom dataPropDeclaration = owlFactory.getOWLDeclarationAxiom(event.getSubProperty());
            OWLAxiom subPropAxiom = owlFactory
                    .getOWLSubDataPropertyOfAxiom(event.getSubProperty(), event.getSuperProperty());

            ChangeApplied ok1 = editorKit.getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), dataPropDeclaration);
            ChangeApplied ok2 = editorKit.getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), subPropAxiom);

            if (ok1 == ChangeApplied.SUCCESSFULLY && ok2 == ChangeApplied.SUCCESSFULLY) {
                dataPropDeclaration.accept(dataContainer.getOWLAxiomAdder());
                subPropAxiom.accept(dataContainer.getOWLAxiomAdder());
                expandItem(event.getSuperProperty());
                Notification.show("Successfully create "
                        + OWLEditorKitImpl.getShortForm(event.getSubProperty()));
            } else
                Notification.show("Cannot create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.WARNING_MESSAGE);

        }

        @Override
        public void handleAddSiblingEntityEvent(OWLEditorEvent.SiblingDataPropertyAddEvent event) {
            Boolean success = false;
            OWLAxiom dataPropDeclaration = owlFactory.getOWLDeclarationAxiom(event.getDeclareProperty());

            ChangeApplied ok1 = editorKit.getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), dataPropDeclaration);
            if (ChangeApplied.SUCCESSFULLY == ok1) {
                success = true;
                dataPropDeclaration.accept(dataContainer.getOWLAxiomAdder());
            }

            for (OWLDataPropertyExpression de : EntitySearcher.getSuperProperties(
                    event.getSiblingProperty(), editorKit.getActiveOntology())) {
                if (!de.isAnonymous()) {
                    OWLAxiom siblingAxiom = owlFactory.getOWLSubDataPropertyOfAxiom(
                            event.getDeclareProperty(), de.asOWLDataProperty());

                    ChangeApplied ok2 = editorKit.getModelManager()
                            .addAxiom(editorKit.getActiveOntology(), siblingAxiom);
                    if (ChangeApplied.SUCCESSFULLY == ok2 && success) {
                        siblingAxiom.accept(dataContainer.getOWLAxiomAdder());
                        expandItem(de.asOWLDataProperty());
                    } else success = false;
                    break;
                }
            }
            if (success)
                Notification.show("Successfully create "
                        + OWLEditorKitImpl.getShortForm(event.getDeclareProperty()));
            else
                Notification.show("Cannot create " + OWLEditorKitImpl.getShortForm(
                                event.getDeclareProperty()),
                        Notification.Type.WARNING_MESSAGE);
        }

        @Override
        public void handleRemoveEntityEvent(OWLEditorEvent.DataPropertyRemoveEvent event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());

            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());

            for (OWLOntologyChange axiom : changes) {

                axiom.accept(dataContainer.getOWLOntologyChangeVisitor());

            }
            dataContainer.getEntityRemover().reset();
        }

        @Override
        public AbstractOWLObjectHierarchicalContainer getTreeDataContainer() {
            return dataContainer;
        }

        @Override
        public OWLDataPropertySource getSelectedItem() {
            return currentProperty;
        }
    }

    public class buildAddDataPropertyWindow extends AbstractAddOWLObjectWindow<OWLDataProperty> {

        public buildAddDataPropertyWindow(@Nonnull OWLEntityActionHandler handler, @Nonnull OWLEntityAddHandler<OWLDataProperty> adder, @Nonnull Boolean isSub) {
            super(handler, adder, isSub);
            nameField.setCaption("DataProperty");
            nameField.setConverter(new StringToOWLDataPropertyConverter(editorKit));
            nameField.addValidator(new OWLDataPropertyValidator(editorKit));
        }
    }
}
