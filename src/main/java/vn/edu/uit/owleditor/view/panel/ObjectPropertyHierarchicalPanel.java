package vn.edu.uit.owleditor.view.panel;

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
 *         Faculty of Computer Network and Telecomunication created on 11/21/14.
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
    public OWLObjectPropertySource getSelectedProperty() {
        return tree.getCurrentProperty();
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
        act.addItem("Add Sub Property", selectedItem -> handleSubNodeCreation());
        act.addItem("Add Sibling Property", selectedItem -> handleSiblingNodeCreation());
        act.addItem("Remove", selectedItem -> handleRemovalNode());
        return tools;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_SUB) {
            handleSubNodeCreation();
        } else if (action == ADD_SIBLING) {
            handleSiblingNodeCreation();
        } else if (action == REMOVE) {
            handleRemovalNode();
        }
    }

    private boolean checkTopObjectProperty(OWLObjectPropertySource prop) {
        return prop.getValue().isOWLTopObjectProperty();
    }


    @Override
    public void handleSubNodeCreation() {
        UI.getCurrent().addWindow(new buildAddObjectPropertyWindow(
                tree,
                subject -> new OWLEditorEvent.SubObjectPropertyCreated(subject, tree.getCurrentProperty().getValue()),
                true));

    }

    @Override
    public void handleSiblingNodeCreation() {
        if (!checkTopObjectProperty(tree.getCurrentProperty()))
            UI.getCurrent().addWindow(new buildAddObjectPropertyWindow(
                    tree,
                    subject -> new OWLEditorEvent.SiblingObjectPropertyCreated(subject, tree.getCurrentProperty().getValue()),
                    false));

        else
            Notification
                    .show("Warning", "Cannot create sibling for TopObjectProperty",
                            Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleRemovalNode() {
        if (!checkTopObjectProperty(tree.getCurrentProperty()))

            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", components1 -> {
                if (components1.isConfirmed()) {
                    tree.afterRemoved(new OWLEditorEvent.ObjectPropertyRemoved(
                            tree.getCurrentProperty().getValue()));
                } else {
                    components1.close();
                }
            });

        else Notification.show("Warning",
                "Cannot remove TopObjectProperty",
                Notification.Type.WARNING_MESSAGE);
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

    public class OWLObjectPropertyTree extends Tree implements TreeKit<OWLObjectPropertySource>,
            OWLEntityActionHandler<OWLEditorEvent.SubObjectPropertyCreated,
                    OWLEditorEvent.SiblingObjectPropertyCreated, OWLEditorEvent.ObjectPropertyRemoved> {

        private final OWLObjectPropertyHierarchicalContainer dataContainer;

        private final OWLObjectPropertySource currentProperty = new OWLObjectPropertySource();

        public OWLObjectPropertyTree() {


            dataContainer = editorKit.getDataFactory().getOWLObjectPropertyHierarchicalContainer();

            editorKit.getModelManager().addOntologyChangeListener(changes -> {

                for (OWLOntologyChange chg : changes) {
                    chg.accept(dataContainer.getOWLOntologyChangeVisitor());
                }
            });
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


        public OWLObjectPropertySource getCurrentProperty() {
            return currentProperty;
        }


        @Override
        public void afterAddSubSaved(OWLEditorEvent.SubObjectPropertyCreated event) {
            OWLDeclarationAxiom objPropDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getSubProperty());

            OWLSubObjectPropertyOfAxiom subPropAxiom = editorKit
                    .getOWLDataFactory()
                    .getOWLSubObjectPropertyOfAxiom(event.getSubProperty(), event.getSuperProperty());

            if (editorKit
                    .getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), objPropDeclaration)
                    == ChangeApplied.SUCCESSFULLY
                    &&
                    editorKit.getModelManager()
                            .addAxiom(editorKit.getActiveOntology(), subPropAxiom)
                            == ChangeApplied.SUCCESSFULLY) {

                expandItem(event.getSuperProperty());
                Notification.show("Successfully create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show("Cannot create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.WARNING_MESSAGE);
            }
        }

        @Override
        public void afterAddSiblingSaved(OWLEditorEvent.SiblingObjectPropertyCreated event) {
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
        public void afterRemoved(OWLEditorEvent.ObjectPropertyRemoved event) {
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
