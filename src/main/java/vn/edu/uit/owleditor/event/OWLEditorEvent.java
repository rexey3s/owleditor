package vn.edu.uit.owleditor.event;

import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/20/14.
 */
public final class OWLEditorEvent {

    public static abstract class EntityAddEvent<T extends OWLLogicalEntity> {

        private final T subject, object;

        public EntityAddEvent(@Nonnull T subject, @Nullable T object) {
            this.subject = subject;
            this.object = object;
        }

        public T getSubject() {
            return subject;
        }

        public T getObject() {
            return object;
        }

    }


    /**
     * Event should be called whenever a OWLNamedObject removed by user
     *
     * @param <T>
     */
    public static abstract class EntityRemoveEvent<T extends OWLLogicalEntity> {

        private final T removedObject;

        public EntityRemoveEvent(T removedObject) {
            this.removedObject = removedObject;
        }

        public T getRemovedObject() {
            return removedObject;
        }

    }

    /**
     * OWLClass creation and removal events
     */
    public static final class SubClassAddEvent extends
            EntityAddEvent<OWLClass> {

        public SubClassAddEvent(OWLClass subClz, OWLClass superClz) {
            super(subClz, superClz);
        }

        public OWLClass getSubClass() {
            return getSubject();
        }

        public OWLClass getSuperClass() {
            return getObject();
        }
    }

    public static final class SiblingClassAddEvent extends
            EntityAddEvent<OWLClass> {

        public SiblingClassAddEvent(OWLClass owlNamedObject, OWLClass siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLClass getDeclareClass() {
            return getSubject();
        }

        public OWLClass getSiblingClass() {
            return getObject();
        }
    }

    public static final class ClassRemoveEvent extends
            EntityRemoveEvent<OWLClass> {

        public ClassRemoveEvent(OWLClass removedObject) {
            super(removedObject);
        }
    }

    /**
     * OWLObjectProperty creation and removal events
     */
    public static final class SubObjectPropertyAddEvent extends
            EntityAddEvent<OWLObjectProperty> {

        public SubObjectPropertyAddEvent(OWLObjectProperty child, OWLObjectProperty parent) {
            super(child, parent);
        }

        public OWLObjectProperty getSubProperty() {
            return getSubject();
        }

        public OWLObjectProperty getSuperProperty() {
            return getObject();
        }
    }

    public static final class SiblingObjectPropertyAddEvent extends
            EntityAddEvent<OWLObjectProperty> {

        public SiblingObjectPropertyAddEvent(OWLObjectProperty owlNamedObject, OWLObjectProperty siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLObjectProperty getDeclareProperty() {
            return getSubject();
        }

        public OWLObjectProperty getSiblingProperty() {
            return getObject();
        }
    }

    public static final class ObjectPropertyRemove extends
            EntityRemoveEvent<OWLObjectProperty> {

        public ObjectPropertyRemove(OWLObjectProperty removedObject) {
            super(removedObject);
        }
    }

    /**
     * OWLDataProperty creation and removal events
     */
    public static final class SubDataPropertyAddEvent extends
            EntityAddEvent<OWLDataProperty> {

        public SubDataPropertyAddEvent(OWLDataProperty child, OWLDataProperty parent) {
            super(child, parent);
        }

        public OWLDataProperty getSubProperty() {
            return getSubject();
        }

        public OWLDataProperty getSuperProperty() {
            return getObject();
        }
    }

    public static final class SiblingDataPropertyAddEvent extends
            EntityAddEvent<OWLDataProperty> {

        public SiblingDataPropertyAddEvent(OWLDataProperty owlNamedObject, OWLDataProperty siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLDataProperty getDeclareProperty() {
            return getSubject();
        }

        public OWLDataProperty getSiblingProperty() {
            return getObject();
        }
    }

    public static final class DataPropertyRemoveEvent extends
            EntityRemoveEvent<OWLDataProperty> {

        public DataPropertyRemoveEvent(OWLDataProperty removedObject) {
            super(removedObject);
        }
    }

    public static abstract class OWLAxiomAddEvent<A extends OWLAxiom, O extends OWLObject> {

        private final A axiom;

        private final O owner;

        public OWLAxiomAddEvent(A axiom, O owner) {
            this.axiom = axiom;
            this.owner = owner;
        }

        public A getAxiom() {
            return axiom;
        }

        public O getOwner() {
            return owner;
        }
    }

    public static abstract class OWLAxiomRemoveEvent<A extends OWLAxiom, O extends OWLObject> {
        private final A axiom;

        private final O owner;

        public OWLAxiomRemoveEvent(A axiom, O owner) {
            this.axiom = axiom;
            this.owner = owner;
        }

        public A getAxiom() {
            return axiom;
        }

        public O getOwner() {
            return owner;
        }
    }

    public static abstract class OWLAxiomModifyEvent<A extends OWLAxiom, O extends OWLObject> {
        private final A newAxiom;
        private final A oldAxiom;
        private final O owner;

        public OWLAxiomModifyEvent(A newAxiom, A old, O owner) {
            this.newAxiom = newAxiom;
            this.oldAxiom = old;
            this.owner = owner;
        }

        public A getNewAxiom() {
            return newAxiom;
        }

        public A getOldAxiom() {
            return oldAxiom;
        }

        public O getOwner() {
            return owner;
        }
    }

    /**
     * OWLClassAxiom addition, removal and modification events
     */
    public static final class ClassAxiomAddEvent extends OWLAxiomAddEvent<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomAddEvent(OWLLogicalAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }

    public static final class afterSubClassOfAxiomAddEvent extends OWLAxiomAddEvent<OWLLogicalAxiom, OWLClass> {

        public afterSubClassOfAxiomAddEvent(OWLSubClassOfAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }

    public static final class afterSubClassOfAxiomRemoveEvent extends OWLAxiomAddEvent<OWLLogicalAxiom, OWLClass> {

        public afterSubClassOfAxiomRemoveEvent(OWLSubClassOfAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }
    
    public static final class IndividualAxiomAddEvent extends OWLAxiomAddEvent<OWLIndividualAxiom, OWLNamedIndividual> {
        public IndividualAxiomAddEvent(OWLIndividualAxiom axiom, OWLNamedIndividual owner) {
            super(axiom, owner);
        }
    }

    public static final class ClassAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomRemoveEvent(OWLLogicalAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }

    public static final class IndividualAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLIndividualAxiom, OWLNamedIndividual> {

        public IndividualAxiomRemoveEvent(OWLIndividualAxiom axiom, OWLNamedIndividual owner) {
            super(axiom, owner);
        }
    }

    public static final class ClassAxiomModifyEvent extends OWLAxiomModifyEvent<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomModifyEvent(OWLLogicalAxiom newAxiom, OWLLogicalAxiom old, OWLClass owner) {
            super(newAxiom, old, owner);
        }
    }


    public static final class IndividualAxiomModifyEvent extends OWLAxiomModifyEvent<OWLIndividualAxiom, OWLNamedIndividual> {

        public IndividualAxiomModifyEvent(OWLIndividualAxiom newAxiom, OWLIndividualAxiom old, OWLNamedIndividual owner) {
            super(newAxiom, old, owner);
        }
    }

    /**
     * OWLNamedIndividual creation and removal events
     */
    public static final class IndividualAddEvent extends
            EntityAddEvent<OWLNamedIndividual> {
        private final OWLNamedIndividual subject;

        public IndividualAddEvent(OWLNamedIndividual subject) {
            super(subject, null);
            this.subject = subject;
        }

        public OWLNamedIndividual getIndividual() {
            return subject;
        }

    }

    public static final class IndividualRemoveEvent extends
            EntityRemoveEvent<OWLNamedIndividual> {
        private final OWLNamedIndividual individual;

        public IndividualRemoveEvent(OWLNamedIndividual individual) {
            super(individual);
            this.individual = individual;
        }

        public OWLNamedIndividual getIndividual() {
            return individual;
        }

    }

    /**
     * OWLObjectPropertyExpression addition and removal events
     */
    public static final class ObjectPropertyAxiomAddEvent extends OWLAxiomAddEvent<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomAddEvent(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class afterSubObjectPropertyOfAxiomAddEvent
            extends OWLAxiomAddEvent<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public afterSubObjectPropertyOfAxiomAddEvent(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class afterSubObjectPropertyOfAxiomRemoveEvent
            extends OWLAxiomAddEvent<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public afterSubObjectPropertyOfAxiomRemoveEvent(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }
    public static final class ObjectPropertyAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomRemoveEvent(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class ObjectPropertyAxiomModifyEvent extends OWLAxiomModifyEvent<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomModifyEvent(OWLObjectPropertyAxiom newAxiom, OWLObjectPropertyAxiom old, OWLObjectProperty owner) {
            super(newAxiom, old, owner);
        }
    }

    /**
     * OWLDataProperty addition and removal events
     */
    public static final class DataPropertyAxiomAddEvent extends OWLAxiomAddEvent<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomAddEvent(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class afterSubDataPropertyOfAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLDataPropertyAxiom, OWLDataProperty> {

        public afterSubDataPropertyOfAxiomRemoveEvent(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }
    public static final class afterSubDataPropertyOfAxiomAddEvent
            extends OWLAxiomAddEvent<OWLDataPropertyAxiom, OWLDataProperty> {

        public afterSubDataPropertyOfAxiomAddEvent(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }
    public static final class DataPropertyAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomRemoveEvent(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class DataPropertyAxiomModifyEvent extends OWLAxiomModifyEvent<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomModifyEvent(OWLDataPropertyAxiom newAxiom, OWLDataPropertyAxiom old, OWLDataProperty owner) {
            super(newAxiom, old, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomAddEvent extends OWLAxiomAddEvent<OWLDataPropertyRangeAxiom, OWLDataProperty> {
        public DataRangeAxiomAddEvent(OWLDataPropertyRangeAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomRemoveEvent extends OWLAxiomRemoveEvent<OWLDataPropertyRangeAxiom, OWLDataProperty> {

        public DataRangeAxiomRemoveEvent(OWLDataPropertyRangeAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomModifyEvent extends OWLAxiomModifyEvent<OWLDataPropertyRangeAxiom, OWLDataProperty> {
        public DataRangeAxiomModifyEvent(OWLDataPropertyRangeAxiom newAxiom, OWLDataPropertyRangeAxiom old, OWLDataProperty owner) {
            super(newAxiom, old, owner);
        }
    }
    
    /**
     * SWRLAPI Rule addition and removal events
     */
    public static final class RuleAddEvent extends OWLAxiomAddEvent<SWRLAPIRule, OWLOntology> {

        public RuleAddEvent(SWRLAPIRule axiom, OWLOntology owner) {
            super(axiom, owner);
        }
    }

    public static final class RuleRemoveEvent extends OWLAxiomRemoveEvent<SWRLAPIRule, OWLOntology> {

        public RuleRemoveEvent(SWRLAPIRule axiom, OWLOntology owner) {
            super(axiom, owner);
        }
    }

    public static final class RuleModifyEvent extends OWLAxiomModifyEvent<SWRLAPIRule, OWLOntology> {

        public RuleModifyEvent(SWRLAPIRule newAxiom, SWRLAPIRule old, OWLOntology owner) {
            super(newAxiom, old, owner);
        }
    }
    
    public static  class ReasonerToggleEvent {
        private final Boolean reasonerStatus;
        private final OWLEntity currentEntity;
        public ReasonerToggleEvent(Boolean status, OWLEntity entity) {
            reasonerStatus = status;
            currentEntity = entity;            
        }
        public Boolean getReasonerStatus() {
            return reasonerStatus;            
        }
        public OWLEntity getCurrentEntity() {
            return currentEntity;
            
        }        
    }


}
