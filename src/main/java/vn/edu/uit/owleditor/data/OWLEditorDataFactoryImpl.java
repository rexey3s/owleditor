package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.data.hierarchy.OWLClassHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLDataPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLObjectPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.list.OWLNamedIndividualContainer;
import vn.edu.uit.owleditor.event.OWLEditorEvent;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/1/2014.
 */
public class OWLEditorDataFactoryImpl implements OWLEditorDataFactory {

    private final OWLEditorKit editorKit;
    private final OWLDataFactory owlFactory = OWLManager.getOWLDataFactory();
    private OWLClassHierarchicalContainer classHierarchicalContainer;
    public OWLEditorDataFactoryImpl(@Nonnull OWLEditorKitImpl eKit) {
        this.editorKit = eKit;
        OWLClassHierarchicalContainer classHierarchicalContainer = new OWLClassHierarchicalContainer(editorKit.getActiveOntology());
    }


    @Override
    public OWLClassHierarchicalContainer getOWLClassHierarchicalContainer() {
        return classHierarchicalContainer;
    }

    @Override
    public OWLObjectPropertyHierarchicalContainer getOWLObjectPropertyHierarchicalContainer() {
        return new OWLObjectPropertyHierarchicalContainer(editorKit.getActiveOntology());
    }

    @Override
    public OWLDataPropertyHierarchicalContainer getOWLDataPropertyHierarchicalContainer() {
        return new OWLDataPropertyHierarchicalContainer(editorKit.getActiveOntology());
    }

    @Override
    public OWLNamedIndividualContainer getOWLIndividualListContainer() {
        return new OWLNamedIndividualContainer(editorKit.getActiveOntology());
    }

    @Override
    public OWLNamedIndividualContainer getOWLIndividualListContainer(@Nonnull OWLClass owlClass) {
        return new OWLNamedIndividualContainer(editorKit.getActiveOntology(), owlClass);
    }

    @Override
    public OWLPropertyAttributes.IsFunctionalProperty getOWLIsFunctionalProperty(OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsFunctionalProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsInverseFunctionalProperty getIsInverseFunctionalProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsInverseFunctionalProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsSymmetricProperty getIsSymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsSymmetricProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsASymmetricProperty getIsASymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsASymmetricProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsTransitiveProperty getIsTransitiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsTransitiveProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsReflexiveProperty getIsReflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsReflexiveProperty(owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsIrreflexiveProperty getIsIrreflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsIrreflexiveProperty(owlObjectProperty);
    }



    @Override
    public OWLEditorEvent.ClassAxiomAddEvent getEquivalentClassesAddEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomAddEvent(
                owlFactory.getOWLEquivalentClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAddEvent getSubClassOfAddEvent(OWLClass owner, OWLClassExpression supCls) {
        return new OWLEditorEvent.ClassAxiomAddEvent(
                owlFactory.getOWLSubClassOfAxiom(owner, supCls),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAddEvent getDisjointClassesAddEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomAddEvent(
                owlFactory.getOWLDisjointClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAddEvent getClassAssertionAddEvent(OWLClass owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.ClassAxiomAddEvent(
                owlFactory.getOWLClassAssertionAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoveEvent getEquivalentClassesRemoveEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomRemoveEvent(
                owlFactory.getOWLEquivalentClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoveEvent getSubClassOfRemoveEvent(OWLClass owner, OWLClassExpression supCls) {
        return new OWLEditorEvent.ClassAxiomRemoveEvent(
                owlFactory.getOWLSubClassOfAxiom(owner, supCls),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoveEvent getDisjointClassesRemoveEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomRemoveEvent(
                owlFactory.getOWLDisjointClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoveEvent getClassAssertionRemoveEvent(OWLClass owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.ClassAxiomRemoveEvent(
                owlFactory.getOWLClassAssertionAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomModifyEvent getEquivalentClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ClassAxiomModifyEvent(
                owlFactory.getOWLEquivalentClassesAxiom(owner, newEx),
                owlFactory.getOWLEquivalentClassesAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ClassAxiomModifyEvent getSubClassOfModEvent(OWLClass owner, OWLClassExpression newSupCls, OWLClassExpression oldSupCls) {
        return new OWLEditorEvent.ClassAxiomModifyEvent(
                owlFactory.getOWLSubClassOfAxiom(owner, newSupCls),
                owlFactory.getOWLSubClassOfAxiom(owner, oldSupCls),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ClassAxiomModifyEvent getDisjointClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ClassAxiomModifyEvent(
                owlFactory.getOWLDisjointClassesAxiom(owner, newEx),
                owlFactory.getOWLDisjointClassesAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualTypesAxiomAddEvent(OWLNamedIndividual owner, OWLClassExpression expression) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLClassAssertionAxiom(expression, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoveEvent getIndividualTypesAxiomRemoveEvent(OWLNamedIndividual owner, OWLClassExpression expression) {
        return new OWLEditorEvent.IndividualAxiomRemoveEvent(
                owlFactory.getOWLClassAssertionAxiom(expression, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomModifyEvent getIndividualTypesAxiomModEvent(OWLNamedIndividual owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.IndividualAxiomModifyEvent(
                owlFactory.getOWLClassAssertionAxiom(newEx, owner),
                owlFactory.getOWLClassAssertionAxiom(oldEx, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getSamesIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLSameIndividualAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoveEvent getSamesIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomRemoveEvent(
                owlFactory.getOWLSameIndividualAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getDifferentIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLDifferentIndividualsAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoveEvent getDifferentIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomRemoveEvent(
                owlFactory.getOWLDifferentIndividualsAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLDataPropertyAssertionAxiom(expression, owner, restriction),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualNegativeDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLNegativeDataPropertyAssertionAxiom(expression, owner, restriction),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualDataAssertionAxiomRemoveEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return null;
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLObjectPropertyAssertionAxiom(expression, owner, object),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAddEvent getIndividualNegativeObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object) {
        return new OWLEditorEvent.IndividualAxiomAddEvent(
                owlFactory.getOWLNegativeObjectPropertyAssertionAxiom(expression, owner, object),
                owner
        );
    }



    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getEquivalentObjectPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLEquivalentObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getSubPropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLSubObjectPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getInversePropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLInverseObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getDomainsAddEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLObjectPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getRangesAddEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLObjectPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAddEvent getDisjointPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAddEvent(
                owlFactory.getOWLDisjointObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getEquivalentObjectPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLEquivalentObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getSubPropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLSubObjectPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getInversePropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLInverseObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getDomainsRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLObjectPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getRangesRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLObjectPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getDisjointPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoveEvent(
                owlFactory.getOWLDisjointObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAddEvent getEquivalentDataPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAddEvent(
                owlFactory.getOWLEquivalentDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAddEvent getSubPropertyOfAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAddEvent(
                owlFactory.getOWLSubDataPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAddEvent getDisjointPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAddEvent(
                owlFactory.getOWLDisjointDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAddEvent getDomainsAddEvent(OWLDataProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAddEvent(
                owlFactory.getOWLDataPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAddEvent getRangesAddEvent(OWLDataProperty owner, OWLDataRange expression) {
        return new OWLEditorEvent.DataPropertyAxiomAddEvent(
                owlFactory.getOWLDataPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoveEvent getEquivalentDataPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoveEvent(
                owlFactory.getOWLEquivalentDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoveEvent getSubPropertyOfRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoveEvent(
                owlFactory.getOWLSubDataPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoveEvent getDisjointPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoveEvent(
                owlFactory.getOWLDisjointDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoveEvent getDomainsRemoveEvent(OWLDataProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoveEvent(
                owlFactory.getOWLDataPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoveEvent getRangesRemoveEvent(OWLDataProperty owner, OWLDataRange expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoveEvent(
                owlFactory.getOWLDataPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomModifyEvent getDomainsModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ObjectPropertyAxiomModifyEvent(
                owlFactory.getOWLObjectPropertyDomainAxiom(owner, newEx),
                owlFactory.getOWLObjectPropertyDomainAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomModifyEvent getRangesModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ObjectPropertyAxiomModifyEvent(
                owlFactory.getOWLObjectPropertyRangeAxiom(owner, newEx),
                owlFactory.getOWLObjectPropertyRangeAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomModifyEvent getDomainsModEvent(OWLDataProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.DataPropertyAxiomModifyEvent(
                owlFactory.getOWLDataPropertyDomainAxiom(owner, newEx),
                owlFactory.getOWLDataPropertyDomainAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomModifyEvent getRangesModEvent(OWLDataProperty owner, OWLDataRange newEx, OWLDataRange oldEx) {
        return new OWLEditorEvent.DataPropertyAxiomModifyEvent(
                owlFactory.getOWLDataPropertyRangeAxiom(owner, newEx),
                owlFactory.getOWLDataPropertyRangeAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.RuleAddEvent getRuleAddEvent(SWRLAPIRule rule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleAddEvent(rule, sourceOntology);
    }

    @Override
    public OWLEditorEvent.RuleRemoveEvent getRuleRemoveEvent(SWRLAPIRule rule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleRemoveEvent(rule, sourceOntology);
    }

    @Override
    public OWLEditorEvent.RuleModifyEvent getRuleMofifyEvent(SWRLAPIRule newRule, SWRLAPIRule oldRule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleModifyEvent(newRule, oldRule, sourceOntology);
    }

}
