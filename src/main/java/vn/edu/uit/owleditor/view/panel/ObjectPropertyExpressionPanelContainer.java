package vn.edu.uit.owleditor.view.panel;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.*;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.event.OWLExpressionRemoveHandler;
import vn.edu.uit.owleditor.view.component.AbstractExpressionPanel;
import vn.edu.uit.owleditor.view.component.AbstractNonEditableOWLObjectLabel;
import vn.edu.uit.owleditor.view.component.InferredLabel;
import vn.edu.uit.owleditor.view.window.AbstractOWLExpressionEditorWindow;
import vn.edu.uit.owleditor.view.window.buildAddClassExpressionWindow;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/24/2014.
 */
public class ObjectPropertyExpressionPanelContainer extends AbstractPanelContainer {
    private static final OWLClass thing = OWLManager.getOWLDataFactory().getOWLThing();
    private static final OWLObjectProperty topProperty = OWLManager.getOWLDataFactory().getOWLTopObjectProperty();
    private static final OWLObjectProperty bottomProperty = OWLManager.getOWLDataFactory().getOWLBottomObjectProperty();
    private AbstractExpressionPanel objPropEquivPanel;
    private AbstractExpressionPanel subObjPropPanel;
    private AbstractExpressionPanel inversePropsPanel;
    private AbstractExpressionPanel domainsPanel;
    private AbstractExpressionPanel rangesPanel;
    private AbstractExpressionPanel disjointPanel;

    public ObjectPropertyExpressionPanelContainer(@Nonnull OWLEditorKit eKit) {
        super(eKit);
        addRootStyleName("object-property-panel");
    }


    @Override
    protected Component buildContent() {
        descriptionPanels = new CssLayout();
        descriptionPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(descriptionPanels);

        objPropEquivPanel = new ObjectPropertyPanel("Equivalent To: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, propertyExpression ->
                        editorKit.getDataFactory().getEquivalentObjectPropertiesAddEvent(
                                dataSource.getValue(), propertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLObjectPropertyExpression> oes = EntitySearcher
                        .getEquivalentProperties(dataSource.getValue(), editorKit.getActiveOntology());

                oes.forEach(oe -> root.addComponent(new simplePropertyLabel(
                                new OWLObjectPropertyExpressionSource(oe),
                                () -> editorKit.getDataFactory().getEquivalentObjectPropertiesRemoveEvent(
                                        dataSource.getValue(), oe)))
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLObjectPropertyExpression> implicitProperties = editorKit.getReasoner()
                            .getEquivalentObjectProperties(dataSource.getValue()).getEntities();
                    implicitProperties.remove(topProperty);
                    implicitProperties.removeAll(oes);
                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory().getOWLEquivalentObjectPropertiesAxiom(
                                                    dataSource.getValue(), obj)
                                    )))
                    );
                }
                */
            }
        };
        subObjPropPanel = new ObjectPropertyPanel("Sub Property Of: ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, propertyExpression ->
                        editorKit.getDataFactory().getSubPropertyOfAddEvent(dataSource.getValue(), propertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLObjectPropertyExpression> oes = EntitySearcher
                        .getSuperProperties(dataSource.getValue(), editorKit.getActiveOntology());

                oes.forEach(oe -> root.addComponent(new simplePropertyLabel(new OWLObjectPropertyExpressionSource(oe),
                                () -> editorKit.getDataFactory().getSubPropertyOfRemoveEvent(
                                        dataSource.getValue(), oe)
                        ))
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLObjectPropertyExpression> implicitProperties = editorKit.getReasoner()
                            .getSuperObjectProperties(dataSource.getValue(), false).getFlattened();

                    implicitProperties.remove(topProperty);
                    implicitProperties.removeAll(oes);
                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLSubObjectPropertyOfAxiom(dataSource.getValue(), obj))
                            ))
                    );
                }
                */
            }
        };
        inversePropsPanel = new ObjectPropertyPanel("Inverse Of: ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, propertyExpression ->
                        editorKit.getDataFactory().getInversePropertyOfAddEvent(dataSource.getValue(), propertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLObjectPropertyExpression> oes = EntitySearcher
                        .getInverses(dataSource.getValue(), editorKit.getActiveOntology());
                oes.forEach(oe -> root.addComponent(new simplePropertyLabel(new OWLObjectPropertyExpressionSource(oe),
                                () -> editorKit.getDataFactory()
                                        .getInversePropertyOfRemoveEvent(dataSource.getValue(), oe)))
                );
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLObjectPropertyExpression> implicitProperties = editorKit.getReasoner()
                            .getInverseObjectProperties(dataSource.getValue()).getEntities();

                    implicitProperties.remove(oes);

                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLInverseObjectPropertiesAxiom(dataSource.getValue(), obj))
                            ))
                    );
                }
                */
            }
        };
        domainsPanel = new ObjectPropertyPanel("Domain (intersection): ") {

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
                                editorKit, new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory().getDomainsRemoveEvent(dataSource.getValue(), ce),
                                newEx -> editorKit.getDataFactory().getDomainsModEvent(dataSource.getValue(), newEx, ce)
                        ))
                );
                if (editorKit.getReasonerStatus()) {
                    Set<OWLClass> implicitClasses = editorKit
                            .getReasoner().getObjectPropertyDomains(dataSource.getValue(), false).getFlattened();
                    implicitClasses.removeAll(ces);
                    implicitClasses.remove(thing);
                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(c,
                            () -> editorKit.explain(editorKit.getOWLDataFactory()
                                    .getOWLObjectPropertyDomainAxiom(dataSource.getValue(), c))
                    )));
                }
            }
        };
        rangesPanel = new ObjectPropertyPanel("Range (intersection): ") {
            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddClassExpressionWindow(editorKit, owlClassExpression ->
                        editorKit.getDataFactory().getRangesAddEvent(dataSource.getValue(), owlClassExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLClassExpression> ces = EntitySearcher
                        .getRanges(dataSource.getValue(), editorKit.getActiveOntology());
                ces.forEach(ce -> root.addComponent(new ClassExpressionPanelContainer.ClassLabel(
                                editorKit,
                                new OWLClassExpressionSource(ce),
                                () -> editorKit.getDataFactory().getRangesRemoveEvent(dataSource.getValue(), ce),
                                newEx -> editorKit.getDataFactory().getRangesModEvent(dataSource.getValue(), newEx, ce)
                        ))
                );
                if (editorKit.getReasonerStatus()) {
                    Set<OWLClass> implicitClasses = editorKit
                            .getReasoner().getObjectPropertyRanges(dataSource.getValue(), false).getFlattened();
                    implicitClasses.removeAll(ces);
                    implicitClasses.remove(thing);
                    implicitClasses.forEach(c -> root.addComponent(new InferredLabel(c,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLObjectPropertyRangeAxiom(dataSource.getValue(), c))
                            ))
                    );
                }
            }
        };
        disjointPanel = new ObjectPropertyPanel("Mutual Disjoint With: ") {

            @Override
            protected void initActionADD() {
                UI.getCurrent().addWindow(new buildAddSimpleExpressionWindow(editorKit, propertyExpression ->
                        editorKit.getDataFactory().getDisjointPropertiesAddEvent(dataSource.getValue(), propertyExpression)
                ));
            }

            @Override
            protected void initActionVIEW() {
                Collection<OWLObjectPropertyExpression> oes = EntitySearcher.getDisjointProperties(dataSource.getValue(),
                        editorKit.getActiveOntology());
                oes.remove(dataSource.getValue());
                oes.forEach(oe -> root.addComponent(new simplePropertyLabel(
                        new OWLObjectPropertyExpressionSource(oe),
                        () -> editorKit.getDataFactory().getDisjointPropertiesRemoveEvent(dataSource.getValue(), oe)
                )));
                /*
                if (editorKit.getReasonerStatus()) {
                    Set<OWLObjectPropertyExpression> implicitProperties = editorKit
                            .getReasoner().getDisjointObjectProperties(dataSource.getValue()).getFlattened();
                    implicitProperties.removeAll(implicitProperties);
                    implicitProperties.remove(bottomProperty);
                    implicitProperties.forEach(obj -> root.addComponent(new InferredLabel(obj,
                                    () -> editorKit.explain(editorKit.getOWLDataFactory()
                                            .getOWLDisjointObjectPropertiesAxiom(dataSource.getValue(), obj))
                            ))
                    );
                }
                */
            }
        };

        descriptionPanels.addComponent(createContentWrapper(objPropEquivPanel, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(subObjPropPanel, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(inversePropsPanel, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(domainsPanel, "right-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(rangesPanel, "left-slot-panel", "panel-mini-slot"));
        descriptionPanels.addComponent(createContentWrapper(disjointPanel, "right-slot-panel", "panel-mini-slot"));


        return descriptionPanels;
    }


    @Override
    public void setPropertyDataSource(@Nonnull Property newDataSource) {
        objPropEquivPanel.setPropertyDataSource(newDataSource);
        subObjPropPanel.setPropertyDataSource(newDataSource);
        inversePropsPanel.setPropertyDataSource(newDataSource);
        domainsPanel.setPropertyDataSource(newDataSource);
        rangesPanel.setPropertyDataSource(newDataSource);
        disjointPanel.setPropertyDataSource(newDataSource);
    }

    private OWLAxiomVisitor addHelper(OWLObjectPropertyAxiom addAxiom, OWLObjectProperty owner) {
        OWLEditorEvent.ObjectPropertyAxiomRemoved removeEvent =
                new OWLEditorEvent.ObjectPropertyAxiomRemoved(addAxiom, owner);
        return new OWLAxiomVisitorAdapter() {

            @Override
            public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
                Set<OWLObjectPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(oe -> objPropEquivPanel.addMoreExpression(new simplePropertyLabel(
                                new OWLObjectPropertyExpressionSource(oe), () -> removeEvent)
                ));
            }

            @Override
            public void visit(OWLInverseObjectPropertiesAxiom axiom) {
                inversePropsPanel.addMoreExpression(new simplePropertyLabel(
                                new OWLObjectPropertyExpressionSource(axiom.getSecondProperty()), () -> removeEvent)
                );
            }

            @Override
            public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
                Set<OWLObjectPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(oe -> disjointPanel.addMoreExpression(new simplePropertyLabel(
                                new OWLObjectPropertyExpressionSource(oe), () -> removeEvent)
                ));
            }

            @Override
            public void visit(OWLObjectPropertyDomainAxiom axiom) {
                domainsPanel.addMoreExpression(new ClassExpressionPanelContainer.ClassLabel(editorKit,
                        new OWLClassExpressionSource(axiom.getDomain()),
                        () -> editorKit.getDataFactory().getDomainsRemoveEvent(owner, axiom.getDomain()),
                        newEx -> editorKit.getDataFactory().getDomainsModEvent(owner, newEx, axiom.getDomain())));
            }

            @Override
            public void visit(OWLObjectPropertyRangeAxiom axiom) {
                rangesPanel.addMoreExpression(new ClassExpressionPanelContainer.ClassLabel(editorKit,
                        new OWLClassExpressionSource(axiom.getRange()),
                        () -> editorKit.getDataFactory().getRangesRemoveEvent(owner, axiom.getRange()),
                        newEx -> editorKit.getDataFactory().getRangesModEvent(owner, newEx, axiom.getRange())));
            }
        };
    }

    private OWLAxiomVisitor removeHelper(OWLObjectProperty owner) {
        return new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
                Set<OWLObjectPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(objPropEquivPanel::removeExpression);
            }

            @Override
            public void visit(OWLSubObjectPropertyOfAxiom axiom) {
                subObjPropPanel.removeExpression(axiom.getSuperProperty());
            }

            @Override
            public void visit(OWLInverseObjectPropertiesAxiom axiom) {
                inversePropsPanel.removeExpression(axiom.getSecondProperty());
            }

            @Override
            public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
                Set<OWLObjectPropertyExpression> oes = axiom.getProperties();
                oes.remove(owner);
                oes.forEach(disjointPanel::removeExpression);
            }

            @Override
            public void visit(OWLObjectPropertyDomainAxiom axiom) {
                domainsPanel.removeExpression(axiom.getDomain());
            }

            @Override
            public void visit(OWLObjectPropertyRangeAxiom axiom) {
                rangesPanel.removeExpression(axiom.getRange());
            }
        };
    }


    @Subscribe
    public void afterObjectPropertyAxiomAdded(
            OWLEditorEvent.ObjectPropertyAxiomAdded event) {
        ChangeApplied ok = editorKit.getModelManager()
                .applyChange(new AddAxiom(editorKit.getActiveOntology(), event.getAxiom()));
        if (ok == ChangeApplied.SUCCESSFULLY) {
            event.getAxiom().accept(addHelper(event.getAxiom(), event.getOwner()));
            Notification.show(
                    "Successfully created OWLObjectPropertyExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show(
                    "Cannot create OWLClassExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    @Subscribe
    public void afterObjectPropertyAxiomRemoved(
            OWLEditorEvent.ObjectPropertyAxiomRemoved event) {

        OWLEditorKit editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
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
    public void afterObjectPropertyAxiomModified(
            OWLEditorEvent.ObjectPropertyAxiomModified event) {

        OWLEditorKit editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        ChangeApplied removeOk = editorKit.getModelManager()
                .applyChange(new RemoveAxiom(editorKit.getActiveOntology(), event.getOldAxiom()));
        ChangeApplied addOk = editorKit.getModelManager()
                .applyChange(new AddAxiom(editorKit.getActiveOntology(), event.getNewAxiom()));

        if (removeOk == ChangeApplied.SUCCESSFULLY && addOk == ChangeApplied.SUCCESSFULLY) {
            event.getOldAxiom().accept(removeHelper(event.getOwner()));
            event.getNewAxiom().accept(addHelper(event.getNewAxiom(), event.getOwner()));
            Notification.show(
                    "Successfully modified OWLObjectPropertyExpression",
                    Notification.Type.TRAY_NOTIFICATION);
        } else {
            Notification.show(
                    "Cannot modify OWLObjectPropertyExpression",
                    Notification.Type.WARNING_MESSAGE);
        }
    }

    public static class buildAddSimpleExpressionWindow extends AbstractOWLExpressionEditorWindow<OWLObjectPropertyExpression> {
        private final ObjectPropertyHierarchicalPanel hierarchy;

        public buildAddSimpleExpressionWindow(@Nonnull OWLEditorKit eKit,
                                              @Nonnull OWLExpressionAddHandler<OWLObjectPropertyExpression> addExpression1) {
            super(eKit, addExpression1);
            hierarchy = new ObjectPropertyHierarchicalPanel(editorKit);
            addMoreTab(hierarchy, "Chose a Property");

        }

        @Override
        protected Button.ClickListener initSaveListener() {
            return click -> {
                if (getSelectedTab() instanceof ObjectPropertyHierarchicalPanel) {
                    OWLEditorUI.getEventBus().post(addExpression.addingExpression(
                            hierarchy.getSelectedProperty().getValue()));
                    close();
                }
            };
        }
    }

    public static class simplePropertyLabel extends AbstractNonEditableOWLObjectLabel<OWLObjectPropertyExpression> {

        public simplePropertyLabel(OWLObjectSource<OWLObjectPropertyExpression> expressionSource,
                                   OWLExpressionRemoveHandler removeExpression) {
            super(expressionSource, removeExpression);
        }
    }

    private abstract class ObjectPropertyPanel extends AbstractExpressionPanel<OWLObjectProperty> {

        public ObjectPropertyPanel(String caption) {
            super(caption);
        }

        @Override
        protected OWLLogicalEntitySource<OWLObjectProperty> initDataSource() {
            return new OWLObjectPropertySource();
        }
    }

}
