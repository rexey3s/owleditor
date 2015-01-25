package vn.edu.uit.owleditor.view.panel;

import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.springframework.util.Assert;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
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

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@VaadinUIScope
@VaadinComponent
public class ClassHierarchicalPanel extends AbstractHierarchyPanel<OWLClass> {
    
    // Actions for the context menu
    private static final Action ADD_SUB = new Action("Add SubClass");
    private static final Action ADD_SIBLING = new Action("Add SiblingClass");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD_SUB,
            ADD_SIBLING, REMOVE};
    //    private static int eventCount = 0;
    private final OWLClassTree owlTree;
    private MenuBar.MenuItem reasonerToggle;
    public ClassHierarchicalPanel() {
        owlTree = new OWLClassTree();
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
        dwn.addClickListener(selected -> UI.getCurrent().addWindow(new DownloadOntologyWindow()));
        MHorizontalLayout configWrapper = new MHorizontalLayout(dwn, buildMenuBar());
        
        toolbar.addComponents(caption, configWrapper);
//        toolbar.setExpandRatio(caption, 1.0f);
        toolbar.setComponentAlignment(configWrapper, Alignment.MIDDLE_RIGHT);
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
        reasonerToggle = act.addItem("Start reasoner", selected -> toggleReasoner(!editorKit.getReasonerStatus()));
        reasonerToggle.setCheckable(true);
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
    public void setReasonerToggle(Boolean value) {
        reasonerToggle.setCheckable(value);
    }
    private void toggleReasoner(Boolean value) {
        editorKit.setReasonerStatus(value);
        reasonerToggle.setChecked(value);
        if (value) {          
            Notification.show("Reasoner activated", Notification.Type.TRAY_NOTIFICATION);
        } else {            
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

        public OWLClassTree() {
            editorKit = OWLEditorUI.getEditorKit();
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
                                + OWLEditorKitImpl.getShortForm(event.getDeclareClass()),
                        Notification.Type.TRAY_NOTIFICATION);
            else {
                Notification.show("Cannot create " + OWLEditorKitImpl.getShortForm(
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
                                    + OWLEditorKitImpl.getShortForm(event.getSubClass()),
                            Notification.Type.TRAY_NOTIFICATION);
            } else {
                    Notification.show("Cannot create "
                                    + OWLEditorKitImpl.getShortForm(event.getSubClass()),
                            Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    public class buildAddOWLClassWindow extends AbstractAddOWLObjectWindow<OWLClass> {

        public buildAddOWLClassWindow(@Nonnull OWLEntityActionHandler handler,
                                      @Nonnull OWLEntityAddHandler<OWLClass> adder,
                                      @Nonnull Boolean isSub) {
            super(handler, adder, isSub);
            nameField.setCaption("Class");
            nameField.setConverter(new StringToOWLClassConverter(editorKit));
            nameField.addValidator(new OWLClassValidator(editorKit));
        }
    }
    public static class DownloadOntologyWindow extends Window {
        private final OWLEditorKit eKit;
        private final TextField ontologyName = new TextField();
        private final OWLDocumentFormat documentFormat;
//        private OWLDocumentFormat targetFormat;
        private final ComboBox formats = new ComboBox();
        private OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
        private RDFXMLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
        private ManchesterSyntaxDocumentFormat manSyntaxFormat = new ManchesterSyntaxDocumentFormat();
        private FunctionalSyntaxDocumentFormat funcSyntaxFormat = new FunctionalSyntaxDocumentFormat();
        public DownloadOntologyWindow() {
            eKit = OWLEditorUI.getEditorKit();
            documentFormat = eKit.getModelManager().getOntologyFormat(eKit.getActiveOntology());
            formats.setNullSelectionAllowed(false);
            formats.addContainerProperty("FORMAT", String.class, null);
            formats.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            formats.setItemCaptionPropertyId("FORMAT");
            initFormatsBox();
            initialize();
        

        }
        @SuppressWarnings("unchecked")
        private void initFormatsBox() {
            formats.addItem(rdfxmlFormat);
            formats.getContainerProperty(rdfxmlFormat, "FORMAT").setValue("RDF/XML");
            formats.addItem(owlxmlFormat);
            formats.getContainerProperty(owlxmlFormat, "FORMAT").setValue("OWL/XML");
            formats.addItem(manSyntaxFormat);
            formats.getContainerProperty(manSyntaxFormat, "FORMAT").setValue("ManchesterSyntax");
            formats.addItem(funcSyntaxFormat);
            formats.getContainerProperty(funcSyntaxFormat, "FORMAT").setValue("FunctionalSyntax");
        }
        private void initialize() {
            setModal(true);
            setClosable(false);
            setResizable(false);
            setWidth(300.0f, Unit.PIXELS);
            setHeight(250.0f, Unit.PIXELS);
            setContent(buildContent());
        }
        private OWLDocumentFormat selectFormat()  {
            try {
                Assert.notNull(formats.getValue(), "Select format");

                if (documentFormat.isPrefixOWLOntologyFormat()) {
                    if (formats.getValue() instanceof RDFXMLDocumentFormat) {
                        rdfxmlFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                        return rdfxmlFormat;
                    }
                    if (formats.getValue() instanceof OWLXMLDocumentFormat) {
                        owlxmlFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                        return owlxmlFormat;
                    }
                    if (formats.getValue() instanceof ManchesterSyntaxDocumentFormat) {
                        manSyntaxFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                        return manSyntaxFormat;
                    }
                    if (formats.getValue() instanceof FunctionalSyntaxDocumentFormat) {
                        funcSyntaxFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                        return funcSyntaxFormat;
                    }
                }
            }
            catch (NullPointerException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);

            }
            return documentFormat;
        }
        private StreamResource createResource()  {
            return new StreamResource(() -> {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    Assert.notNull(ontologyName.getValue(), "Enter name");
//                    Assert.notNull(formats.getValue(), "Select format");
                    eKit.getModelManager()
                            .saveOntology(eKit.getActiveOntology(), selectFormat(), new StreamDocumentTarget(bos));

                } catch (NullPointerException | OWLOntologyStorageException nullEx) {
                    Notification.show(nullEx.getMessage(), Notification.Type.WARNING_MESSAGE);
                }
                return new ByteArrayInputStream(bos.toByteArray());
            }, ontologyName.getValue());
        }
        
        private Component buildContent() {
            final VerticalLayout result = new VerticalLayout();
            result.setMargin(true);
            result.setSpacing(true);
            FormLayout form = new FormLayout();
            ontologyName.focus();
            ontologyName.setCaption("FileName");
            ontologyName.setValue("ontology.owl");
            formats.setCaption("Format");
            
            form.addComponent(ontologyName);
            form.addComponent(formats);
            result.addComponent(form);
            result.addComponent(buildFooter());

            return result;
        }

        private Component buildFooter() {
            final HorizontalLayout footer = new HorizontalLayout();
            footer.setSpacing(true);
            footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
            footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

            Button cancel = new Button("Cancel");
            cancel.addClickListener(event -> close());
            cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

            Button save = new Button("Save");
            save.addStyleName(ValoTheme.BUTTON_PRIMARY);

            StreamResource rs = createResource();
            FileDownloader fd = new FileDownloader(rs);
            fd.extend(save);

            save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

            footer.addComponents(cancel, save);
            footer.setExpandRatio(cancel, 1);
            footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
            return footer;

        }

    }

}
