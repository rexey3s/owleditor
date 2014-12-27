package vn.edu.uit.owleditor.view.panel;

import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.owlapi.OWLPropertyExpressionVisitorAdapter;
import vn.edu.uit.owleditor.data.property.*;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;
import vn.edu.uit.owleditor.view.component.AbstractExpressionPanel;
import vn.edu.uit.owleditor.view.component.AbstractNonEditableOWLObjectLabel;
import vn.edu.uit.owleditor.view.component.InferredLabel;
import vn.edu.uit.owleditor.view.component.OWLIndividualAxiomLabel;
import vn.edu.uit.owleditor.view.window.AddNamedIndividualWindow;
import vn.edu.uit.owleditor.view.window.buildAddClassExpressionWindow;
import vn.edu.uit.owleditor.view.window.buildAddDataPropertyAssertionEditorWindow;
import vn.edu.uit.owleditor.view.window.buildObjectPropertyAssertionEditorWindow;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/3/2014.
 */
public class NamedIndividualPanelContainer extends AbstractPanelContainer {

    private AbstractExpressionPanel typePn;

    private AbstractExpressionPanel samePn;

    private AbstractExpressionPanel diffPn;

    private AbstractExpressionPanel dataAssertPn;

    private AbstractExpressionPanel objAssertPn;

    private AbstractExpressionPanel negDataAssertPn;

    private AbstractExpressionPanel negObjAssertPn;


    public NamedIndividualPanelContainer(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    protected Component buildContent() {
        descriptionPanels = new CssLayout();
        descriptionPanels.addStyleName("dashboard-panels");

        typePn = new IndividualPanel("Types: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(
                        editorKit,
                        owlClassExpression -> editorKit.getDataFactory()
                                .getIndividualTypesAxiomAddEvent(dataSource.getValue(), owlClassExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher
                        .getTypes(dataSource.getValue(), editorKit.getActiveOntology());
                ces.forEach(ce -> root.addComponent(new ClassExpressionPanelContainer.ClassLabel(
                                editorKit,
                                new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory()
                                        .getIndividualTypesAxiomRemoveEvent(dataSource.getValue(), ce),
                                newEx -> editorKit.getDataFactory()
                                        .getIndividualTypesAxiomModEvent(dataSource.getValue(), newEx, ce)
                        ))
                );
                if (editorKit.getReasonerStatus()) {
                    Set<OWLClass> implicitClasses = editorKit.getReasoner()
                            .getTypes(dataSource.getValue(), false).getFlattened();
                    ces.stream().filter(ce -> (ce instanceof OWLClass)).forEach(ce -> implicitClasses.remove((OWLClass) ce));
                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(c,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLClassAssertionAxiom(c, dataSource.getValue()))
                            ))
                    );
                }
            }
        };

        samePn = new IndividualPanel("Same Individuals: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new AddNamedIndividualWindow(
                        owlNamedIndividual -> editorKit.getDataFactory()
                                .getSamesIndividualsAxiomAddEvent(dataSource.getValue(), owlNamedIndividual)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLNamedIndividual> individuals = EntitySearcher
                        .getSameIndividuals(dataSource.getValue(), editorKit.getActiveOntology())
                        .stream().filter(OWLIndividual::isNamed)
                        .map(i -> (OWLNamedIndividual) i)
                        .collect(Collectors.toCollection(ArrayList::new));

                individuals.forEach(i -> root
                        .addComponent(new
                                NamedIndividualLabel(new
                                OWLNamedIndividualSource(
                                i.asOWLNamedIndividual()),
                                () -> editorKit.getDataFactory()
                                        .getSamesIndividualsAxiomRemoveEvent(dataSource
                                                .getValue(), i.asOWLNamedIndividual()))));
                
                if (editorKit.getReasonerStatus()) {
                    Set<OWLNamedIndividual> implicitIndividuals = editorKit.getReasoner()
                            .getSameIndividuals(dataSource.getValue()).getEntities();

                    implicitIndividuals.removeAll(individuals);
                    implicitIndividuals.forEach(i -> root
                            .addComponent(new
                                    InferredLabel(i, () -> editorKit
                                    .explain(editorKit.getOWLDataFactory()
                                            .getOWLSameIndividualAxiom(i, dataSource.getValue())))));
                }
            }
        };

        diffPn = new IndividualPanel("Different Individuals: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new AddNamedIndividualWindow(
                        owlNamedIndividual -> editorKit.getDataFactory()
                                .getDifferentIndividualsAxiomAddEvent(dataSource.getValue(), owlNamedIndividual)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLNamedIndividual> individuals = EntitySearcher
                        .getDifferentIndividuals(dataSource.getValue(), editorKit.getActiveOntology())
                        .stream().filter(OWLIndividual::isNamed)
                        .map(i -> (OWLNamedIndividual) i)
                        .collect(Collectors.toCollection(ArrayList::new));

                individuals.stream().filter(OWLIndividual::isNamed)
                        .forEach(i -> root.addComponent(new NamedIndividualLabel(
                                        new OWLNamedIndividualSource(i.asOWLNamedIndividual()),
                                        () -> editorKit.getDataFactory()
                                                .getDifferentIndividualsAxiomRemoveEvent(
                                                dataSource.getValue(), i.asOWLNamedIndividual()))
                        ));
                if (editorKit.getReasonerStatus()) {
                    Set<OWLNamedIndividual> implicitIndividuals = editorKit.getReasoner()
                            .getDifferentIndividuals(dataSource.getValue()).getFlattened();
                    implicitIndividuals.removeAll(individuals);
                    implicitIndividuals.forEach(e -> root.addComponent(new InferredLabel(e,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLDifferentIndividualsAxiom(e, dataSource.getValue()))
                            ))
                    );
                }
            }
        };
        objAssertPn = new IndividualPanel("Object Property Assertions: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildObjectPropertyAssertionEditorWindow(
                        editorKit, (expression, restriction) ->
                        editorKit.getDataFactory()
                                .getIndividualObjectPropertyAssertionAxiomAddEvent(dataSource.getValue(),
                                        expression, restriction)));
            }

            @Override
            protected void initActionVIEW() {
                Multimap<OWLObjectPropertyExpression, OWLIndividual> map = EntitySearcher
                        .getObjectPropertyValues(dataSource.getValue(), editorKit.getActiveOntology());
                map.entries().forEach(entry -> {
                    OWLIndividualAxiom axiom = editorKit.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(
                            entry.getKey(),
                            dataSource.getValue(),
                            entry.getValue()
                    );
                    root.addComponent(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                            new OWLEditorEvent.IndividualAxiomRemoved(axiom, dataSource.getValue())
                    ));

                });
                if(editorKit.getReasonerStatus()) {
                    editorKit.getActiveOntology().getDataPropertiesInSignature()
                            .forEach(dp -> dp.accept(new OWLPropertyExpressionVisitorAdapter() {
                                public void visit(OWLObjectProperty property) {
                                    Set<OWLNamedIndividual> individuals = editorKit.getReasoner()
                                            .getObjectPropertyValues(dataSource.getValue(), property).getFlattened();

                                    individuals.forEach(individual -> {
                                        if(!map.containsEntry(property, individual)) {
                                            OWLObjectPropertyAssertionAxiom axiom = editorKit.getOWLDataFactory()
                                                    .getOWLObjectPropertyAssertionAxiom(
                                                            property,
                                                            dataSource.getValue(), individual);

                                            root.addComponent(new InferredLabel(axiom.getProperty(),
                                                    () -> editorKit.explain(axiom)));
                                        }
                                    });
                                }
                            }));
                }
            }
        };
        dataAssertPn = new IndividualPanel("Data Property Assertions: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddDataPropertyAssertionEditorWindow(
                        editorKit, (expression, restriction) ->
                        editorKit.getDataFactory()
                                .getIndividualDataAssertionAxiomAddEvent(dataSource.getValue(),
                                        expression, restriction)));
            }

            @Override
            protected void initActionVIEW() {
                Multimap<OWLDataPropertyExpression, OWLLiteral> map = EntitySearcher
                        .getDataPropertyValues(dataSource.getValue(), editorKit.getActiveOntology());
                map.entries().forEach(entry -> {
                    OWLIndividualAxiom axiom = editorKit.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(
                            entry.getKey(),
                            dataSource.getValue(),
                            entry.getValue()
                    );
                    root.addComponent(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                            new OWLEditorEvent.IndividualAxiomRemoved(axiom, dataSource.getValue())
                    ));

                });
                if(editorKit.getReasonerStatus()) {
                    editorKit.getActiveOntology().getDataPropertiesInSignature()
                        .forEach(dp -> dp.accept(new OWLPropertyExpressionVisitorAdapter() {
                            public void visit(OWLDataProperty property) {
                                Set<OWLLiteral> literals = editorKit.getReasoner()
                                        .getDataPropertyValues(dataSource.getValue(), property);
                                literals.forEach(literal -> {
                                    if(!map.containsEntry(property, literal)) {
                                        OWLDataPropertyAssertionAxiom axiom = editorKit.getOWLDataFactory()
                                                .getOWLDataPropertyAssertionAxiom(
                                                        property,
                                                        dataSource.getValue(), literal);
                                        root.addComponent(new InferredLabel(axiom.getProperty(),
                                                () -> editorKit.explain(axiom)));
                                    }
                                });
                            }
                    }));
                }
            }
        };
        negObjAssertPn = new IndividualPanel("Negative Object Property Assertions: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildObjectPropertyAssertionEditorWindow(
                        editorKit, (expression, restriction) ->
                        editorKit.getDataFactory()
                                .getIndividualNegativeObjectPropertyAssertionAxiomAddEvent(dataSource.getValue(),
                                        expression, restriction)));
            }

            @Override
            protected void initActionVIEW() {
                Multimap<OWLObjectPropertyExpression, OWLIndividual> map = EntitySearcher
                        .getNegativeObjectPropertyValues(dataSource.getValue(), editorKit.getActiveOntology());
                map.entries().forEach(entry -> {
                    OWLIndividualAxiom axiom = editorKit.getOWLDataFactory().getOWLNegativeObjectPropertyAssertionAxiom(
                            entry.getKey(),
                            dataSource.getValue(),
                            entry.getValue()
                    );
                    root.addComponent(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                            new OWLEditorEvent.IndividualAxiomRemoved(axiom, dataSource.getValue())
                    ));

                });
            }
        };

        negDataAssertPn = new IndividualPanel("Negative Data Property Assertions: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddDataPropertyAssertionEditorWindow(
                        editorKit, (expression, restriction) ->
                        editorKit.getDataFactory()
                                .getIndividualNegativeDataAssertionAxiomAddEvent(dataSource.getValue(),
                                        expression, restriction)));
            }

            @Override
            protected void initActionVIEW() {
                Multimap<OWLDataPropertyExpression, OWLLiteral> map = EntitySearcher
                        .getNegativeDataPropertyValues(dataSource.getValue(), editorKit.getActiveOntology());
                map.entries().forEach(entry -> {
                    OWLIndividualAxiom axiom = editorKit.getOWLDataFactory().getOWLNegativeDataPropertyAssertionAxiom(
                            entry.getKey(),
                            dataSource.getValue(),
                            entry.getValue()
                    );
                    root.addComponent(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                            new OWLEditorEvent.IndividualAxiomRemoved(axiom, dataSource.getValue())
                    ));

                });
            }
        };


        Responsive.makeResponsive(descriptionPanels);

        descriptionPanels.addComponent(
                createContentWrapper(typePn, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(samePn, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(diffPn, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(objAssertPn, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(dataAssertPn, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(negObjAssertPn, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(
                createContentWrapper(negDataAssertPn, "left-slot-panel", "panel-mini-slot"));


        return descriptionPanels;

    }

    @Override
    public void setPropertyDataSource(@Nonnull Property property) {
        typePn.setPropertyDataSource(property);
        samePn.setPropertyDataSource(property);
        diffPn.setPropertyDataSource(property);
        dataAssertPn.setPropertyDataSource(property);
        objAssertPn.setPropertyDataSource(property);
        negDataAssertPn.setPropertyDataSource(property);
        negObjAssertPn.setPropertyDataSource(property);
    }

    private OWLAxiomVisitor addHelper(OWLNamedIndividual owner) {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLClassAssertionAxiom axiom) {
                typePn.addMoreExpression(new ClassExpressionPanelContainer.ClassLabel(
                        editorKit,
                        new OWLClassExpressionSource(axiom.getClassExpression()),
                        () -> editorKit.getDataFactory()
                                .getIndividualTypesAxiomRemoveEvent(owner, axiom.getClassExpression()),
                        newEx -> editorKit.getDataFactory()
                                .getIndividualTypesAxiomModEvent(owner, newEx, axiom.getClassExpression())
                ));
            }

            @Override
            public void visit(OWLSameIndividualAxiom axiom) {
                Set<OWLNamedIndividual> sames = axiom.getIndividualsInSignature();
                sames.remove(owner);

                sames.forEach(i ->
                        samePn.addMoreExpression(new NamedIndividualLabel(
                                        new OWLNamedIndividualSource(i),
                                        () -> editorKit.getDataFactory()
                                                .getSamesIndividualsAxiomRemoveEvent(owner, i))
                ));
            }

            @Override
            public void visit(OWLDifferentIndividualsAxiom axiom) {
                Set<OWLNamedIndividual> sames = axiom.getIndividualsInSignature();

                sames.remove(owner);
                sames.forEach(i ->
                        diffPn.addMoreExpression(new NamedIndividualLabel(
                                        new OWLNamedIndividualSource(i),
                                        () -> editorKit.getDataFactory()
                                                .getDifferentIndividualsAxiomRemoveEvent(owner, i))
                ));
            }

            @Override
            public void visit(OWLDataPropertyAssertionAxiom axiom) {

                dataAssertPn.addMoreExpression(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                        new OWLEditorEvent.IndividualAxiomRemoved(axiom, owner)
                ));
            }

            @Override
            public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                objAssertPn.addMoreExpression(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                        new OWLEditorEvent.IndividualAxiomRemoved(axiom, owner)
                ));
            }

            @Override
            public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                negDataAssertPn.addMoreExpression(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                        new OWLEditorEvent.IndividualAxiomRemoved(axiom, owner)
                ));
            }

            @Override
            public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
                negObjAssertPn.addMoreExpression(new OWLIndividualAxiomLabel(new OWLIndividualAxiomSource(axiom), () ->
                        new OWLEditorEvent.IndividualAxiomRemoved(axiom, owner)
                ));
            }

        };
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void afterIndividualAxiomAdded(OWLEditorEvent.IndividualAxiomAdded event) {
        ChangeApplied ok = editorKit.getModelManager()
                .addAxiom(editorKit.getActiveOntology(), event.getAxiom());

        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(addHelper(event.getOwner()));

            Notification.show("Successfully added OWLClassExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Cannot add OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    private OWLAxiomVisitor removeHelper(OWLNamedIndividual owner) {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLClassAssertionAxiom axiom) {
                typePn.removeExpression(axiom.getClassExpression());
            }

            @Override
            public void visit(OWLSameIndividualAxiom axiom) {
                Set<OWLIndividual> sames = axiom.getIndividuals();
                sames.remove(owner);
                sames.forEach(samePn::removeExpression);
            }

            @Override
            public void visit(OWLDifferentIndividualsAxiom axiom) {
                Set<OWLIndividual> sames = axiom.getIndividuals();
                sames.remove(owner);
                sames.forEach(diffPn::removeExpression);
            }

            @Override
            public void visit(OWLDataPropertyAssertionAxiom axiom) {
                dataAssertPn.removeExpression(axiom);
            }

            @Override
            public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                negDataAssertPn.removeExpression(axiom);
            }

            @Override
            public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                objAssertPn.removeExpression(axiom);
            }

            @Override
            public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
                negObjAssertPn.removeExpression(axiom);
            }
        };
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void afterIndividualAxiomRemoved(OWLEditorEvent.IndividualAxiomRemoved event) {
        ChangeApplied ok = editorKit.getModelManager()
                .applyChange(new RemoveAxiom(editorKit.getActiveOntology(), event.getAxiom()));

        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(removeHelper(event.getOwner()));


            Notification.show("Successfully removed OWLClassExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Cannot remove OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void afterIndividualAxiomModified(OWLEditorEvent.IndividualAxiomModified event) {
        ChangeApplied removeOk = editorKit.getModelManager()
                .applyChange(new RemoveAxiom(editorKit.getActiveOntology(), event.getOldAxiom()));

        ChangeApplied addOk = editorKit.getModelManager()
                .addAxiom(editorKit.getActiveOntology(), event.getNewAxiom());

        if (removeOk == ChangeApplied.SUCCESSFULLY && addOk == ChangeApplied.SUCCESSFULLY) {
            event.getOldAxiom().accept(removeHelper(event.getOwner()));
            event.getNewAxiom().accept(addHelper(event.getOwner()));
        }

    }

    private static abstract class IndividualPanel extends AbstractExpressionPanel<OWLNamedIndividual> {

        public IndividualPanel(String caption) {
            super(caption);
        }

        @Override
        protected OWLLogicalEntitySource<OWLNamedIndividual> initDataSource() {
            return new OWLNamedIndividualSource();
        }
    }

    public static class NamedIndividualLabel extends AbstractNonEditableOWLObjectLabel<OWLNamedIndividual> {

        public NamedIndividualLabel(@Nonnull OWLObjectSource<OWLNamedIndividual> expressionSource, OWLExpressionRemoveHandler removeExpression) {
            super(expressionSource, removeExpression);
        }
    }
}
