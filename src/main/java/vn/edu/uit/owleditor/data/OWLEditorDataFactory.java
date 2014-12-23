package vn.edu.uit.owleditor.data;

import vn.edu.uit.owleditor.data.hierarchy.OWLClassHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLDataPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.hierarchy.OWLObjectPropertyHierarchicalContainer;
import vn.edu.uit.owleditor.data.list.OWLNamedIndividualContainer;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;

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
    OWLEditorEvent.ClassAxiomAdded getEquivalentClassesAddEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomAdded getSubClassOfAddEvent(OWLClass owner, OWLClassExpression supCls);

    OWLEditorEvent.ClassAxiomAdded getDisjointClassesAddEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomAdded getClassAssertionAddEvent(OWLClass owner, OWLNamedIndividual expression);

    OWLEditorEvent.ClassAxiomRemoved getEquivalentClassesRemoveEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomRemoved getSubClassOfRemoveEvent(OWLClass owner, OWLClassExpression supCls);

    OWLEditorEvent.ClassAxiomRemoved getDisjointClassesRemoveEvent(OWLClass owner, OWLClassExpression expression);

    OWLEditorEvent.ClassAxiomRemoved getClassAssertionRemoveEvent(OWLClass owner, OWLNamedIndividual expression);

    OWLEditorEvent.ClassAxiomModified getEquivalentClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.ClassAxiomModified getSubClassOfModEvent(OWLClass owner, OWLClassExpression newSupCls, OWLClassExpression oldSupCls);

    OWLEditorEvent.ClassAxiomModified getDisjointClassesModEvent(OWLClass owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.IndividualAxiomAdded getIndividualTypesAxiomAddEvent(OWLNamedIndividual owner, OWLClassExpression expression);

    OWLEditorEvent.IndividualAxiomRemoved getIndividualTypesAxiomRemoveEvent(OWLNamedIndividual owner, OWLClassExpression expression);

    OWLEditorEvent.IndividualAxiomModified getIndividualTypesAxiomModEvent(OWLNamedIndividual owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.IndividualAxiomAdded getSamesIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomRemoved getSamesIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomAdded getDifferentIndividualsAxiomAddEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomRemoved getDifferentIndividualsAxiomRemoveEvent(OWLNamedIndividual owner, OWLNamedIndividual expression);

    OWLEditorEvent.IndividualAxiomAdded getIndividualDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction);

    OWLEditorEvent.IndividualAxiomAdded getIndividualNegativeDataAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral restriction);

    OWLEditorEvent.IndividualAxiomAdded getIndividualDataAssertionAxiomRemoveEvent(OWLNamedIndividual owner, OWLDataPropertyExpression expression, OWLLiteral object);

    OWLEditorEvent.IndividualAxiomAdded getIndividualObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object);

    OWLEditorEvent.IndividualAxiomAdded getIndividualNegativeObjectPropertyAssertionAxiomAddEvent(OWLNamedIndividual owner, OWLObjectPropertyExpression expression, OWLIndividual object);



    OWLEditorEvent.ObjectPropertyAxiomAdded getEquivalentObjectPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAdded getSubPropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAdded getInversePropertyOfAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAdded getDomainsAddEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAdded getRangesAddEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomAdded getDisjointPropertiesAddEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getEquivalentObjectPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getSubPropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getInversePropertyOfRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getDomainsRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getRangesRemoveEvent(OWLObjectProperty owner, OWLClassExpression expression);

    OWLEditorEvent.ObjectPropertyAxiomRemoved getDisjointPropertiesRemoveEvent(OWLObjectProperty owner, OWLObjectPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAdded getEquivalentDataPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAdded getSubPropertyOfAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAdded getDisjointPropertiesAddEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomAdded getDomainsAddEvent(OWLDataProperty owner, OWLClassExpression expression);

    OWLEditorEvent.DataPropertyAxiomAdded getRangesAddEvent(OWLDataProperty owner, OWLDataRange expression);

    OWLEditorEvent.DataPropertyAxiomRemoved getEquivalentDataPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoved getSubPropertyOfRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoved getDisjointPropertiesRemoveEvent(OWLDataProperty owner, OWLDataPropertyExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoved getDomainsRemoveEvent(OWLDataProperty owner, OWLClassExpression expression);

    OWLEditorEvent.DataPropertyAxiomRemoved getRangesRemoveEvent(OWLDataProperty owner, OWLDataRange expression);

    OWLEditorEvent.ObjectPropertyAxiomModified getDomainsModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.ObjectPropertyAxiomModified getRangesModEvent(OWLObjectProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.DataPropertyAxiomModified getDomainsModEvent(OWLDataProperty owner, OWLClassExpression newEx, OWLClassExpression oldEx);

    OWLEditorEvent.DataPropertyAxiomModified getRangesModEvent(OWLDataProperty owner, OWLDataRange newEx, OWLDataRange oldEx);

    /**
     * Rule
     */
    OWLEditorEvent.RuleAdded getRuleAddEvent(SWRLAPIRule rule, OWLOntology sourceOntology);

    OWLEditorEvent.RuleRemoved getRuleRemoveEvent(SWRLAPIRule rule, OWLOntology sourceOntology);

    OWLEditorEvent.RuleModified getRuleMofifyEvent(SWRLAPIRule newRule, SWRLAPIRule oldRule, OWLOntology sourceOntology);
}
