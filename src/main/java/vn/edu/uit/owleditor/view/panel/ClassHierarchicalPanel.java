package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vn.edu.uit.owleditor.event.OWLEditorEvent.SiblingClassCreatedEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToOWLClassConverter;
import vn.edu.uit.owleditor.utils.validator.OWLClassValidator;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@VaadinUIScope
@VaadinComponent
public class ClassHierarchicalPanel extends AbstractHierarchyPanel<OWLClass> {
    private static final Logger LOG = LoggerFactory.getLogger(ClassHierarchicalPanel.class);
    // Actions for the context menu
    private static final Action ADD_SUB = new Action("Add SubClass");
    private static final Action ADD_SIBLING = new Action("Add SiblingClass");
    private static final Action REMOVE = new Action("Remove");
    private static final Action[] ACTIONS = new Action[]{ADD_SUB,
            ADD_SIBLING, REMOVE};

    private final OWLClassTree tree;
    public ClassHierarchicalPanel() {
        super();
        tree = new OWLClassTree();
        tree.addActionHandler(this);
        buildComponents();
    }

    @Override
    public OWLClassSource getSelectedItem() {
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
        MHorizontalLayout configWrapper = new MHorizontalLayout(dwn, buildMenuBar()).withSpacing(false);

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
        reasonerToggle = act.addItem("Start reasoner", select -> startReasonerClickListener());
        reasonerToggle.setCheckable(true);
        act.addItem("Add SubClass", select -> handleSubNodeCreation());
        act.addItem("Add SiblingClass", select-> handleSiblingNodeCreation());
        act.addItem("Remove", select -> handleRemovalNode());
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


    private boolean checkOWLThing(OWLClassSource prop) {
        return prop.getValue().isOWLThing();
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

    @Override
    public void handleSubNodeCreation() {
        UI.getCurrent().addWindow(new buildAddOWLClassWindow(tree,
                c -> new OWLEditorEvent.SubClassCreatedEvent(c, tree.getSelectedItem().getValue()), true));

    }

    @Override
    public void handleSiblingNodeCreation() {
        if (!checkOWLThing(tree.selectedItem))
            UI.getCurrent().addWindow(new buildAddOWLClassWindow(tree,
                    c -> new SiblingClassCreatedEvent(c, tree.getSelectedItem().getValue()), false));
        
        else Notification.show("Warning", "Cannot create sibling for OWLThing", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleRemovalNode() {
        if (!checkOWLThing(tree.getSelectedItem()))

            ConfirmDialog.show(UI.getCurrent(), "Are you sure ?", dialog -> {
                if (dialog.isConfirmed()) {
                    tree.afterRemoved(new OWLEditorEvent.ClassRemovedEvent(
                            tree.getSelectedItem().getValue()));
                } else {
                    dialog.close();
                }
            });
        else Notification.show("Warning", "Cannot remove OWLThing", Notification.Type.WARNING_MESSAGE);
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

        private OWLDocumentFormat selectFormat() {
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
            } catch (NullPointerException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);

            }
            return documentFormat;
        }

        private StreamResource createResource() {
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

    public class OWLClassTree extends Tree implements TreeKit<OWLClassSource>,
            OWLEntityActionHandler<OWLEditorEvent.SubClassCreatedEvent, SiblingClassCreatedEvent, OWLEditorEvent.ClassRemovedEvent> {

        private final OWLClassHierarchicalContainer dataContainer;
        private final OWLClassSource selectedItem = new OWLClassSource();

        public OWLClassTree() {
            dataContainer = editorKit.getDataFactory().getOWLClassHierarchicalContainer();

//            editorKit.getModelManager().addOntologyChangeListener(changes -> {
//                for (OWLOntologyChange chg : changes) {
//                    chg.accept(dataContainer.getOWLOntologyChangeVisitor());
//                }
//            });
            setContainerDataSource(dataContainer);
            addValueChangeListener(this);
            setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId(OWLEditorData.OWLClassName);

        }

        @PostConstruct
        public void registerWithEventBus() {
            OWLEditorEventBus.register(this);
        }

        @Subscribe
        public void handleAfterSubClassOfAxiomAddEvent(OWLEditorEvent.afterSubClassOfAxiomAddEvent event) {
            event.getAxiom().accept(dataContainer.getOWLAxiomAdder());
        }
        
        @Override
        public OWLClassSource getSelectedItem() {
            return selectedItem;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (event.getProperty().getValue() != null) {
                selectedItem.setValue((OWLClass) event.getProperty().getValue());
            }
        }


        public void afterAddSiblingSaved(SiblingClassCreatedEvent event) {
            Boolean success = false;
            OWLAxiom clsDeclaration = owlFactory.getOWLDeclarationAxiom(event.getDeclareClass());

            ChangeApplied ok1 = editorKit
                    .getModelManager()
                    .addAxiom(editorKit.getActiveOntology(), clsDeclaration);
            if (ChangeApplied.SUCCESSFULLY == ok1) {
                success = true;
                clsDeclaration.accept(dataContainer.getOWLAxiomAdder());
            }

            for (OWLClassExpression ce : EntitySearcher
                    .getSuperClasses(event.getSiblingClass(), editorKit.getActiveOntology())) {

                if (!ce.isAnonymous()) {
                    OWLAxiom siblingAxiom = owlFactory.getOWLSubClassOfAxiom(event.getDeclareClass(), ce.asOWLClass());

                    ChangeApplied ok2 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), siblingAxiom);
                    if (ChangeApplied.SUCCESSFULLY == ok2 && success) {
                        siblingAxiom.accept(dataContainer.getOWLAxiomAdder());
                        expandItem(ce.asOWLClass());
                    } else success = false;
                    break;
                }
            }
            if (success)
                Notification.show("Successfully create " + OWLEditorKitImpl.getShortForm(event.getDeclareClass()),
                        Notification.Type.TRAY_NOTIFICATION);
            else Notification.show("Cannot create " + OWLEditorKitImpl.getShortForm(event.getDeclareClass()),
                        Notification.Type.WARNING_MESSAGE);

        }

        public void afterRemoved(OWLEditorEvent.ClassRemovedEvent event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());
            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());
            for (OWLOntologyChange axiom : changes) {
                axiom.accept(dataContainer.getOWLOntologyChangeVisitor());
            }
            dataContainer.getEntityRemover().reset();
        }

        public void afterAddSubSaved(OWLEditorEvent.SubClassCreatedEvent event) {
            OWLAxiom clsDeclaration = owlFactory.getOWLDeclarationAxiom(event.getSubClass());
            OWLAxiom subClsAxiom = owlFactory.getOWLSubClassOfAxiom(event.getSubClass(), event.getSuperClass());

            ChangeApplied ok1 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), clsDeclaration);
            ChangeApplied ok2 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), subClsAxiom);

            if (ChangeApplied.SUCCESSFULLY == ok1 && ChangeApplied.SUCCESSFULLY == ok2) {
                clsDeclaration.accept(dataContainer.getOWLAxiomAdder());
                subClsAxiom.accept(dataContainer.getOWLAxiomAdder());
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

}
