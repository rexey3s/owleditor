package vn.edu.uit.owleditor.view.demo;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.logging.Log;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.core.owlapi.OWLDataRangeVisitorAdapter;
import vn.edu.uit.owleditor.data.hierarchy.OWLClassHierarchicalContainer;
import vn.edu.uit.owleditor.data.list.OWL2DatatypeContainer;
import vn.edu.uit.owleditor.data.property.OWLLiteralSource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.view.IndividualsSheet;
import vn.edu.uit.owleditor.view.component.InferredLabel;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/15/14.
 */
public class DemoUIFactory {
    private static final Log LOG = org.apache.commons.logging.LogFactory.getLog(DemoUIFactory.class);
    private final OWLEditorKit editorKit;
    private DemoWizardStep defaultStep;

    public DemoUIFactory(@Nonnull OWLEditorKit eKit) {
        this.editorKit = eKit;

    }

    public Set<WizardStep> getObjectPropertyAssertionCreator(OWLObjectProperty property, OWLNamedIndividual subject) {
        defaultStep = new DemoAbstractComponent(subject) {
            @Override
            public Component getContent() {
                Label warning = new Label("No component has been added because range of property" +
                        " may be out of bound");
                warning.addStyleName(ValoTheme.LABEL_H2);
                warning.addStyleName(ValoTheme.LABEL_FAILURE);
                addComponent(warning);
                setComponentAlignment(warning, Alignment.MIDDLE_CENTER);
                return this;
            }
            @Override
            protected void addAxiom() throws Exception { }

            @Override
            public String getCaption() {
                return OWLEditorKitImpl.render(property);
            }
        };
        final Set<WizardStep> retSteps = new HashSet<>();
        Collection<OWLClassExpression> ranges = EntitySearcher.getRanges(property, editorKit.getActiveOntology());
        if (ranges.size() > 0)
            ranges.forEach(range -> {
            retSteps.add(range.accept(new OWLClassExpressionVisitorEx<WizardStep>() {
                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLClass ce) {
                    return new ObjectPropertyAssertionCreator(property, subject);
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectIntersectionOf ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectUnionOf ce) {
                    return new ObjectPropertyAssertionCreator(property, subject);
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectComplementOf ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectSomeValuesFrom ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectAllValuesFrom ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectHasValue ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectMinCardinality ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectExactCardinality ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectMaxCardinality ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectHasSelf ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLObjectOneOf ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataSomeValuesFrom ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataAllValuesFrom ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataHasValue ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataMinCardinality ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataExactCardinality ce) {
                    return defaultStep;
                }

                @Nonnull
                @Override
                public WizardStep visit(@Nonnull OWLDataMaxCardinality ce) {
                    return defaultStep;
                }
            }
        ));});
        else {
            retSteps.add(new ObjectPropertyAssertionCreator(property, subject));
        }
        return retSteps;
    }

    public Set<WizardStep> getDataPropertyAssertionCreator(OWLDataProperty property, OWLNamedIndividual subject) {
        defaultStep = new DemoAbstractComponent(subject) {
            @Override
            public Component getContent() {
                Label warning = new Label("No component has been added because range of property" +
                        " may be out of bound");
                warning.addStyleName(ValoTheme.LABEL_H2);
                warning.addStyleName(ValoTheme.LABEL_FAILURE);
                addComponent(warning);
                setComponentAlignment(warning, Alignment.MIDDLE_CENTER);
                return this;
            }
            @Override
            protected void addAxiom() throws Exception { }

            @Override
            public String getCaption() {
                return OWLEditorKitImpl.render(property);
            }
        };
        final Set<WizardStep> retSteps = new HashSet<>();
        Collection<OWLDataRange> ranges = EntitySearcher.getRanges(property, editorKit.getActiveOntology());
        if (ranges.size() > 0)
            ranges.forEach(range -> {
                retSteps.add(range.accept(new OWLDataRangeVisitorEx<WizardStep>() {
                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDatatype node) {
                        return new DataAssertionCreator(property, subject,
                                (new DatatypeExtractor(property, editorKit.getActiveOntology()).getFilter()));
                    }

                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDataOneOf node) {
                        return new OWLDataOneOfRangeComponent(node, property, subject);
                    }

                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDataComplementOf node) {
                        return defaultStep;
                    }

                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDataIntersectionOf node) {
                        return defaultStep;
                    }

                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDataUnionOf node) {
                        return defaultStep;
                    }

                    @Nonnull
                    @Override
                    public WizardStep visit(@Nonnull OWLDatatypeRestriction node) {
                        return defaultStep;
                    }
                }));
            });
        else {
            retSteps.add(new DataAssertionCreator(property, subject,
                    (new DatatypeExtractor(property, editorKit.getActiveOntology()).getFilter())));
        }
        return retSteps;
    }

    public interface DemoWizardStep extends WizardStep {
    }

    public static class FinishStep extends VerticalLayout implements WizardStep {
        final OWLEditorKit editorKit;

        public FinishStep() {
            editorKit = OWLEditorUI.getEditorKit();

        }

        public void printNewType(@Nonnull OWLNamedIndividual individual) {
            editorKit.getReasoner().flush();
            Set<OWLClass> types = editorKit.getReasoner().getTypes(individual, true).getFlattened();
            for (OWLClass type : types) {
                addComponent(new InferredLabel(type, () -> editorKit.explain(editorKit.getOWLDataFactory()
                        .getOWLClassAssertionAxiom(type, individual))));
            }
            setSpacing(true);
            setMargin(true);
            setSizeFull();
        }

        @Override
        public String getCaption() {
            return "Types Of this Individual";
        }

        @Override
        public Component getContent() {
            return this;
        }

        @Override
        public boolean onAdvance() {
            return true;
        }

        @Override
        public boolean onBack() {
            return true;
        }
    }

    /*
    public static class ObjectPropertiesFilter implements Container.Filter {
        private final Collection<OWLObjectProperty> filter;

        public ObjectPropertiesFilter(Collection<OWLObjectProperty> filteringObjects) {
            filter = filteringObjects;
        }

        @Override
        public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {

            return true;
        }

        @Override
        public boolean appliesToProperty(Object o) {
            return false;
        }
    }

    public static class DataPropertiesFilter implements Container.Filter {
        private final Collection<OWLDataProperty> filter;

        public DataPropertiesFilter(Collection<OWLDataProperty> filteringObjects) {
            this.filter = filteringObjects;
        }

        @Override
        public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {

            return true;
        }

        @Override
        public boolean appliesToProperty(Object o) {
            return false;
        }
    }
    */
    public static class ClassFilter implements Container.Filter {

        private final Collection<OWLClass> filter;

        public ClassFilter(Collection<OWLClass> inputFilter) {
            filter = inputFilter;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            return filter.isEmpty() || filter.contains(itemId);
        }

        @Override
        public boolean appliesToProperty(Object o) {
            return false;
        }
    }

    public static class DatatypeFilter implements Container.Filter {
        private final Collection<OWLDatatype> filter;

        public DatatypeFilter(Collection<OWLDatatype> inputFilter) {
            filter = inputFilter;
        }

        @Override
        public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {
            if (filter.isEmpty()) return true;
            else
                return filter.contains(o);
        }

        @Override
        public boolean appliesToProperty(Object o) {
            return false;
        }
    }

    public static class ObjectPropertyRangeExtrator {
        private final Collection<OWLClass> filter = new HashSet<>();

        public ObjectPropertyRangeExtrator(OWLObjectProperty property, OWLOntology ontology) {
            Collection<OWLClassExpression> rawRanges = EntitySearcher.getRanges(property, ontology);
            rawRanges.forEach(ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                @Override
                public void visit(OWLClass owlClass) {
                    filter.add(owlClass);
                }

                @Override
                public void visit(OWLObjectUnionOf unionOf) {
                    unionOf.asDisjunctSet().forEach(ce -> {
                        if (ce instanceof OWLClass) {
                            filter.add((OWLClass) ce);
                            recursive(ontology, (OWLClass) ce, null);
                        }
                    });
                }
            }));
        }

        private void recursive(OWLOntology ontology,
                               OWLClass child, OWLClass parent) {

            filter.add(child);

            for (OWLClassExpression ce : EntitySearcher
                    .getSubClasses(child, ontology)) {

                for (OWLClass c : ce.getClassesInSignature()) {
                    recursive(ontology, c, child);
                }
            }
        }

        public Collection<OWLClass> getFilter() {
            return filter;
        }
    }

    public static class DatatypeExtractor {
        private final Collection<OWLDatatype> filter = new HashSet<>();
        private final OWLDataRangeVisitor visitor = new OWLDataRangeVisitorAdapter() {
            @Override
            public void visit(OWLDatatype node) {
                filter.add(node);
            }
        };
        public DatatypeExtractor(OWLDataProperty pe, OWLOntology ontology) {
            Collection<OWLDataRange> rawRanges = EntitySearcher.getRanges(pe, ontology);
            rawRanges.forEach(range -> range.accept(visitor));
        }

        public Collection<OWLDatatype> getFilter() {
            return filter;
        }
    }

    public static class ObjectPropertyAssertionCreator extends DemoAbstractComponent {
        private final OWLObjectProperty property;
        private final OWLEditorKit editorKit;
        private final IndividualsSheet.IndividualList individualList;
        private Tree owlClassTree = new Tree();


        public ObjectPropertyAssertionCreator(OWLObjectProperty property, OWLNamedIndividual subject) {
            super(subject);
            editorKit = OWLEditorUI.getEditorKit();
            this.property = property;
            individualList = new IndividualsSheet.IndividualList();
            OWLClassHierarchicalContainer container = OWLEditorUI.getEditorKit().getDataFactory().getOWLClassHierarchicalContainer();
            owlClassTree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            owlClassTree.setItemCaptionPropertyId(OWLEditorData.OWLClassName);
            Collection<OWLClass> filering = (new ObjectPropertyRangeExtrator(property,
                    editorKit.getActiveOntology())).getFilter();
            container.addContainerFilter(new ClassFilter(filering));

            owlClassTree.setContainerDataSource(container);
            owlClassTree.addValueChangeListener(valueChangedEvent -> {
                if (valueChangedEvent.getProperty().getValue() != null) {
                    if (valueChangedEvent.getProperty().getValue() instanceof OWLClass) {
                        individualList.setContainerDataSource(editorKit.getDataFactory()
                                .getOWLIndividualListContainer((OWLClass) valueChangedEvent.getProperty().getValue()));
                    }
                }
            });
            initialise();
        }

        private void initialise() {

            final Panel treePanel = new Panel();
            treePanel.setContent(owlClassTree);
            treePanel.setCaption("Ranges");
            treePanel.setHeight("100%");

            individualList.setHeight("100%");
            addComponents(treePanel, individualList);
            setExpandRatio(treePanel, 1);
            setExpandRatio(individualList, 1);
            setSizeFull();
            addStyleName(ValoTheme.LAYOUT_WELL);
        }

        public String getCaption() {
            return OWLEditorKitImpl.getShortForm(property);
        }

        @Override
        protected void addAxiom() throws Exception {
            OWLIndividualAxiom newAxiom = factory
                    .getOWLObjectPropertyAssertionAxiom(property,
                            subject, individualList.getSelectedProperty().getValue());

            ConfirmDialog.show(UI.getCurrent(),
                    "Are you sure about adding \"" + OWLEditorKitImpl.render(newAxiom) + "\"",
                    dialog -> {
                        if (dialog.isConfirmed()) {
                            OWLEditorEventBus.post(new OWLEditorEvent.IndividualAxiomAddEvent(newAxiom, subject));
                            dialog.close();
                        } else
                            dialog.close();
                    });
        }


    }

    public static class DataAssertionCreator extends DemoAbstractComponent {
        private final TextArea lexicalValue = new TextArea();
        private final ListSelect dataType = new ListSelect();
        private final OWLDataProperty property;


        /* Use DataPropertyAtomPatterns Collector here man */
        public DataAssertionCreator(OWLDataProperty property, OWLNamedIndividual subject, Collection<OWLDatatype> filtering) {
            super(subject);
            initialise();
            this.property = property;
            IndexedContainer container = new OWL2DatatypeContainer();

            container.addContainerFilter(new DatatypeFilter(filtering));
            dataType.setContainerDataSource(container);



        }

        public String getCaption() {
            return OWLEditorKitImpl.getShortForm(property);
        }

        private void initialise() {
            final HorizontalLayout wrapper = new HorizontalLayout();

            lexicalValue.setSizeFull();
            dataType.setSizeFull();
            wrapper.addComponents(lexicalValue, dataType);
            wrapper.setExpandRatio(lexicalValue, 1);
            wrapper.setExpandRatio(dataType, 1);
            wrapper.addStyleName(ValoTheme.LAYOUT_WELL);
            wrapper.setSizeFull();
            addComponent(wrapper);
            setSizeFull();
        }

        private OWLLiteral getOWLLiteral() {
            EditorUtils.checkNotNull(dataType.getValue(), "Please select a OWL2Datatype");
            return factory.getOWLLiteral(lexicalValue.getValue(), (OWLDatatype) dataType.getValue());
        }


        @Override
        protected void addAxiom() throws Exception {

            OWLIndividualAxiom newAxiom = factory
                    .getOWLDataPropertyAssertionAxiom(property, subject, getOWLLiteral());
            ConfirmDialog.show(UI.getCurrent(),
                    "Are you sure about adding \"" + OWLEditorKitImpl.render(newAxiom) + "\"",
                    dialog -> {
                        if (dialog.isConfirmed()) {
                            OWLEditorEventBus.post(new OWLEditorEvent.IndividualAxiomAddEvent(newAxiom, subject));
                            dialog.close();

                        } else
                            dialog.close();
                    });
        }


    }

    public static class OWLDataOneOfRangeComponent extends DemoAbstractComponent {
        private final OWLDataOneOf dataRange;
        private final OWLDataProperty property;
        private final ListSelect options = new ListSelect();
        private final OWLLiteralSource selectedProperty = new OWLLiteralSource();
        public OWLDataOneOfRangeComponent(OWLDataOneOf node, OWLDataProperty dp, OWLNamedIndividual individual) {
            super(individual);
            options.addContainerProperty("CAPTION", String.class, null);
            options.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
            options.setItemCaptionPropertyId("CAPTION");
            options.addValueChangeListener(change -> {
                Object value = change.getProperty().getValue();
                if (value instanceof OWLLiteral) {
                    selectedProperty.setValue((OWLLiteral) value);
                }
            });
            property = dp;
            dataRange = node;
            dataRange.getValues().forEach(literal -> {
                options.addItem(literal);
                options.getContainerProperty(literal, "CAPTION").setValue(literal.getLiteral());
            });

            options.setSizeFull();
            addComponent(options);
        }

        @Override
        protected void addAxiom() throws Exception {
            EditorUtils.checkNotNull(selectedProperty.getValue(), "Please select an option");
            OWLIndividualAxiom axiom = factory
                    .getOWLDataPropertyAssertionAxiom(property, subject, selectedProperty.getValue());
            OWLEditorEventBus.post(new OWLEditorEvent.IndividualAxiomAddEvent(axiom, subject));

        }

        @Override
        public String getCaption() {
            return OWLEditorKitImpl.getShortForm(property);
        }
    }
    private static abstract class DemoAbstractComponent extends VerticalLayout implements DemoWizardStep {
        protected final OWLNamedIndividual subject;
        protected final OWLDataFactory factory = OWLManager.getOWLDataFactory();
        public DemoAbstractComponent(OWLNamedIndividual subject) {
            this.subject = subject;
        }
        protected abstract void addAxiom() throws Exception;
        public abstract String getCaption();
        @Override
        public Component getContent() {
            return this;
        }
        @Override
        public boolean onAdvance() {
            try {
                addAxiom();
                return true;

            } catch (NullPointerException nullEx) {
                Notification.show(nullEx.getMessage(), Notification.Type.ERROR_MESSAGE);
            } catch (Exception ex) {
                Notification.show(ex.getMessage(), Notification.Type.HUMANIZED_MESSAGE);
            }
            return false;
        }
        @Override
        public boolean onBack() {
            return false;
        }
    }


}
