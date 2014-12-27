package vn.edu.uit.owleditor.view.panel;

import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.vaadin.dialogs.ConfirmDialog;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
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
 *         Faculty of Computer Network and Telecomunication created on 11/22/2014.
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
        act.addItem("Add Sub DataProperty", selectedItem -> handleSubNodeCreation());
        act.addItem("Add Sibling DataProperty", selectedItem -> handleSiblingNodeCreation());
        act.addItem("Remove", selectedItem -> handleRemovalNode());
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
            handleSubNodeCreation();
        } else if (action == ADD_SIBLING) {
            handleSiblingNodeCreation();
        } else if (action == REMOVE) {
            handleRemovalNode();
        } else if (action == FUNCTIONAL_MARK) {
            Boolean checked = (Boolean)
                    tree.getContainerProperty(getSelectedProperty().getValue(),
                            OWLEditorData.OWLFunctionalProperty).getValue();
            tree.getContainerProperty(getSelectedProperty().getValue(), OWLEditorData.OWLFunctionalProperty)
                    .setValue(!checked);
        }
    }

    @Override
    public Property<OWLDataProperty> getSelectedProperty() {
        return tree.getCurrentProperty();
    }

    @Override
    public void handleSubNodeCreation() {
        UI.getCurrent().addWindow(new buildAddDataPropertyWindow(
                tree,
                s -> new OWLEditorEvent.SubDataPropertyCreated(s, tree.getCurrentProperty().getValue()),
                true));
    }

    @Override
    public void handleSiblingNodeCreation() {
        if (!checkTopDataProp(tree.getCurrentProperty()))
            UI.getCurrent().addWindow(new buildAddDataPropertyWindow(
                    tree,
                    s -> new OWLEditorEvent.SiblingDataPropertyCreated(s, tree.getCurrentProperty().getValue()),
                    false));
        else
            Notification
                    .show("Warning", "Cannot create sibling for TopDataProperty",
                            Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleRemovalNode() {
        if (!checkTopDataProp(tree.getCurrentProperty()))

            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", components1 -> {
                if (components1.isConfirmed()) {
                    tree.afterRemoved(new OWLEditorEvent.DataPropertyRemoved(
                            tree.getCurrentProperty().getValue()));
                } else {
                    components1.close();
                }
            });

        else Notification.show("Warning",
                "Cannot remove TopDataProperty",
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

    public class OWLDataPropertyTree extends Tree implements TreeKit<OWLDataPropertySource>,
            OWLEntityActionHandler<OWLEditorEvent.SubDataPropertyCreated, OWLEditorEvent.SiblingDataPropertyCreated, OWLEditorEvent.DataPropertyRemoved> {

        private final OWLDataPropertyHierarchicalContainer dataContainer;

        private final OWLDataPropertySource currentProperty = new OWLDataPropertySource();

        public OWLDataPropertyTree() {

            dataContainer = editorKit.getDataFactory().getOWLDataPropertyHierarchicalContainer();

            editorKit.getModelManager().addOntologyChangeListener(changes -> {

                for (OWLOntologyChange chg : changes) {
                    chg.accept(dataContainer.getOWLOntologyChangeListener());
                }
            });

            setContainerDataSource(dataContainer);

            addValueChangeListener(this);

            dataContainer.addValueChangeListener(valueChangeEvent -> {
                if (valueChangeEvent.getProperty().getType() == Boolean.class) {
                    dataContainer.toggle((Boolean) valueChangeEvent.getProperty().getValue(),
                            editorKit.getOWLDataFactory().getOWLFunctionalDataPropertyAxiom(getCurrentProperty().getValue()));
                    dataContainer.checkFunctionalIcon(getCurrentProperty().getValue());

                    Notification.show(OWLEditorKitImpl.getShortForm(
                                    getCurrentProperty().getValue())
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
        public void afterAddSubSaved(OWLEditorEvent.SubDataPropertyCreated event) {

            OWLDeclarationAxiom objPropDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getSubProperty());

            OWLSubDataPropertyOfAxiom subPropAxiom = editorKit
                    .getOWLDataFactory()
                    .getOWLSubDataPropertyOfAxiom(event.getSubProperty(), event.getSuperProperty());

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
                        + OWLEditorKitImpl.getShortForm(event.getSubProperty()));
            } else
                Notification.show("Cannot create "
                                + OWLEditorKitImpl.getShortForm(event.getSubProperty()),
                        Notification.Type.WARNING_MESSAGE);

        }

        @Override
        public void afterAddSiblingSaved(OWLEditorEvent.SiblingDataPropertyCreated event) {
            OWLDeclarationAxiom objPropDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getDeclareProperty());

            ChangeApplied ok = editorKit
                    .getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), objPropDeclaration);


            for (OWLDataPropertyExpression pe : EntitySearcher.getSuperProperties(
                    event.getSiblingProperty(), editorKit.getActiveOntology())) {

                OWLSubDataPropertyOfAxiom siblingAxiom = editorKit
                        .getOWLDataFactory().getOWLSubDataPropertyOfAxiom(
                                event.getDeclareProperty(),
                                pe.asOWLDataProperty());

                editorKit
                        .getModelManager()
                        .addAxiom(editorKit.getActiveOntology(), siblingAxiom);
            }
            if (ok == ChangeApplied.SUCCESSFULLY)
                Notification.show("Successfully create "
                        + OWLEditorKitImpl.getShortForm(event.getDeclareProperty()));
            else
                Notification.show("Cannot create " + OWLEditorKitImpl.getShortForm(
                                event.getDeclareProperty()),
                        Notification.Type.WARNING_MESSAGE);
        }

        @Override
        public void afterRemoved(OWLEditorEvent.DataPropertyRemoved event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());

            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());

            for (OWLOntologyChange axiom : changes) {

                axiom.accept(dataContainer.getOWLOntologyChangeListener());

            }
            dataContainer.getEntityRemover().reset();
        }

        @Override
        public OWLDataPropertySource getCurrentProperty() {
            return currentProperty;
        }
    }

    public class buildAddDataPropertyWindow extends AbstractAddOWLObjectWindow<OWLDataProperty> {

        public buildAddDataPropertyWindow(@Nonnull OWLEntityActionHandler handler, @Nonnull OWLEntityAddHandler<OWLDataProperty> adder, @Nonnull Boolean isSub) {
            super(handler, adder, isSub);
            nameField.setConverter(new StringToOWLDataPropertyConverter(editorKit));
            nameField.addValidator(new OWLDataPropertyValidator(editorKit));
        }
    }
}
