package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.hierarchy.AbstractOWLObjectHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLClassHierarchicalContainer;
import vn.edu.uit.owleditor.data.property.OWLClassSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEvent.SiblingClassAddEvent;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.event.OWLEntityAddHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToOWLClassConverter;
import vn.edu.uit.owleditor.utils.validator.OWLClassValidator;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;
import vn.edu.uit.owleditor.view.window.DownloadOntologyWindow;

import javax.annotation.Nonnull;
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
        Responsive.makeResponsive(caption, configWrapper);
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
        act.addItem("Add SubClass", select -> handleSubItemCreate());
        act.addItem("Add SiblingClass", select -> handleSiblingItemCreate());
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
    public void handleSubItemCreate() {
        if (getSelectedItem().getValue() != null) {
            UI.getCurrent().addWindow(new buildAddOWLClassWindow(tree,
                    c -> new OWLEditorEvent.SubClassAddEvent(c, tree.getSelectedItem().getValue()), true));
        } else Notification.show("Notice",
                "Please select a Super Class for your Class", Notification.Type.WARNING_MESSAGE);

    }

    @Override
    public void handleSiblingItemCreate() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a Sibling Class for your Class", Notification.Type.WARNING_MESSAGE);

        else if (!checkOWLThing(tree.selectedItem))
            UI.getCurrent().addWindow(new buildAddOWLClassWindow(tree,
                    c -> new SiblingClassAddEvent(c, tree.getSelectedItem().getValue()), false));

        else Notification.show("Notice",
                    "You can not create any sibling for OWLThing", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void handleItemRemove() {
        if (getSelectedItem().getValue() == null)
            Notification.show("Notice",
                    "Please select a Class to remove", Notification.Type.WARNING_MESSAGE);

        else if (!checkOWLThing(tree.getSelectedItem()))
            ConfirmDialog.show(UI.getCurrent(),
                    "Are you sure ?", dialog -> {
                if (dialog.isConfirmed()) {
                    tree.handleRemoveEntityEvent(new OWLEditorEvent.ClassRemoveEvent(
                            tree.getSelectedItem().getValue()));
                } else {
                    dialog.close();
                }
                    });

        else Notification.show("Notice", "You can not remove OWLThing", Notification.Type.WARNING_MESSAGE);
    }

    @Subscribe
    public void handleAfterSubClassOfAxiomAddEvent(OWLEditorEvent.afterSubClassOfAxiomAddEvent event) {
        event.getAxiom().accept(tree.getTreeDataContainer().getOWLAxiomAdder());
        tree.expandItem(event.getOwner());
    }

    @Subscribe
    public void handleAfterSubClassOfAxiomRemoveEvent(OWLEditorEvent.afterSubClassOfAxiomRemoveEvent event) {
        event.getAxiom().accept(tree.getTreeDataContainer().getOWLAxiomRemover());
    }
    

    public class OWLClassTree extends Tree implements TreeKit<OWLClassSource>,
            OWLEntityActionHandler<OWLEditorEvent.SubClassAddEvent, OWLEditorEvent.SiblingClassAddEvent, OWLEditorEvent.ClassRemoveEvent> {
        private final OWLClassHierarchicalContainer dataContainer;
        private final OWLClassSource selectedItem = new OWLClassSource();

        public OWLClassTree() {
            dataContainer = editorKit.getDataFactory().getOWLClassHierarchicalContainer();

            setContainerDataSource(dataContainer);
            addValueChangeListener(this);
            setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId(OWLEditorData.OWLClassName);
        }


        @Override
        public AbstractOWLObjectHierarchicalContainer getTreeDataContainer() {
            return dataContainer;
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

        public void handleAddSiblingEntityEvent(OWLEditorEvent.SiblingClassAddEvent event) {
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

        public void handleRemoveEntityEvent(OWLEditorEvent.ClassRemoveEvent event) {
            event.getRemovedObject().accept(dataContainer.getEntityRemover());

            List<OWLOntologyChange> changes = editorKit.getModelManager()
                    .applyChanges(dataContainer.getEntityRemover().getChanges());

            for (OWLOntologyChange axiom : changes) {
                axiom.accept(dataContainer.getOWLOntologyChangeVisitor());
            }
            dataContainer.getEntityRemover().reset();
        }


        public void handleAddSubEntityEvent(OWLEditorEvent.SubClassAddEvent event) {
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

//        public buildAddOWLClassWindow(@Nonnull OWLEntityAddHandler<OWLClass> adder,
//                                      @Nonnull Boolean isSub) {
//            super(adder, isSub);
//            nameField.setCaption("Class");
//            nameField.setConverter(new StringToOWLClassConverter(editorKit));
//            nameField.addValidator(new OWLClassValidator(editorKit));
//        }
//
//        @Override
//        protected Button.ClickListener getSaveListener() {
//            return click -> {
//                try {
//                    nameField.validate();
//                    if (isSub)
//                        OWLEditorEventBus.post(adder.addingEntity((OWLClass) nameField.getConvertedValue()));
//                    else
//                        OWLEditorEventBus.post((adder.addingEntity((OWLClass) nameField.getConvertedValue())));
//                    close();
//                } catch (Validator.InvalidValueException ex) {
//                    Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
//                } catch (Exception e) {
//                    LOG.error(e.getMessage(), this);
//                }
//            };
//        }
    }

}
