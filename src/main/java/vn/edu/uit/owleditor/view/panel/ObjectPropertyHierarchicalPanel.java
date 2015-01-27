package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.hierarchy.AbstractOWLObjectHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLObjectPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.property.OWLObjectPropertySource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToOWLObjectPropertyConverter;
import vn.edu.uit.owleditor.utils.validator.OWLObjectPropertyValidator;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/21/14.
 */
public class ObjectPropertyHierarchicalPanel extends AbstractHierarchyPanel<OWLObjectProperty> {

    // Actions for the context menu
    private static final Action ADD_SUB = new Action("Add Sub Property");
    private static final Action ADD_SIBLING = new Action("Add Sibling Property");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD_SUB,
            ADD_SIBLING, REMOVE};

    private final OWLObjectPropertyTree tree = new OWLObjectPropertyTree();

    public ObjectPropertyHierarchicalPanel() {
        super();
        buildComponents();
        tree.addActionHandler(this);
        Responsive.makeResponsive(this);
    }

    @Override
    public OWLObjectPropertySource getSelectedItem() {
        return tree.getSelectedItem();
    }

    private void buildComponents() {

        Panel treePanel = new Panel();
        treePanel.setContent(tree);
        treePanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        treePanel.setSizeFull();
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.addStyleName("hierarchy-view-toolbar");
        setCaption("Properties Hierarchy");
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
        act.addItem("Add Sub Property", select -> handleSubItemCreate());
        act.addItem("Add Sibling Property", select -> handleSiblingItemCreate());
        act.addItem("Remove", select -> handleItemRemove());
        return tools;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_SUB) {
            handleSubItemCreate();
        } else if (action == ADD_SIBLING) {
            handleSiblingItemCreate();
        } else if (action == REMOVE) {
            handleItemRemove();
        }
    }

    private boolean checkTopObjectProperty(OWLObjectPropertySource prop) {
        return prop.getValue().isOWLTopObjectProperty();
    }


    
    @Override
    public void handleSubItemCreate() {
        if (getSelectedItem().getValue() != null) {
            UI.getCurrent().addWindow(new buildAddObjectPropertyWindow(tree,
                    subject -> new OWLEditorEvent.SubObjectPropertyAddEvent(subject,
                            tree.getSelectedItem().getValue()), true));
        } else Notification.show("Notice",
                "Please select a Super ObjectProperty for your ObjectProperty", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleSiblingItemCreate() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a Sibling ObjectProperty for your ObjectProperty", Notification.Type.WARNING_MESSAGE);
        else if (!checkTopObjectProperty(tree.getSelectedItem()))
            UI.getCurrent().addWindow(new buildAddObjectPropertyWindow(tree,
                    subject -> new OWLEditorEvent.SiblingObjectPropertyAddEvent(subject,
                            tree.getSelectedItem().getValue()), false));

        else Notification.show("Notice",
                    "You can not create any sibling for TopObjectProperty", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleItemRemove() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a ObjectProperty to remove", Notification.Type.WARNING_MESSAGE);
        else if (!checkTopObjectProperty(tree.getSelectedItem()))
            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", components1 -> {
                if (components1.isConfirmed()) {
                    tree.handleRemoveEntityEvent(new OWLEditorEvent.ObjectPropertyRemove(
                            tree.getSelectedItem().getValue()));
                } else {
                    components1.close();
                }
            });

        else Notification.show("Notice",
                    "You can not remove TopObjectProperty", Notification.Type.WARNING_MESSAGE);
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
    public void handleafterSubObjectPropertyOfAxiomAddEvent(OWLEditorEvent.afterSubObjectPropertyOfAxiomAddEvent event) {
        event.getAxiom().accept(tree.getTreeDataContainer().getOWLAxiomAdder());
    }
    public class OWLObjectPropertyTree extends Tree implements TreeKit<OWLObjectPropertySource>,
            OWLEntityActionHandler<OWLEditorEvent.SubObjectPropertyAddEvent,
                    OWLEditorEvent.SiblingObjectPropertyAddEvent, OWLEditorEvent.ObjectPropertyRemove> {

        private final OWLObjectPropertyHierarchicalContainer dataContainer;

        private final OWLObjectPropertySource currentProperty = new OWLObjectPropertySource();

        public OWLObjectPropertyTree() {
            dataContainer = editorKit.getDataFactory().getOWLObjectPropertyHierarchicalContainer();

//            editorKit.getModelManager().addOntologyChangeListener(changes -> {
//
//                for (OWLOntologyChange chg : changes) {
//                    chg.accept(dataContainer.getOWLOntologyChangeVisitor());
//                }
//            });
            setContainerDataSource(dataContainer);
            addValueChangeListener(this);
            setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId(OWLEditorData.OWLObjectPropertyName);

        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (event.getProperty().getValue() != null) {
                currentProperty.setValue((OWLObjectProperty) event.getProperty().getValue());
            }
        }


        @Override
        public AbstractOWLObjectHierarchicalContainer getTreeDataContainer() {
            return dataContainer;
        }

        public OWLObjectPropertySource getSelectedItem() {
            return currentProperty;
        }


        @Override
        public void handleAddSubEntityEvent(OWLEditorEvent.SubObjectPropertyAddEvent event) {
            OWLAxiom objPropDeclaration = owlFactory.getOWLDeclarationAxiom(event.getSubProperty());
            OWLAxiom subPropAxiom = owlFactory
                    .getOWLSubObjectPropertyOfAxiom(event.getSubProperty(), event.getSuperProperty());
            ChangeApplied ok1 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), objPropDeclaration);
            ChangeApplied ok2 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), subPropAxiom);
            if (ok1 == ChangeApplied.SUCCESSFULLY && ok2 == ChangeApplied.SUCCESSFULLY) {
                objPropDeclaration.accept(dataContainer.getOWLAxiomAdder());
                subPropAxiom.accept(dataContainer.getOWLAxiomAdder());
                expandItem(event.getSuperProperty());
                Notification.show("Successfully create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show("Can not create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.WARNING_MESSAGE);
            }
        }

        @Override
        public void handleAddSiblingEntityEvent(OWLEditorEvent.SiblingObjectPropertyAddEvent event) {
            OWLDeclarationAxiom objPropDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getDeclareProperty());

            ChangeApplied ok = editorKit
                    .getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), objPropDeclaration);


            for (OWLObjectPropertyExpression pe : EntitySearcher.getSuperProperties(
                    event.getSiblingProperty(), editorKit.getActiveOntology())) {

                OWLSubObjectPropertyOfAxiom siblingAxiom = editorKit
                        .getOWLDataFactory().getOWLSubObjectPropertyOfAxiom(
                                event.getDeclareProperty(),
                                pe.asOWLObjectProperty());

                ok = editorKit
                        .getModelManager()
                        .addAxiom(editorKit.getActiveOntology(), siblingAxiom);
            }
            if (ok == ChangeApplied.SUCCESSFULLY) {
                Notification.show("Successfully create "
                                + OWLEditorKitImpl.getShortForm(event.getDeclareProperty()),
                        Notification.Type.TRAY_NOTIFICATION);
            } else
                Notification.show("Cannot create " + OWLEditorKitImpl.getShortForm(
                                event.getDeclareProperty()),
                        Notification.Type.WARNING_MESSAGE);
        }

        @Override
        public void handleRemoveEntityEvent(OWLEditorEvent.ObjectPropertyRemove event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());

            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());

            for (OWLOntologyChange axiom : changes) {

                axiom.accept(dataContainer.getOWLOntologyChangeVisitor());

                System.out.println(OWLEditorKitImpl.render(axiom.getAxiom()));
            }
            dataContainer.getEntityRemover().reset();
        }
    }

    public class buildAddObjectPropertyWindow extends AbstractAddOWLObjectWindow<OWLObjectProperty> {

        public buildAddObjectPropertyWindow(@Nonnull OWLEntityActionHandler handler, @Nonnull OWLEntityAddHandler<OWLObjectProperty> adder, @Nonnull Boolean isSub) {
            super(handler, adder, isSub);
            nameField.setCaption("ObjectProperty");
            nameField.setConverter(new StringToOWLObjectPropertyConverter(editorKit));
            nameField.addValidator(new OWLObjectPropertyValidator(editorKit));
        }
    }
}
