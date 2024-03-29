package vn.edu.uit.owleditor.view.demo;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
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
import vn.edu.uit.owleditor.data.property.OWLNamedIndividualSource;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.view.diagram.SuggestionGraph;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/17/14.
 */

public class DemoPanel extends VerticalLayout implements WizardProgressListener {
    private static final Logger LOG = LoggerFactory.getLogger(DemoPanel.class);
    private final OWLEditorKit editorKit;

    private final DemoUIFactory uiFactory;

    private final OWLNamedIndividualSource individualSource = new OWLNamedIndividualSource();

    private final OWLClassSource classSource = new OWLClassSource();

    private final SWRLAtomSearchByDefinedClass searcher;

    private final VerticalLayout body = new VerticalLayout();

    private final Component start;

    private SuggestionGraph graph;

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
        Component graphWrapper = createSuggestionGraphWrapper(graph);
        addComponent(graphWrapper);
        addComponent(body);
        setSpacing(true);
        setSizeFull();
    }

    private Component createSuggestionGraphWrapper(final Component graph) {
        final HorizontalLayout container = new HorizontalLayout();
        container.addStyleName("suggestion-graph-container");
        graph.setSizeFull();
        container.addComponent(graph);
        container.addStyleName(ValoTheme.LAYOUT_WELL);
        container.setComponentAlignment(graph, Alignment.MIDDLE_CENTER);
        container.setSizeFull();
        setSizeFull();
        return container;
        
    }


    private Component buildStartButton() {
        final VerticalLayout wrapper = new VerticalLayout();
        final Button start = new Button("Suggestions");
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
        wz.addStep(new DemoUIFactory.FinishStep());
        return wz;
    }


    private Button.ClickListener initStartClickListener() {
        return click -> {

            if (editorKit.getReasonerStatus()) {
                try {
                    EditorUtils.checkNotNull(classSource.getValue(), "Please choose a Class");
                    EditorUtils.checkNotNull(individualSource.getValue(), "Please choose an Individual");

                    body.removeComponent(start);
                    body.addComponent(buildWizard(classSource.getValue(), individualSource.getValue()));
                } catch (NullPointerException ex) {
                    Notification.show(ex.getMessage(), Notification.Type.WARNING_MESSAGE);
                }
            } else Notification.show("Please start reasoner");

        };
    }

    /*
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
    */
    private String sf(OWLEntity entity) {
        return OWLEditorKitImpl.getShortForm(entity);
    }

    public void setSuggestionSource(OWLClass cls) {
        if (cls != null) {
            classSource.setValue(cls);
            graph.setData(AtomSearcher.getSuggestion(cls, editorKit.getSWRLActiveOntology()).toString());
            
        }
    }
    
    public void setIndividualSource(OWLNamedIndividual individual) {
        if(individual != null) {
            individualSource.setValue(individual);
        }
        
    }
    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        if (event.getActivatedStep() instanceof DemoUIFactory.FinishStep) {
            ((DemoUIFactory.FinishStep) event.getActivatedStep())
                    .printNewType(individualSource.getValue());

        }
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        body.removeComponent(event.getWizard());
        body.addComponent(start);    

    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        ConfirmDialog.show(UI.getCurrent(), "Are you sure", dialog -> {
            if (dialog.isConfirmed()) {
                body.removeComponent(event.getWizard());
                body.addComponent(start);
                Notification.show("Suggestion Generation Cancelled", Notification.Type.TRAY_NOTIFICATION);
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
