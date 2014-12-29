package vn.edu.uit.owleditor.view.demo;

import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.*;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.core.swrlapi.AtomSearcher;
import vn.edu.uit.owleditor.core.swrlapi.SWRLAtomSearchByDefinedClass;
import vn.edu.uit.owleditor.data.property.OWLClassSource;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.view.diagram.SuggestionGraph;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/17/14.
 */

public class DemoPanel extends VerticalLayout implements Property.Viewer, WizardProgressListener {
    private static final Logger LOG = LoggerFactory.getLogger(DemoPanel.class);
    private final OWLEditorKit editorKit;

    private final DemoUIFactory uiFactory;

    private final OWLClassSource dataSource = new OWLClassSource();

    private final SWRLAtomSearchByDefinedClass searcher;

    private final VerticalLayout body = new VerticalLayout();

    private final Component start;

    private SuggestionGraph graph;
    private Collection<OWLNamedIndividual> individualsToClassify;

    public DemoPanel() {
        editorKit = OWLEditorUI.getEditorKit();
        uiFactory = new DemoUIFactory(editorKit);
        searcher = new SWRLAtomSearchByDefinedClass(editorKit);
        start = buildStartButton();
        graph = new SuggestionGraph();
        initialise();

        
    }


    private void initialise() {

        body.addStyleName(ValoTheme.LAYOUT_WELL);
        body.addComponent(start);
        body.setHeight("100%");
        addComponent(createSuggestionGraphWrapper(graph));
        addComponent(body);
        setSpacing(true);
        setSizeFull();
    }

    private Component createSuggestionGraphWrapper(final Component graph) {
        final HorizontalLayout container = new HorizontalLayout();
        addStyleName("suggestion-graph-container");
        container.addComponent(graph);
        container.addStyleName(ValoTheme.LAYOUT_WELL);
        container.setComponentAlignment(graph, Alignment.MIDDLE_CENTER);
        container.setSizeFull();
        setSizeFull();
        return container;
        
    }


    private Component buildStartButton() {
        final VerticalLayout wrapper = new VerticalLayout();
        final Button start = new Button("Start recommendation");
        start.addStyleName(ValoTheme.BUTTON_HUGE);
        start.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        start.addClickListener(initStartClickListener());
        wrapper.addComponent(start);
        wrapper.setComponentAlignment(start, Alignment.MIDDLE_CENTER);
        wrapper.setSizeFull();
        return wrapper;
    }

    private Component buildWizard(OWLClass owlClass, OWLNamedIndividual individual) {
        final DemoWizard wz = new DemoWizard(individual);
        wz.addListener(this);
        searcher.getDataPropertiesByDefinedClass(owlClass)
                .stream().filter(dp -> (dp instanceof OWLDataProperty))
                .forEach(dp -> uiFactory.getDataPropertyAssertionCreator((OWLDataProperty) dp, individual)
                        .forEach(step -> wz.addStep(step, sf((OWLDataProperty) dp))));

        searcher.getObjectPropertiesByDefinedClass(owlClass)
                .stream().filter(op -> (op instanceof OWLObjectProperty))
                .forEach(op -> uiFactory.getObjectPropertyAssertionCreator(
                        (OWLObjectProperty) op, individual)
                        .forEach(step -> wz.addStep(step, sf((OWLObjectProperty) op))));
        return wz;
    }


    private Button.ClickListener initStartClickListener() {
        return clicked -> {
            try {
                EditorUtils.checkNotNull(dataSource.getValue(),
                        "Please choose a Domain (Class) to generate recommendation");
                EditorUtils.checkNotNull(editorKit.getReasoner(), "Please turn on Reasoning");
                Collection<OWLIndividual> individuals = EntitySearcher
                        .getIndividuals(dataSource.getValue(), editorKit.getActiveOntology());
                if (individuals.isEmpty()) {
                    Notification.show(sf(dataSource.getValue()) + " does not have any individuals to classify",
                            Notification.Type.WARNING_MESSAGE);
                } else
//                PopulateClassWithDefaultIndividual();
                ConfirmDialog.show(UI.getCurrent(),
                        "Generate recommendation for " + OWLEditorKitImpl.getShortForm(dataSource.getValue()),
                        dialog -> {
                            individualsToClassify = new HashSet<>();
                            if (dialog.isConfirmed()) {
                                individuals.stream().filter(OWLIndividual::isNamed)
                                        .forEach(i -> individualsToClassify.add((OWLNamedIndividual) i));
                                if (individualsToClassify.iterator().hasNext()) {
                                    OWLNamedIndividual i = individualsToClassify.iterator().next();
                                    body.removeComponent(start);
                                    body.addComponent(buildWizard(dataSource.getValue(), i));
                                }

                            } else
                                dialog.close();
                        });

            } catch (NullPointerException ex) {
                Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
            }

        };
    }


    private void PopulateClassWithDefaultIndividual() {
        editorKit.getActiveOntology().getClassesInSignature().forEach(clz -> {
            if (!checkHadDefaultIndividual(clz)) {
                Boolean ok = addDefaultIndividual(clz);
                System.out.println("Populate individual for " + sf(clz) + " -> Status: " + ok);
            }
        });
    }


    private Boolean addDefaultIndividual(OWLClass owlClass) {
        final String nameInd = sf(owlClass) + "_" + "DefaultIndividual";
        OWLNamedIndividual individual = editorKit.getOWLDataFactory()
                .getOWLNamedIndividual(":" + nameInd, editorKit.getPrefixManager());
        OWLDeclarationAxiom axiom1 = editorKit.getOWLDataFactory()
                .getOWLDeclarationAxiom(individual);
        OWLClassAssertionAxiom axiom2 = editorKit.getOWLDataFactory()
                .getOWLClassAssertionAxiom(owlClass, individual);

        ChangeApplied ok1 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), axiom1);
        ChangeApplied ok2 = editorKit.getModelManager().addAxiom(editorKit.getActiveOntology(), axiom2);

        return ChangeApplied.SUCCESSFULLY == ok1 && ChangeApplied.SUCCESSFULLY == ok2;
    }

    private Boolean checkHadDefaultIndividual(OWLClass owlClass) {
        final String nameInd = sf(owlClass) + "_" + "DefaultIndividual";
        OWLNamedIndividual individual = editorKit
                .getOWLDataFactory().getOWLNamedIndividual(":" + nameInd, editorKit.getPrefixManager());

        return editorKit.getActiveOntology().isDeclared(individual);
    }

    private String sf(OWLEntity entity) {
        return OWLEditorKitImpl.getShortForm(entity);
    }

    @Override
    public Property getPropertyDataSource() {
        return dataSource;
    }

    @Override
    public void setPropertyDataSource(Property property) {
        if (property.getValue() != null) {
            dataSource.setValue((OWLClass) property.getValue());
            Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> multimap = AtomSearcher
                    .getObjectPropertySuggestion(dataSource.getValue(), editorKit.getSWRLActiveOntology());
            JsonObject root = new JsonObject();
            JsonArray nodes = new JsonArray();
            JsonArray edges = new JsonArray();

            JsonObject classNode = new JsonObject();
            final String className = OWLEditorKitImpl.getShortForm(dataSource.getValue());
            classNode.addProperty("id", className);
            classNode.addProperty("label", className);
            nodes.add(classNode);

            multimap.entries().forEach(entry -> {

                final String edgeLabel = OWLEditorKitImpl.getShortForm(entry.getKey());
                entry.getValue().entries().forEach(entry2 -> {
                    entry2.getKey().forEach(clz -> {
                        final JsonObject node = new JsonObject();
                        final JsonObject edge = new JsonObject();
                        final String clzName = OWLEditorKitImpl.getShortForm(clz);
                        node.addProperty("id", clzName);
                        node.addProperty("label", clzName);
                        nodes.add(node);
                        edge.addProperty("start", className);
                        edge.addProperty("end", clzName);
                        edge.addProperty("label", edgeLabel);
                        edges.add(edge);
                    });
                });
            });
            root.add("nodes", nodes);
            root.add("edges", edges);
            graph.setData(root.toString());
            
            LOG.info(AtomSearcher.getDataProperySuggestion(dataSource.getValue(), editorKit.getSWRLActiveOntology()).toString());
//            LOG.info();
        }
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        if (event.getActivatedStep() instanceof DemoUIFactory.DemoWizardStep) {
            final DemoUIFactory.DemoWizardStep demoWizardStep =
                    (DemoUIFactory.DemoWizardStep) event.getActivatedStep();
            StringBuilder sb = new StringBuilder();

        }
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {

    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        individualsToClassify.remove(((DemoWizard) event.getWizard()).getOwner());
        body.removeComponent(event.getWizard());
        //            body.removeComponent(start);

        if (individualsToClassify.iterator().hasNext()) {
            OWLNamedIndividual i = individualsToClassify.iterator().next();

            body.addComponent(buildWizard(dataSource.getValue(), i));
            body.removeComponent(start);
        } else {
            Notification.show("Classification completed");
        }

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        ConfirmDialog.show(UI.getCurrent(), "Are you sure", dialog -> {
            if (dialog.isConfirmed()) {
                body.removeComponent(event.getWizard());
                body.addComponent(start);
                Notification.show("Recommendation Generation Cancelled", Notification.Type.TRAY_NOTIFICATION);
                dialog.close();
            } else
                dialog.close();
        });
    }

    public static class DemoWizard extends Wizard {
        private final OWLNamedIndividual owner;

        public DemoWizard(@Nonnull OWLNamedIndividual owner) {
            this.owner = owner;
        }

        public OWLNamedIndividual getOwner() {
            return owner;
        }
    }
}
