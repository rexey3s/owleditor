package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;
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

    private final OWLEditorKitImpl editorKit;

    public OWLEditorDataFactoryImpl(@Nonnull OWLEditorKitImpl eKit) {
        this.editorKit = eKit;
    }


    @Override
    public OWLClassHierarchicalContainer getOWLClassHierarchicalContainer() {
        return new OWLClassHierarchicalContainer(editorKit.getActiveOntology());
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
        return new OWLPropertyAttributes.IsFunctionalProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsInverseFunctionalProperty getIsInverseFunctionalProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsInverseFunctionalProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsSymmetricProperty getIsSymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsSymmetricProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsASymmetricProperty getIsASymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsASymmetricProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsTransitiveProperty getIsTransitiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsTransitiveProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsReflexiveProperty getIsReflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsReflexiveProperty(editorKit, owlObjectProperty);
    }

    @Override
    public OWLPropertyAttributes.IsIrreflexiveProperty getIsIrreflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty) {
        return new OWLPropertyAttributes.IsIrreflexiveProperty(editorKit, owlObjectProperty);
    }



    @Override
    public OWLEditorEvent.ClassAxiomAdded getEquivalentClassesAddEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomAdded(
                editorKit.getOWLDataFactory().getOWLEquivalentClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAdded getSubClassOfAddEvent(OWLClass owner, OWLClassExpression supCls) {
        return new OWLEditorEvent.ClassAxiomAdded(
                editorKit.getOWLDataFactory().getOWLSubClassOfAxiom(owner, supCls),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAdded getDisjointClassesAddEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDisjointClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomAdded getClassAssertionAddEvent(OWLClass owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.ClassAxiomAdded(
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoved getEquivalentClassesRemoveEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLEquivalentClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoved getSubClassOfRemoveEvent(OWLClass owner, OWLClassExpression supCls) {
        return new OWLEditorEvent.ClassAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLSubClassOfAxiom(owner, supCls),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoved getDisjointClassesRemoveEvent(OWLClass owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ClassAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDisjointClassesAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomRemoved getClassAssertionRemoveEvent(OWLClass owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.ClassAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(owner, expression),
                owner);
    }

    @Override
    public OWLEditorEvent.ClassAxiomModified getEquivalentClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ClassAxiomModified(
                editorKit.getOWLDataFactory().getOWLEquivalentClassesAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLEquivalentClassesAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ClassAxiomModified getSubClassOfModEvent(OWLClass owner, OWLClassExpression newSupCls, OWLClassExpression oldSupCls) {
        return new OWLEditorEvent.ClassAxiomModified(
                editorKit.getOWLDataFactory().getOWLSubClassOfAxiom(owner, newSupCls),
                editorKit.getOWLDataFactory().getOWLSubClassOfAxiom(owner, oldSupCls),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ClassAxiomModified getDisjointClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ClassAxiomModified(
                editorKit.getOWLDataFactory().getOWLDisjointClassesAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLDisjointClassesAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualTypesAxiomAddEvent(OWLNamedIndividual owner, OWLClassExpression expression) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(expression, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoved getIndividualTypesAxiomRemoveEvent(OWLNamedIndividual owner, OWLClassExpression expression) {
        return new OWLEditorEvent.IndividualAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(expression, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomModified getIndividualTypesAxiomModEvent(OWLNamedIndividual owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.IndividualAxiomModified(
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(newEx, owner),
                editorKit.getOWLDataFactory().getOWLClassAssertionAxiom(oldEx, owner),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getSamesIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLSameIndividualAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoved getSamesIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLSameIndividualAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getDifferentIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDifferentIndividualsAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomRemoved getDifferentIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression) {
        return new OWLEditorEvent.IndividualAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDifferentIndividualsAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDataPropertyAssertionAxiom(expression, owner, restriction),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualNegativeDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLNegativeDataPropertyAssertionAxiom(expression, owner, restriction),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualDataAssertionAxiomRemoveEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction) {
        return null;
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(expression, owner, object),
                owner
        );
    }

    @Override
    public OWLEditorEvent.IndividualAxiomAdded getIndividualNegativeObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object) {
        return new OWLEditorEvent.IndividualAxiomAdded(
                editorKit.getOWLDataFactory().getOWLNegativeObjectPropertyAssertionAxiom(expression, owner, object),
                owner
        );
    }



    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getEquivalentObjectPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLEquivalentObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getSubPropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLSubObjectPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getInversePropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLInverseObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getDomainsAddEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLObjectPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getRangesAddEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLObjectPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomAdded getDisjointPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDisjointObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getEquivalentObjectPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLEquivalentObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getSubPropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLSubObjectPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getInversePropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLInverseObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getDomainsRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLObjectPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getRangesRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLObjectPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomRemoved getDisjointPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression) {
        return new OWLEditorEvent.ObjectPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDisjointObjectPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAdded getEquivalentDataPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLEquivalentDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAdded getSubPropertyOfAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLSubDataPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAdded getDisjointPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDisjointDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAdded getDomainsAddEvent(OWLDataProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDataPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomAdded getRangesAddEvent(OWLDataProperty owner, OWLDataRange expression) {
        return new OWLEditorEvent.DataPropertyAxiomAdded(
                editorKit.getOWLDataFactory().getOWLDataPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoved getEquivalentDataPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLEquivalentDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoved getSubPropertyOfRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLSubDataPropertyOfAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoved getDisjointPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDisjointDataPropertiesAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoved getDomainsRemoveEvent(OWLDataProperty owner, OWLClassExpression expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDataPropertyDomainAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomRemoved getRangesRemoveEvent(OWLDataProperty owner, OWLDataRange expression) {
        return new OWLEditorEvent.DataPropertyAxiomRemoved(
                editorKit.getOWLDataFactory().getOWLDataPropertyRangeAxiom(owner, expression),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomModified getDomainsModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ObjectPropertyAxiomModified(
                editorKit.getOWLDataFactory().getOWLObjectPropertyDomainAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLObjectPropertyDomainAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.ObjectPropertyAxiomModified getRangesModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.ObjectPropertyAxiomModified(
                editorKit.getOWLDataFactory().getOWLObjectPropertyRangeAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLObjectPropertyRangeAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomModified getDomainsModEvent(OWLDataProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx) {
        return new OWLEditorEvent.DataPropertyAxiomModified(
                editorKit.getOWLDataFactory().getOWLDataPropertyDomainAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLDataPropertyDomainAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.DataPropertyAxiomModified getRangesModEvent(OWLDataProperty owner, OWLDataRange newEx, OWLDataRange oldEx) {
        return new OWLEditorEvent.DataPropertyAxiomModified(
                editorKit.getOWLDataFactory().getOWLDataPropertyRangeAxiom(owner, newEx),
                editorKit.getOWLDataFactory().getOWLDataPropertyRangeAxiom(owner, oldEx),
                owner
        );
    }

    @Override
    public OWLEditorEvent.RuleAdded getRuleAddEvent(SWRLAPIRule rule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleAdded(rule, sourceOntology);
    }

    @Override
    public OWLEditorEvent.RuleRemoved getRuleRemoveEvent(SWRLAPIRule rule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleRemoved(rule, sourceOntology);
    }

    @Override
    public OWLEditorEvent.RuleModified getRuleMofifyEvent(SWRLAPIRule newRule, SWRLAPIRule oldRule, OWLOntology sourceOntology) {
        return new OWLEditorEvent.RuleModified(newRule, oldRule, sourceOntology);
    }

}
