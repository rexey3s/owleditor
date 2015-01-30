package vn.edu.uit.owleditor.view;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.property.OWLNamedIndividualSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEntityActionHandler;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.utils.converter.StringToNamedIndividualConverter;
import vn.edu.uit.owleditor.utils.validator.IRIValidatorImpl;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;
import vn.edu.uit.owleditor.view.panel.NamedIndividualPanelContainer;
import vn.edu.uit.owleditor.view.window.AbstractAddOWLObjectWindow;

import java.util.List;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/3/2014.
 */
@VaadinUIScope
@VaadinView(name = IndividualsSheet.NAME)
public class IndividualsSheet extends HorizontalLayout implements View {

    public static final String NAME = "Individuals";
    
    private ClassHierarchicalPanel hierarchy;

    private IndividualList individualsList;

    private NamedIndividualPanelContainer indPanels;

    private OWLEditorKit editorKit;

    public IndividualsSheet() {
        editorKit = OWLEditorUI.getEditorKit();
        initialise();

    }

    private void initialise() {
        hierarchy = new ClassHierarchicalPanel();
        individualsList = new IndividualList();
        indPanels = new NamedIndividualPanelContainer();

        hierarchy.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                OWLClass clz = hierarchy.getSelectedItem().getValue();
                if (clz.isOWLThing()) {
                    individualsList.setContainerDataSource(editorKit
                            .getDataFactory()
                            .getOWLIndividualListContainer());
                } else
                    individualsList.setContainerDataSource(editorKit
                            .getDataFactory()
                            .getOWLIndividualListContainer(clz));

            }
        });
        individualsList.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                indPanels.setPropertyDataSource(new
                        OWLNamedIndividualSource((OWLNamedIndividual) event.getProperty().getValue()));
            }
        });
        hierarchy.setImmediate(true);
        individualsList.setImmediate(true);
        VerticalLayout listWrapper = new VerticalLayout();
        listWrapper.addComponents(hierarchy, individualsList);
        listWrapper.setSpacing(true);
        listWrapper.setSizeFull();

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("dashboard-tabsheet");
        tabs.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        tabs.addTab(indPanels, "Individual Description");
        tabs.addTab(new Panel(), "Annotation");

        VerticalLayout ver = new VerticalLayout();
        ver.setSpacing(true);
        ver.addComponent(tabs);
        ver.setExpandRatio(tabs, 1.0f);
        ver.setSizeFull();


        addComponents(listWrapper, ver);
        setExpandRatio(listWrapper, 1.0f);
        setExpandRatio(ver, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @VaadinUIScope
    @VaadinComponent
    public static class IndividualList extends VerticalLayout implements
            OWLEntityActionHandler<OWLEditorEvent.IndividualAddEvent, OWLEditorEvent.IndividualAddEvent, OWLEditorEvent.IndividualRemoveEvent>,
            Container.Viewer {

        private final ListSelect list = new ListSelect();


        private final OWLNamedIndividualSource selectSource = new OWLNamedIndividualSource();

        private final OWLEditorKit editorKit;

        public IndividualList() {
            buildComponents();
            editorKit = OWLEditorUI.getEditorKit();
            list.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            list.setItemCaptionPropertyId(OWLEditorData.OWLNamedIndividualName);
            list.addValueChangeListener(event -> {
                if (event.getProperty().getValue() != null) {
                    selectSource.setValue((OWLNamedIndividual) event.getProperty().getValue());
                }
            });
            list.setNullSelectionAllowed(false);
        }

        private void buildComponents() {

            Panel panel = new Panel();
            list.setSizeFull();
            panel.setContent(list);
            panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
            panel.setSizeFull();
            panel.addStyleName("listselect-panel");
            HorizontalLayout toolbar = new HorizontalLayout();
            toolbar.setWidth("100%");
            toolbar.addStyleName("toolbar");
            Label caption = new Label("Individuals");
            caption.addStyleName(ValoTheme.LABEL_H4);
            caption.addStyleName(ValoTheme.LABEL_COLORED);
            caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            caption.setSizeUndefined();
            toolbar.addComponents(caption, buildMenuBar());
            toolbar.setExpandRatio(caption, 1.0f);
            toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
            addStyleName(ValoTheme.LAYOUT_CARD);
            addStyleName("individual-list");
            addComponent(toolbar);
            addComponent(panel);
            setExpandRatio(panel, 1.0f);
            setSizeFull();
        }


        private Component buildMenuBar() {

            final MenuBar tools = new MenuBar();
            tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
            MenuBar.MenuItem act = tools.addItem("", FontAwesome.COG, null);
            act.addItem("Add Individual", clicked ->
                            UI.getCurrent().addWindow(new buildAddIndividualWindow(this))
            );
            act.addItem("Remove Individual", clicked -> ConfirmDialog.show(UI.getCurrent(),
                    "Are you sure ?", dialog -> {
                        if (dialog.isConfirmed()) {
                            this.handleRemoveEntityEvent(new OWLEditorEvent.IndividualRemoveEvent(
                                    getSelectedProperty().getValue()
                            ));
                        } else {
                            dialog.close();
                        }
                    }));
            return tools;
        }


        public void addValueChangeListener(Property.ValueChangeListener valueChangeListener) {
            list.addValueChangeListener(valueChangeListener);
        }     
       
        public void removeValueChangeListener(Property.ValueChangeListener valueChangeListener) {
            list.removeValueChangeListener(valueChangeListener);
        }


        @Override
        public Container getContainerDataSource() {
            return list.getContainerDataSource();
        }

        @Override
        public void setContainerDataSource(Container container) {
            list.setContainerDataSource(container);
        }

        public OWLNamedIndividualSource getSelectedProperty() throws NullPointerException {
            EditorUtils.checkNotNull(selectSource.getValue(), "Please select an Individual");
            return selectSource;
        }

        @Override
        public void handleAddSubEntityEvent(OWLEditorEvent.IndividualAddEvent event) {

        }

        @Override
        public void handleAddSiblingEntityEvent(OWLEditorEvent.IndividualAddEvent event) {
            OWLDeclarationAxiom declareIndividual = editorKit.getOWLDataFactory().getOWLDeclarationAxiom(event.getIndividual());
            ChangeApplied ok = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), declareIndividual);
            if(ok == ChangeApplied.SUCCESSFULLY) {
                list.addItem(event.getIndividual());
                list.getContainerProperty(event.getIndividual(), OWLEditorData.OWLNamedIndividualName)
                        .setValue(OWLEditorKitImpl.getShortForm(event.getIndividual()));
                Notification.show("Successfully created individual", Notification.Type.TRAY_NOTIFICATION);
            }
            else
                Notification.show("Cannot add individual", Notification.Type.WARNING_MESSAGE);
        }

        @Override
        public void handleRemoveEntityEvent(OWLEditorEvent.IndividualRemoveEvent event) {
            event.getIndividual().accept(editorKit.getEntityRemover());
            List<OWLOntologyChange> changes = editorKit
                    .getModelManager()
                    .applyChanges(editorKit.getEntityRemover().getChanges());
            if (changes.size() > 0) {
                list.removeItem(event.getIndividual());
                Notification.show("Successfully removed individual", Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show("Cannot remove individual", Notification.Type.WARNING_MESSAGE);

            }
            editorKit.getEntityRemover().reset();
        }

        public class buildAddIndividualWindow extends AbstractAddOWLObjectWindow<OWLNamedIndividual> {
            private final OWLEntityActionHandler handler;

            public buildAddIndividualWindow(OWLEntityActionHandler handler) {
                super();
                this.handler = handler;
                nameField.setImmediate(false);
                nameField.setCaption("Individual");
                nameField.setConverter(new StringToNamedIndividualConverter(editorKit));
                nameField.addValidator(new IRIValidatorImpl<OWLNamedIndividual>(editorKit));
            }
            @Override
            protected Button.ClickListener getSaveListener() {
                return click -> {
                    try {
                        nameField.validate();
                        handler.handleAddSiblingEntityEvent(new OWLEditorEvent.IndividualAddEvent((OWLNamedIndividual) nameField.getConvertedValue()));
                        close();
                    }
                    catch (Validator.InvalidValueException ex) {
                        Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
                    }
                };
            }
        }
    }

}
