package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;
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
public interface OWLEditorDataFactory {

    OWLClassHierarchicalContainer getOWLClassHierarchicalContainer();

    OWLObjectPropertyHierarchicalContainer getOWLObjectPropertyHierarchicalContainer();

    OWLDataPropertyHierarchicalContainer getOWLDataPropertyHierarchicalContainer();

    OWLNamedIndividualContainer getOWLIndividualListContainer();

    OWLNamedIndividualContainer getOWLIndividualListContainer(@Nonnull OWLClass owlClass);

    OWLPropertyAttributes.IsFunctionalProperty getOWLIsFunctionalProperty(OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsInverseFunctionalProperty getIsInverseFunctionalProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsSymmetricProperty getIsSymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsASymmetricProperty getIsASymmetricProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsTransitiveProperty getIsTransitiveProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsReflexiveProperty getIsReflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    OWLPropertyAttributes.IsIrreflexiveProperty getIsIrreflexiveProperty(@Nonnull OWLObjectProperty owlObjectProperty);

    /**
     * Events factory
     */
    OWLEditorEvent.ClassAxiomAddEvent getEquivalentClassesAddEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomAddEvent getSubClassOfAddEvent(OWLClass owner, OWLClassExpression supCls);

    OWLEditorEvent.ClassAxiomAddEvent getDisjointClassesAddEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomAddEvent getClassAssertionAddEvent(OWLClass owner, OWLNamedIndividual expression);

    OWLEditorEvent.ClassAxiomRemoveEvent getEquivalentClassesRemoveEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomRemoveEvent getSubClassOfRemoveEvent(OWLClass owner, OWLClassExpression supCls);

    OWLEditorEvent.ClassAxiomRemoveEvent getDisjointClassesRemoveEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomRemoveEvent getClassAssertionRemoveEvent(OWLClass owner, OWLNamedIndividual expression);

    OWLEditorEvent.ClassAxiomModifyEvent getEquivalentClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.ClassAxiomModifyEvent getSubClassOfModEvent(OWLClass owner, OWLClassExpression newSupCls, OWLClassExpression oldSupCls);

    OWLEditorEvent.ClassAxiomModifyEvent getDisjointClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualTypesAxiomAddEvent(OWLNamedIndividual owner, OWLClassExpression expression);

    OWLEditorEvent.IndividualAxiomRemoveEvent getIndividualTypesAxiomRemoveEvent(OWLNamedIndividual owner, OWLClassExpression expression);

    OWLEditorEvent.IndividualAxiomModifyEvent getIndividualTypesAxiomModEvent(OWLNamedIndividual owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.IndividualAxiomAddEvent getSamesIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomRemoveEvent getSamesIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomAddEvent getDifferentIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomRemoveEvent getDifferentIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualNegativeDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualDataAssertionAxiomRemoveEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral object);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object);

    OWLEditorEvent.IndividualAxiomAddEvent getIndividualNegativeObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object);


    OWLEditorEvent.ObjectPropertyAxiomAddEvent getEquivalentObjectPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAddEvent getSubPropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAddEvent getInversePropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAddEvent getDomainsAddEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAddEvent getRangesAddEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAddEvent getDisjointPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getEquivalentObjectPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getSubPropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getInversePropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getDomainsRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getRangesRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoveEvent getDisjointPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAddEvent getEquivalentDataPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAddEvent getSubPropertyOfAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAddEvent getDisjointPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAddEvent getDomainsAddEvent(OWLDataProperty owner, OWLClassExpression expression);

    OWLEditorEvent.DataPropertyAxiomAddEvent getRangesAddEvent(OWLDataProperty owner, OWLDataRange expression);

    OWLEditorEvent.DataPropertyAxiomRemoveEvent getEquivalentDataPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoveEvent getSubPropertyOfRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoveEvent getDisjointPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoveEvent getDomainsRemoveEvent(OWLDataProperty owner, OWLClassExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoveEvent getRangesRemoveEvent(OWLDataProperty owner, OWLDataRange expression);

    OWLEditorEvent.ObjectPropertyAxiomModifyEvent getDomainsModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.ObjectPropertyAxiomModifyEvent getRangesModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.DataPropertyAxiomModifyEvent getDomainsModEvent(OWLDataProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.DataPropertyAxiomModifyEvent getRangesModEvent(OWLDataProperty owner, OWLDataRange newEx, OWLDataRange oldEx);

    /**
     * Rule
     */
    OWLEditorEvent.RuleAddEvent getRuleAddEvent(SWRLAPIRule rule, OWLOntology sourceOntology);

    OWLEditorEvent.RuleRemoveEvent getRuleRemoveEvent(SWRLAPIRule rule, OWLOntology sourceOntology);

    OWLEditorEvent.RuleModifyEvent getRuleMofifyEvent(SWRLAPIRule newRule, SWRLAPIRule oldRule, OWLOntology sourceOntology);
}
