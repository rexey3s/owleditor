package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import vn.edu.uit.owleditor.data.property.*;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;
import vn.edu.uit.owleditor.event.OWLExpressionUpdateHandler;
import vn.edu.uit.owleditor.view.component.AbstractEditableOWLObjectLabel;
import vn.edu.uit.owleditor.view.component.AbstractExpressionPanel;
import vn.edu.uit.owleditor.view.component.InferredLabel;
import vn.edu.uit.owleditor.view.window.AddNamedIndividualWindow;
import vn.edu.uit.owleditor.view.window.buildAddClassExpressionWindow;
import vn.edu.uit.owleditor.view.window.buildEditClassExpressionWindow;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/16/2014.
 */
public class ClassExpressionPanelContainer extends AbstractPanelContainer {
    private static final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();
    private static final OWLClass nothing = OWLManager.getOWLDataFactory().getOWLNothing();

    private AbstractExpressionPanel equivPanel;

    private AbstractExpressionPanel indPanel;

    private AbstractExpressionPanel subClsOfPanel;

    private AbstractExpressionPanel disjointPanel;
    
    private Boolean reasonerStatus = false;

    @Override
    protected Component buildContent() {
        equivPanel = new ClassPanel("Equivalent To: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(
                                owlClassExpression ->
                                editorKit.getDataFactory().getEquivalentClassesAddEvent(
                                        dataSource.getValue(), owlClassExpression))
                );
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher
                        .getEquivalentClasses(dataSource.getValue(), editorKit.getActiveOntology());

                ces.remove(dataSource.getValue());
                ces.forEach(ce -> root.addComponent(new ClassLabel(
                                new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory().getEquivalentClassesRemoveEvent(
                                        dataSource.getValue(), ce),
                                modEx -> editorKit.getDataFactory().getEquivalentClassesModEvent(
                                        dataSource.getValue(), modEx, ce))
                ));
                if (reasonerStatus) {
                    addInferredExpressions();
                }
            }
            @Override
            public void addInferredExpressions() {
                Set<OWLClass> implicitClasses = editorKit.getReasoner()
                        .getEquivalentClasses(dataSource.getValue())
                        .getEntities();

                implicitClasses.remove(dataSource.getValue());
                    /* Obviously,  class is a thing, so remove it */
                implicitClasses.remove(thing);
                implicitClasses.forEach(ce -> root.addComponent(new InferredLabel(ce,
                                () -> editorKit.explain(editorKit.getOWLDataFactory()
                                        .getOWLEquivalentClassesAxiom(dataSource.getValue(), ce))))
                );
                
            } 
        };
        subClsOfPanel = new ClassPanel("SubClass Of: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(expression ->
                                editorKit.getDataFactory().getSubClassOfAddEvent(
                                        dataSource.getValue(), expression))
                );
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher
                        .getSuperClasses(dataSource.getValue(),
                                editorKit.getActiveOntology());
                for (OWLClassExpression ce : ces) {
                    root.addComponent(new ClassLabel(
                                    new OWLClassExpressionSource(ce),
                                    () -> editorKit.getDataFactory().getSubClassOfRemoveEvent(
                                            dataSource.getValue(), ce),
                                    modEx -> editorKit.getDataFactory().getSubClassOfModEvent(
                                            dataSource.getValue(), modEx, ce))
                    );
                }
                if (reasonerStatus) {

                    Set<OWLClass> implicitClasses = editorKit.getReasoner()
                            .getSuperClasses(dataSource.getValue(), false).getFlattened();
                    implicitClasses.remove(thing);
                    ces.stream().filter(ce -> (ce instanceof OWLClass)).forEach(ce -> implicitClasses.remove((OWLClass) ce));
                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(
                                    c, () -> editorKit.explain(editorKit.getOWLDataFactory()
                                    .getOWLSubClassOfAxiom(dataSource.getValue(), c))))
                    );
                }

            }
        };
        indPanel = new ClassPanel("Members: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new AddNamedIndividualWindow(owlNamedIndividual ->
                        editorKit.getDataFactory().getClassAssertionAddEvent(dataSource.getValue(), owlNamedIndividual)
                ));
            }

            @Override
            protected void initActionVIEW() {
                EntitySearcher.getIndividuals(dataSource.getValue(), editorKit.getActiveOntology())
                        .stream().filter(OWLIndividual::isNamed).forEach(ind ->
                                root.addComponent(new NamedIndividualPanelContainer.NamedIndividualLabel(
                                        new OWLNamedIndividualSource(ind.asOWLNamedIndividual()),
                                        () -> editorKit.getDataFactory().getClassAssertionRemoveEvent(
                                                dataSource.getValue(), ind.asOWLNamedIndividual())
                                ))
                );
                if (reasonerStatus) {
                    editorKit.getReasoner()
                            .getInstances(dataSource.getValue(), true)
                            .getFlattened().forEach(i -> root.addComponent(new InferredLabel(i,
                            () -> editorKit.explain(editorKit.getOWLDataFactory()
                                    .getOWLClassAssertionAxiom(dataSource.getValue(), i)))));
                }
            }
        };
        disjointPanel = new ClassPanel("Mutual Disjoint With: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(expression ->
                        editorKit.getDataFactory().getDisjointClassesAddEvent(dataSource.getValue(), expression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher.getDisjointClasses(dataSource.getValue(),
                        editorKit.getActiveOntology());
                ces.remove(dataSource.getValue());
                ces.forEach(ce -> root.addComponent(new ClassLabel(
                                new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory().getDisjointClassesRemoveEvent(
                                        dataSource.getValue(), ce),
                                modEx -> editorKit.getDataFactory().getDisjointClassesModEvent(
                                        dataSource.getValue(), modEx, ce))
                ));
                if (reasonerStatus) {
                    Set<OWLClass> implicitClasses = editorKit.getReasoner()
                            .getDisjointClasses(dataSource.getValue()).getFlattened();

                    ces.stream().filter(ce -> (ce instanceof OWLClass)).forEach(ce -> implicitClasses.remove((OWLClass) ce));
                    implicitClasses.remove(nothing);

                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(c,
                            () -> editorKit.explain(editorKit.getOWLDataFactory()
                                    .getOWLDisjointClassesAxiom(dataSource.getValue(), c)))));
                }
            }
        };
        descriptionPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(descriptionPanels);
        descriptionPanels.addComponent(createContentWrapper(equivPanel, "left-slot-panel"));
        descriptionPanels.addComponent(createContentWrapper(indPanel, "right-slot-panel"));
        descriptionPanels.addComponent(createContentWrapper(subClsOfPanel, "left-slot-panel"));
        descriptionPanels.addComponent(createContentWrapper(disjointPanel, "right-slot-panel"));
        return descriptionPanels;
    }

    @Override
    public void setPropertyDataSource(@Nonnull Property newDataSource) {
        reasonerStatus = editorKit.getReasonerStatus();
        if(reasonerStatus) editorKit.getReasoner().flush();
        equivPanel.setPropertyDataSource(newDataSource);
        subClsOfPanel.setPropertyDataSource(newDataSource);
        indPanel.setPropertyDataSource(newDataSource);
        disjointPanel.setPropertyDataSource(newDataSource);
    }

    private OWLAxiomVisitor addHelper(OWLLogicalAxiom addAxiom, OWLClass owner) {
        OWLClassSource dataSource = new OWLClassSource(owner);
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLClassAssertionAxiom axiom) {
                if (axiom.getIndividual().isNamed()) {
                    indPanel.addMoreExpression(
                            new NamedIndividualPanelContainer.NamedIndividualLabel(
                                    new OWLNamedIndividualSource(axiom.getIndividual().asOWLNamedIndividual()),
                                    () -> editorKit.getDataFactory().getClassAssertionRemoveEvent(owner, axiom.getIndividual().asOWLNamedIndividual())
                            ));
                }
            }

            @Override
            public void visit(OWLDisjointClassesAxiom axiom) {
                Set<OWLClassExpression> ces = axiom.getClassExpressions();
                ces.remove(owner);
                ces.forEach(ce -> disjointPanel.addMoreExpression(new ClassLabel(
                                new OWLClassExpressionSource(ce),
                                () -> new OWLEditorEvent.ClassAxiomRemoved(addAxiom, owner),
                                modEx -> editorKit.getDataFactory().getDisjointClassesModEvent(
                                        dataSource.getValue(), modEx, ce))
                ));

            }

            @Override
            public void visit(OWLEquivalentClassesAxiom axiom) {
                Set<OWLClassExpression> ces = axiom.getClassExpressions();
                ces.remove(owner);
                ces.forEach(ce -> equivPanel.addMoreExpression(new ClassLabel(
                                new OWLClassExpressionSource(ce),
                                () -> new OWLEditorEvent.ClassAxiomRemoved(addAxiom, owner),
                                modEx -> editorKit.getDataFactory().getEquivalentClassesModEvent(
                                        dataSource.getValue(), modEx, ce))
                ));
            }

            @Override
            public void visit(OWLSubClassOfAxiom axiom) {
                subClsOfPanel.addMoreExpression(new ClassLabel(
                                new OWLClassExpressionSource(axiom.getSuperClass()),
                                () -> new OWLEditorEvent.ClassAxiomRemoved(addAxiom, owner),
                                modEx -> editorKit.getDataFactory().getSubClassOfModEvent(
                                        dataSource.getValue(), modEx, axiom.getSuperClass()))
                );
            }
        };
    }

    private OWLAxiomVisitor removeHelper(OWLClass owner) {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLClassAssertionAxiom axiom) {
                if (axiom.getIndividual().isNamed()) {
                    indPanel.removeExpression(axiom.getIndividual().asOWLNamedIndividual());
                }
            }

            @Override
            public void visit(OWLDisjointClassesAxiom axiom) {
                Set<OWLClassExpression> ces = axiom.getClassExpressions();
                ces.remove(owner);
                ces.forEach(disjointPanel::removeExpression);
            }

            @Override
            public void visit(OWLEquivalentClassesAxiom axiom) {
                Set<OWLClassExpression> ces = axiom.getClassExpressions();
                ces.remove(owner);
                ces.forEach(equivPanel::removeExpression);
            }

            @Override
            public void visit(OWLSubClassOfAxiom axiom) {
                subClsOfPanel.removeExpression(axiom.getSuperClass());
            }
        };
    }
    @Subscribe
    public void afterReasonerToggle(OWLEditorEvent.ReasonerToggleEvent event) {
        if(event.getReasonerStatus()) {
            equivPanel.addInferredExpressions();
            subClsOfPanel.addInferredExpressions();
            indPanel.addInferredExpressions();
            disjointPanel.addInferredExpressions();
        }
        else {
            equivPanel.removeInferredExpressions();
            subClsOfPanel.removeInferredExpressions();
            indPanel.removeInferredExpressions();
            disjointPanel.removeInferredExpressions();
        }         
        
    }

    @Subscribe
    public void afterClassAxiomAdded(OWLEditorEvent.ClassAxiomAdded event) {
        ChangeApplied ok = editorKit.getModelManager()
                .applyChange(new AddAxiom(editorKit.getActiveOntology(), event.getAxiom()));
        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(addHelper(event.getAxiom(), event.getOwner()));
            Notification.show(
                    "Successfully created OWLClassExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show(
                    "Cannot create OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    public void afterClassAxiomRemoved(OWLEditorEvent.ClassAxiomRemoved event) {
        ChangeApplied ok = editorKit.getModelManager()
                .applyChange(new RemoveAxiom(editorKit.getActiveOntology(), event.getAxiom()));
        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(removeHelper(event.getOwner()));

            Notification.show(
                    "Successfully removed OWLClassExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show(
                    "Cannot remove OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    public void afterClassAxiomModified(OWLEditorEvent.ClassAxiomModified event) {
        ChangeApplied RemoveOk = editorKit.getModelManager()
                .applyChange(new RemoveAxiom(editorKit.getActiveOntology(), event.getOldAxiom()));
        ChangeApplied AddOk = editorKit.getModelManager()
                .applyChange(new AddAxiom(editorKit.getActiveOntology(), event.getNewAxiom()));

        if (RemoveOk == ChangeApplied.SUCCESSFULLY && AddOk == ChangeApplied.SUCCESSFULLY) {
            event.getOldAxiom().accept(removeHelper(event.getOwner()));
            event.getNewAxiom().accept(addHelper(event.getNewAxiom(), event.getOwner()));
            Notification.show("Successfully modified OWLClassExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Cannot modify OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }
    
    public static class ClassLabel extends AbstractEditableOWLObjectLabel<OWLClassExpression> {
        public ClassLabel(@Nonnull OWLObjectSource<OWLClassExpression> expressionSource,
                          @Nonnull OWLExpressionRemoveHandler removeExpression1,
                          @Nonnull OWLExpressionUpdateHandler<OWLClassExpression> modifyExpression1) {
            super(expressionSource, removeExpression1, modifyExpression1);
        }

        @Override
        public void initModifiedAction() {
            UI.getCurrent().addWindow(new buildEditClassExpressionWindow(expressionSource, modifyExpression));
        }
    }


    private abstract class ClassPanel extends AbstractExpressionPanel<OWLClass> {

        public ClassPanel(String caption) {
            super(caption);
        }

        @Override
        protected OWLLogicalEntitySource<OWLClass> initDataSource() {
            return new OWLClassSource();
        }
    }


}
