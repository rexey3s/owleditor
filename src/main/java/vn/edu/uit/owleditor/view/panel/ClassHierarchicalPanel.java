package vn.edu.uit.owleditor.view.panel;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.hierarchy.OWLClassHierarchicalContainer;
import vn.edu.uit.owleditor.data.property.OWLClassSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEvent.SiblingClassCreated;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToOWLClassConverter;
import vn.edu.uit.owleditor.utils.validator.OWLClassValidator;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;


public class ClassHierarchicalPanel extends AbstractHierarchyPanel<OWLClass> {
    private static final Log LOG = LogFactory.getLog(ClassHierarchicalPanel.class);
    // Actions for the context menu
    private static final Action ADD_SUB = new Action("Add SubClass");
    private static final Action ADD_SIBLING = new Action("Add SiblingClass");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD_SUB,
            ADD_SIBLING, REMOVE};
    //    private static int eventCount = 0;
    private final OWLClassTree owlTree;

    public ClassHierarchicalPanel(@Nonnull OWLEditorKit eKit) {
        super(eKit);
        owlTree = new OWLClassTree(editorKit);
        owlTree.addActionHandler(this);

        buildComponents();

    }

    @Override
    public OWLClassSource getSelectedProperty() {
        return owlTree.getCurrentProperty();
    }

    private void buildComponents() {

        Panel treePanel = new Panel();
        treePanel.setContent(owlTree);
        treePanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        treePanel.setSizeFull();
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.addStyleName("hierarchy-view-toolbar");
        setCaption("Class Hierarchy");
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        caption.setSizeUndefined();
        Button dwn = new Button();
        dwn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        dwn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        dwn.setIcon(FontAwesome.DOWNLOAD);
        dwn.addClickListener(selected -> {
            try {
                StreamResource rs = createResource();
                FileDownloader fd = new FileDownloader(rs);
                fd.extend(dwn);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });
        CssLayout configWrapper = new CssLayout();
        configWrapper.addComponents(dwn, buildMenuBar());
        configWrapper.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        toolbar.addComponents(caption, configWrapper);
        toolbar.setExpandRatio(caption, 1.0f);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
        addStyleName(ValoTheme.LAYOUT_CARD);
        addStyleName("hierarchy-view");
        addComponent(toolbar);
        addComponent(treePanel);
        setExpandRatio(treePanel, 1.0f);
        setSizeFull();
    }

    private StreamResource createResource() {
        return new StreamResource(() -> {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                editorKit.getModelManager()
                        .saveOntology(editorKit.getActiveOntology(), new StreamDocumentTarget(bos));
                return new ByteArrayInputStream(bos.toByteArray());

            } catch (OWLOntologyStorageException e) {
                e.printStackTrace();
                return null;
            }

        }, "embedded_ontology.owl");
    }

    private Component buildMenuBar() {

        final MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuBar.MenuItem act = tools.addItem("", FontAwesome.COG, null);
        act.addItem("Start reasoner", selected -> toggleReasoner(!editorKit.getReasonerStatus()));
        act.addItem("Add SubClass", selectedItem -> handleSubNodeCreation());
        act.addItem("Add SiblingClass", selectedItem -> handleSiblingNodeCreation());
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

    private void toggleReasoner(Boolean value) {
        if (value) {
            editorKit.setReasonerStatus(value);
            Notification.show("Reasoner activated", Notification.Type.TRAY_NOTIFICATION);
        } else {
            editorKit.setReasonerStatus(value);
            Notification.show("Reasoner deactivated", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private boolean checkOWLThing(OWLClassSource prop) {
        return prop.getValue().isOWLThing();
    }

    @Override
    public void addValueChangeListener(Property.ValueChangeListener listener) {
        owlTree.addValueChangeListener(listener);
    }

    @Deprecated
    public void addListener(Property.ValueChangeListener listener) {
        owlTree.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(Property.ValueChangeListener listener) {
        owlTree.removeValueChangeListener(listener);
    }

    @Deprecated
    public void removeListener(Property.ValueChangeListener listener) {
        owlTree.removeValueChangeListener(listener);
    }

    @Override
    public void handleSubNodeCreation() {
        UI.getCurrent().addWindow(new buildAddOWLClassWindow(
                owlTree, c -> new OWLEditorEvent.SubClassCreated(c, owlTree.getCurrentProperty().getValue()), true));

    }

    @Override
    public void handleSiblingNodeCreation() {
        if (!checkOWLThing(owlTree.currentProperty))
            UI.getCurrent().addWindow(new buildAddOWLClassWindow(
                    owlTree, c -> new OWLEditorEvent.SiblingClassCreated(c, owlTree.getCurrentProperty().getValue()), false));

        else
            Notification
                    .show("Warning", "Cannot create sibling for OWLThing",
                            Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleRemovalNode() {
        if (!checkOWLThing(owlTree.getCurrentProperty()))

            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", components1 -> {
                if (components1.isConfirmed()) {
                    owlTree.afterRemoved(new OWLEditorEvent.ClassRemoved(
                            owlTree.getCurrentProperty().getValue()));
                } else {
                    components1.close();
                }
            });

        else Notification.show("Warning",
                "Cannot remove OWLThing",
                Notification.Type.WARNING_MESSAGE);
    }

    public static class OWLClassTree extends Tree implements TreeKit<OWLClassSource>,
            OWLEntityActionHandler<OWLEditorEvent.SubClassCreated, SiblingClassCreated, OWLEditorEvent.ClassRemoved> {

        private final OWLClassHierarchicalContainer dataContainer;
        private final OWLClassSource currentProperty = new OWLClassSource();
        private final OWLEditorKit editorKit;

        public OWLClassTree(@Nonnull OWLEditorKit eKit) {

            editorKit = eKit;

            dataContainer = editorKit.getDataFactory().getOWLClassHierarchicalContainer();

            editorKit.getModelManager().addOntologyChangeListener(changes -> {

                for (OWLOntologyChange chg : changes) {
                    chg.accept(dataContainer.getOWLOntologyChangeListener());
                }
            });

            setContainerDataSource(dataContainer);

            addValueChangeListener(this);

            setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

            setItemCaptionPropertyId(OWLEditorData.OWLClassName);

        }

        @Override
        public OWLClassSource getCurrentProperty() {
            return currentProperty;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (event.getProperty().getValue() != null) {
                currentProperty.setValue((OWLClass) event.getProperty().getValue());
            }
        }


        @Override
        public void afterAddSiblingSaved(SiblingClassCreated event) {
            OWLAxiom clsDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getDeclareClass());

            ChangeApplied ok = editorKit
                    .getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), clsDeclaration);

            for (OWLClassExpression ce : EntitySearcher.getSuperClasses(event
                    .getSiblingClass(), editorKit
                    .getActiveOntology())) {

                OWLAxiom siblingAxiom = editorKit
                        .getOWLDataFactory()
                        .getOWLSubClassOfAxiom(event
                                        .getDeclareClass(),
                                ce.asOWLClass());

                ok = editorKit
                        .getModelManager()
                        .addAxiom(editorKit.getActiveOntology(), siblingAxiom);
            }
            if (ok == ChangeApplied.SUCCESSFULLY)
                Notification.show("Successfully create "
                                + OWLEditorKit.getShortForm(event.getDeclareClass()),
                        Notification.Type.TRAY_NOTIFICATION);
            else {
                Notification.show("Cannot create " + OWLEditorKit.getShortForm(
                                event.getDeclareClass()),
                        Notification.Type.WARNING_MESSAGE);
            }
        }

        @Override
        public void afterRemoved(OWLEditorEvent.ClassRemoved event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());
            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());


            for (OWLOntologyChange axiom : changes) {

                axiom.accept(dataContainer.getOWLOntologyChangeListener());

                LOG.info(String.format("{0} change: {1}", System.currentTimeMillis(),
                        OWLEditorKit.render(axiom.getAxiom())));
            }
            dataContainer.getEntityRemover().reset();
        }

        @Override
        public void afterAddSubSaved(OWLEditorEvent.SubClassCreated event) {
            OWLDeclarationAxiom clsDeclaration = editorKit
                    .getOWLDataFactory()
                    .getOWLDeclarationAxiom(event.getSubClass());
            OWLSubClassOfAxiom subClsAxiom = editorKit
                    .getOWLDataFactory()
                    .getOWLSubClassOfAxiom(event.getSubClass(), event.getSuperClass());

            ChangeApplied declareOk = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), clsDeclaration);
            ChangeApplied subClzOk = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), subClsAxiom);
            if (ChangeApplied.SUCCESSFULLY == declareOk && ChangeApplied.SUCCESSFULLY == subClzOk) {
                expandItem(event.getSuperClass());
                    Notification.show("Successfully create "
                                    + OWLEditorKit.getShortForm(event.getSubClass()),
                            Notification.Type.TRAY_NOTIFICATION);
            } else {
                    Notification.show("Cannot create "
                                    + OWLEditorKit.getShortForm(event.getSubClass()),
                            Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    public class buildAddOWLClassWindow extends AbstractAddOWLObjectWindow<OWLClass> {

        public buildAddOWLClassWindow(@Nonnull OWLEntityActionHandler handler,
                                      @Nonnull OWLEntityAddHandler<OWLClass> adder,
                                      @Nonnull Boolean isSub) {
            super(handler, adder, isSub);
            nameField.setConverter(new StringToOWLClassConverter(editorKit));
            nameField.addValidator(new OWLClassValidator(editorKit));
        }
    }

}
