package vn.edu.uit.owleditor.view.panel;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.*;
import vn.edu.uit.owleditor.event.*;
import vn.edu.uit.owleditor.view.component.AbstractEditableOWLObjectLabel;
import vn.edu.uit.owleditor.view.component.AbstractExpressionPanel;
import vn.edu.uit.owleditor.view.component.AbstractNonEditableOWLObjectLabel;
import vn.edu.uit.owleditor.view.component.InferredLabel;
import vn.edu.uit.owleditor.view.window.AbstractOWLExpressionEditorWindow;
import vn.edu.uit.owleditor.view.window.DataRangeEditorWindow;
import vn.edu.uit.owleditor.view.window.buildAddClassExpressionWindow;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/14.
 */
public class DataPropertyExpressionPanelContainer extends AbstractPanelContainer {

    private static final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();


    private AbstractExpressionPanel equivPn;
    private AbstractExpressionPanel subOfPn;
    private AbstractExpressionPanel disjointPn;
    private AbstractExpressionPanel domainsPn;
    private AbstractExpressionPanel rangesPn;

    public DataPropertyExpressionPanelContainer(@Nonnull OWLEditorKit eKit) {
        super(eKit);

    }

    @Override
    protected Component buildContent() {
        descriptionPanels = new CssLayout();
        descriptionPanels.addStyleName("dashboard-panels");
        equivPn = new DataPropertyPanel("Equivalent To: ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, owlDataPropertyExpression ->
                        editorKit.getDataFactory().getEquivalentDataPropertiesAddEvent(
                                dataSource.getValue(), owlDataPropertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLDataPropertyExpression> des = EntitySearcher
                        .getEquivalentProperties(dataSource.getValue(), editorKit.getActiveOntology());
                des.forEach(de -> root.addComponent(new buildSimpleDataPropertyLabel(
                                new OWLDataPropertyExpressionSource(de),
                                () -> editorKit.getDataFactory()
                                        .getEquivalentDataPropertiesRemoveEvent(dataSource.getValue(), de)))
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLDataProperty> implicitProperties = editorKit.getReasoner()
                            .getEquivalentDataProperties(dataSource.getValue()).getEntities();
                    implicitProperties.removeAll(des);
                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                            () -> editorKit.explain(editorKit.getOWLDataFactory()
                                    .getOWLEquivalentDataPropertiesAxiom(dataSource.getValue(), obj))
                    )));
                }
                */
            }
        };
        subOfPn = new DataPropertyPanel("Sub Property Of: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, owlDataPropertyExpression ->
                        editorKit.getDataFactory().getSubPropertyOfAddEvent(dataSource.getValue(), owlDataPropertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLDataPropertyExpression> des = EntitySearcher
                        .getSuperProperties(dataSource.getValue(), editorKit.getActiveOntology());
                des.forEach(de -> root.addComponent(new buildSimpleDataPropertyLabel(
                                new OWLDataPropertyExpressionSource(de),
                                () -> editorKit.getDataFactory()
                                        .getSubPropertyOfRemoveEvent(dataSource.getValue(), de)))
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLDataProperty> implicitProperties = editorKit.getReasoner()
                            .getSuperDataProperties(dataSource.getValue(), false).getFlattened();
                    implicitProperties.removeAll(des);
                    implicitProperties.remove(topDataProperty);
                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLSubDataPropertyOfAxiom(dataSource.getValue(), obj))
                            ))
                    );
                }
                */
            }
        };
        disjointPn = new DataPropertyPanel("Mutual Disjoint With: ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, owlDataPropertyExpression ->
                        editorKit.getDataFactory().getDisjointPropertiesAddEvent(dataSource.getValue(), owlDataPropertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLDataPropertyExpression> des = EntitySearcher.getDisjointProperties(dataSource.getValue(),
                        editorKit.getActiveOntology());
                des.remove(dataSource.getValue());
                des.forEach(de -> root.addComponent(new buildSimpleDataPropertyLabel(
                                        new OWLDataPropertyExpressionSource(de),
                                        () -> editorKit.getDataFactory()
                                                .getDisjointPropertiesRemoveEvent(dataSource.getValue(), de)
                                )
                        )
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLDataProperty> implicitProperties = editorKit.getReasoner()
                            .getDisjointDataProperties(dataSource.getValue()).getFlattened();
                    implicitProperties.removeAll(des);

                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLDisjointDataPropertiesAxiom(dataSource.getValue(), obj))
                            ))
                    );
                }
                */
            }
        };
        domainsPn = new DataPropertyPanel("Domains (intersection): ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(editorKit, owlClassExpression ->
                        editorKit.getDataFactory().getDomainsAddEvent(dataSource.getValue(), owlClassExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher
                        .getDomains(dataSource.getValue(), editorKit.getActiveOntology());
                ces.forEach(ce -> root.addComponent(new ClassExpressionPanelContainer.ClassLabel(
                                editorKit,
                                new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory().getDomainsRemoveEvent(dataSource.getValue(), ce),
                                newEx -> editorKit.getDataFactory().getDomainsModEvent(dataSource.getValue(), newEx, ce)))
                );
                if (editorKit.getReasonerStatus()) {
                    Set<OWLClass> implicitClasses = editorKit.getReasoner()
                            .getDataPropertyDomains(dataSource.getValue(), false).getFlattened();
                    ces.stream().filter(ce -> (ce instanceof OWLClass)).forEach(ce -> implicitClasses.remove((OWLClass) ce));
                    implicitClasses.remove(thing);
                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(c,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                                    .getOWLDataPropertyDomainAxiom(dataSource.getValue(), c)
                                    )))
                    );
                }
            }
        };
        rangesPn = new DataPropertyPanel("Range (intersection): ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new DataRangeEditorWindow(editorKit, range ->
                        editorKit.getDataFactory().getRangesAddEvent(dataSource.getValue(), range)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLDataRange> dataRanges = EntitySearcher
                        .getRanges(dataSource.getValue(), editorKit.getActiveOntology());

                dataRanges.forEach(rng -> root.addComponent(new DataRangeLabel(editorKit,
                            new OWLDataRangeSource(rng),
                            () -> editorKit.getDataFactory().getRangesRemoveEvent(dataSource.getValue(), rng),
                            newRange -> editorKit.getDataFactory().getRangesModEvent(dataSource.getValue(), newRange, rng)
                        ))
                );
            }
        };

        Responsive.makeResponsive(descriptionPanels);
        descriptionPanels.addComponent(createContentWrapper(equivPn, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(subOfPn, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(domainsPn, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(rangesPn, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(disjointPn, "left-slot-panel", "panel-mini-slot"));

        return descriptionPanels;
    }

    private OWLAxiomVisitor addHelper(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
        return new OWLAxiomVisitorAdapter() {

            @Override
            public void visit(OWLDataPropertyDomainAxiom axiom) {
                domainsPn.addMoreExpression(new ClassExpressionPanelContainer.ClassLabel(editorKit,
                        new OWLClassExpressionSource(axiom.getDomain()),
                        () -> editorKit.getDataFactory().getDomainsRemoveEvent(owner, axiom.getDomain()),
                        newEx -> editorKit.getDataFactory().getDomainsModEvent(owner, newEx, axiom.getDomain())
                ));

            }

            @Override
            public void visit(OWLDataPropertyRangeAxiom axiom) {

                rangesPn.addMoreExpression(new DataRangeLabel(editorKit,
                        new OWLDataRangeSource(axiom.getRange()),
                        () -> editorKit.getDataFactory().getRangesRemoveEvent(owner, axiom.getRange()),
                        newEx -> editorKit.getDataFactory().getRangesModEvent(owner, newEx, axiom.getRange())
                ));
            }

            @Override
            public void visit(OWLDisjointDataPropertiesAxiom axiom) {
                Set<OWLDataPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(oe -> disjointPn.addMoreExpression(new buildSimpleDataPropertyLabel(
                                new OWLDataPropertyExpressionSource(oe),
                                () -> editorKit.getDataFactory().getDisjointPropertiesRemoveEvent(owner, oe)
                        ))
                );
            }

            @Override
            public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
                Set<OWLDataPropertyExpression> des = axiom.getProperties();
                des.remove(owner);
                des.forEach(de -> equivPn.addMoreExpression(new buildSimpleDataPropertyLabel(
                        new OWLDataPropertyExpressionSource(de),
                        () -> editorKit.getDataFactory().getEquivalentDataPropertiesRemoveEvent(owner, de)
                )));
            }

            @Override
            public void visit(OWLSubDataPropertyOfAxiom axiom) {
                subOfPn.addMoreExpression(new buildSimpleDataPropertyLabel(
                        new OWLDataPropertyExpressionSource(axiom.getSuperProperty()),
                        () -> editorKit.getDataFactory().getSubPropertyOfRemoveEvent(owner, axiom.getSuperProperty())
                ));
            }

            @Override
            public void visit(OWLDatatypeDefinitionAxiom axiom) {
                super.visit(axiom);
            }

        };
    }

    private OWLAxiomVisitor removeHelper(OWLDataProperty owner) {
        return new OWLAxiomVisitorAdapter() {

            @Override
            public void visit(OWLDataPropertyAssertionAxiom axiom) {
                handleDefault(axiom);
            }

            @Override
            public void visit(OWLDataPropertyDomainAxiom axiom) {
                domainsPn.removeExpression(axiom.getDomain());
            }

            @Override
            public void visit(OWLDataPropertyRangeAxiom axiom) {
                rangesPn.removeExpression(axiom.getRange());
            }

            @Override
            public void visit(OWLDisjointDataPropertiesAxiom axiom) {
                Set<OWLDataPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(disjointPn::removeExpression);
            }

            @Override
            public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
                Set<OWLDataPropertyExpression> des = axiom.getProperties();
                des.remove(owner);
                des.forEach(equivPn::removeExpression);
            }

            @Override
            public void visit(OWLSubDataPropertyOfAxiom axiom) {
                subOfPn.removeExpression(axiom.getSuperProperty());
            }

            @Override
            public void visit(OWLDatatypeDefinitionAxiom axiom) {
                handleDefault(axiom);
            }

        };
    }

    @Subscribe
    public void afterDataPropertyAxiomAdded(OWLEditorEvent.DataPropertyAxiomAdded event) {
        ChangeApplied ok = editorKit.getModelManager().applyChange(new AddAxiom(editorKit.getActiveOntology(), event.getAxiom()));
        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(addHelper(event.getAxiom(), event.getOwner()));
            Notification.show("Successfully created DataProperty Expression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Cannot create DataProperty Expression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    public void afterDataPropertyAxiomRemoved(OWLEditorEvent.DataPropertyAxiomRemoved event) {
        ChangeApplied ok = editorKit.getModelManager().applyChange(new RemoveAxiom(
                editorKit.getActiveOntology(), event.getAxiom()));

        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(removeHelper(event.getOwner()));
            Notification.show("Successfully removed DataProperty Expression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show("Cannot remove DataProperty Expression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    public void afterDataPropertyAxiomModified(OWLEditorEvent.DataPropertyAxiomModified event) {
        ChangeApplied removeOk = editorKit.getModelManager().applyChange(new RemoveAxiom(
                editorKit.getActiveOntology(), event.getOldAxiom()));
        ChangeApplied addOk = editorKit.getModelManager().applyChange(new AddAxiom(
                editorKit.getActiveOntology(), event.getNewAxiom()));
        if (removeOk == ChangeApplied.SUCCESSFULLY && addOk == ChangeApplied.SUCCESSFULLY) {
            event.getOldAxiom().accept(removeHelper(event.getOwner()));
            event.getNewAxiom().accept(addHelper(event.getNewAxiom(), event.getOwner()));
            Notification.show(
                    "Successfully modified OWLDataPropertyExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show(
                    "Cannot modify OWLDataPropertyExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Override
    public void setPropertyDataSource(@Nonnull Property newDataSource) {
        equivPn.setPropertyDataSource(newDataSource);
        subOfPn.setPropertyDataSource(newDataSource);
        disjointPn.setPropertyDataSource(newDataSource);
        domainsPn.setPropertyDataSource(newDataSource);
        rangesPn.setPropertyDataSource(newDataSource);
    }

    private static class buildAddSimpleExpressionWindow extends AbstractOWLExpressionEditorWindow<OWLDataPropertyExpression> {
        private final DataPropertyHierarchicalPanel hierarchy;

        public buildAddSimpleExpressionWindow(@Nonnull OWLEditorKit eKit, OWLExpressionAddHandler<OWLDataPropertyExpression> addExpression1) {
            super(eKit, addExpression1);
            hierarchy = new DataPropertyHierarchicalPanel(editorKit);
            addMoreTab(hierarchy, "Chose a property");

        }

        @Override
        protected Button.ClickListener initSaveListener() {
            return click -> {
                if (getSelectedTab() instanceof DataPropertyHierarchicalPanel) {
                    OWLEditorEventBus.post(
                            addExpression.addingExpression(hierarchy.getSelectedProperty().getValue()));
                    close();
                }
            };
        }
    }

    public static class buildSimpleDataPropertyLabel extends AbstractNonEditableOWLObjectLabel<OWLDataPropertyExpression> {

        public buildSimpleDataPropertyLabel(@Nonnull OWLObjectSource<OWLDataPropertyExpression> expressionSource,
                                            @Nonnull OWLExpressionRemoveHandler expression) {
            super(expressionSource, expression);
        }
    }

    public static class DataRangeLabel extends AbstractEditableOWLObjectLabel<OWLDataRange> {

        public DataRangeLabel(@Nonnull OWLEditorKit eKit,
                              @Nonnull OWLObjectSource<OWLDataRange> expressionSource,
                              @Nonnull OWLExpressionRemoveHandler removeExpression,
                              @Nonnull OWLExpressionUpdateHandler<OWLDataRange> modifyExpression) {
            super(eKit, expressionSource, removeExpression, modifyExpression);
        }

        @Override
        public void initModifiedAction() {
            UI.getCurrent().addWindow(new DataRangeEditorWindow(editorKit, expressionSource, modifyExpression));
        }
    }

    private abstract class DataPropertyPanel extends AbstractExpressionPanel<OWLDataProperty> {

        public DataPropertyPanel(String caption) {
            super(caption);
        }

        @Override
        protected OWLLogicalEntitySource<OWLDataProperty> initDataSource() {
            return new OWLDataPropertySource();
        }
    }


}
