package vn.edu.uit.owleditor.event;

import org.semanticweb.owlapi.model.*;
import org.swrlapi.core.SWRLAPIRule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/20/14.
 */
public final class OWLEditorEvent {

    public static abstract class OWLEntityCreatedEvent<T extends OWLLogicalEntity> {

        private final T subject, object;

        public OWLEntityCreatedEvent(@Nonnull T subject,
                                     @Nullable T object) {
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
    public static abstract class OWLEntityRemovedEvent<T extends OWLLogicalEntity> {

        private final T removedObject;

        public OWLEntityRemovedEvent(T removedObject) {
            this.removedObject = removedObject;
        }

        public T getRemovedObject() {
            return removedObject;
        }

    }

    /**
     * OWLClass creation and removal events
     */
    public static final class SubClassCreated extends
            OWLEntityCreatedEvent<OWLClass> {

        public SubClassCreated(OWLClass subClz, OWLClass superClz) {
            super(subClz, superClz);
        }

        public OWLClass getSubClass() {
            return getSubject();
        }

        public OWLClass getSuperClass() {
            return getObject();
        }
    }

    public static final class SiblingClassCreated extends
            OWLEntityCreatedEvent<OWLClass> {

        public SiblingClassCreated(OWLClass owlNamedObject, OWLClass siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLClass getDeclareClass() {
            return getSubject();
        }

        public OWLClass getSiblingClass() {
            return getObject();
        }
    }

    public static final class ClassRemoved extends
            OWLEntityRemovedEvent<OWLClass> {

        public ClassRemoved(OWLClass removedObject) {
            super(removedObject);
        }
    }

    /**
     * OWLObjectProperty creation and removal events
     */
    public static final class SubObjectPropertyCreated extends
            OWLEntityCreatedEvent<OWLObjectProperty> {

        public SubObjectPropertyCreated(OWLObjectProperty child, OWLObjectProperty parent) {
            super(child, parent);
        }

        public OWLObjectProperty getSubProperty() {
            return getSubject();
        }

        public OWLObjectProperty getSuperProperty() {
            return getObject();
        }
    }

    public static final class SiblingObjectPropertyCreated extends
            OWLEntityCreatedEvent<OWLObjectProperty> {

        public SiblingObjectPropertyCreated(OWLObjectProperty owlNamedObject, OWLObjectProperty siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLObjectProperty getDeclareProperty() {
            return getSubject();
        }

        public OWLObjectProperty getSiblingProperty() {
            return getObject();
        }
    }

    public static final class ObjectPropertyRemoved extends
            OWLEntityRemovedEvent<OWLObjectProperty> {

        public ObjectPropertyRemoved(OWLObjectProperty removedObject) {
            super(removedObject);
        }
    }

    /**
     * OWLDataProperty creation and removal events
     */
    public static final class SubDataPropertyCreated extends
            OWLEntityCreatedEvent<OWLDataProperty> {

        public SubDataPropertyCreated(OWLDataProperty child, OWLDataProperty parent) {
            super(child, parent);
        }

        public OWLDataProperty getSubProperty() {
            return getSubject();
        }

        public OWLDataProperty getSuperProperty() {
            return getObject();
        }
    }

    public static final class SiblingDataPropertyCreated extends
            OWLEntityCreatedEvent<OWLDataProperty> {

        public SiblingDataPropertyCreated(OWLDataProperty owlNamedObject, OWLDataProperty siblingObject) {
            super(owlNamedObject, siblingObject);
        }

        public OWLDataProperty getDeclareProperty() {
            return getSubject();
        }

        public OWLDataProperty getSiblingProperty() {
            return getObject();
        }
    }

    public static final class DataPropertyRemoved extends
            OWLEntityRemovedEvent<OWLDataProperty> {

        public DataPropertyRemoved(OWLDataProperty removedObject) {
            super(removedObject);
        }
    }

    public static abstract class OWLAxiomAdded<A extends OWLAxiom, O extends OWLObject> {

        private final A axiom;

        private final O owner;

        public OWLAxiomAdded(A axiom, O owner) {
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

    public static abstract class OWLAxiomRemoved<A extends OWLAxiom, O extends OWLObject> {
        private final A axiom;

        private final O owner;

        public OWLAxiomRemoved(A axiom, O owner) {
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

    public static abstract class OWLAxiomModified<A extends OWLAxiom, O extends OWLObject> {
        private final A newAxiom;
        private final A oldAxiom;
        private final O owner;

        public OWLAxiomModified(A newAxiom, A old, O owner) {
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
    public static final class ClassAxiomAdded extends OWLAxiomAdded<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomAdded(OWLLogicalAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }

    public static final class IndividualAxiomAdded extends OWLAxiomAdded<OWLIndividualAxiom, OWLNamedIndividual> {
        public IndividualAxiomAdded(OWLIndividualAxiom axiom, OWLNamedIndividual owner) {
            super(axiom, owner);
        }
    }

    public static final class ClassAxiomRemoved extends OWLAxiomRemoved<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomRemoved(OWLLogicalAxiom axiom, OWLClass owner) {
            super(axiom, owner);
        }
    }

    public static final class IndividualAxiomRemoved extends OWLAxiomRemoved<OWLIndividualAxiom, OWLNamedIndividual> {

        public IndividualAxiomRemoved(OWLIndividualAxiom axiom, OWLNamedIndividual owner) {
            super(axiom, owner);
        }
    }

    public static final class ClassAxiomModified extends OWLAxiomModified<OWLLogicalAxiom, OWLClass> {

        public ClassAxiomModified(OWLLogicalAxiom newAxiom, OWLLogicalAxiom old, OWLClass owner) {
            super(newAxiom, old, owner);
        }
    }


    public static final class IndividualAxiomModified extends OWLAxiomModified<OWLIndividualAxiom, OWLNamedIndividual> {

        public IndividualAxiomModified(OWLIndividualAxiom newAxiom, OWLIndividualAxiom old, OWLNamedIndividual owner) {
            super(newAxiom, old, owner);
        }
    }

    /**
     * OWLNamedIndividual creation and removal events
     */
    public static final class IndividualAdded extends
            OWLEntityCreatedEvent<OWLNamedIndividual> {
        private final OWLNamedIndividual subject;

        public IndividualAdded(OWLNamedIndividual subject) {
            super(subject, null);
            this.subject = subject;
        }

        public OWLNamedIndividual getIndividual() {
            return subject;
        }

    }

    public static final class IndividualRemoved extends
            OWLEntityRemovedEvent<OWLNamedIndividual> {
        private final OWLNamedIndividual individual;

        public IndividualRemoved(OWLNamedIndividual individual) {
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
    public static final class ObjectPropertyAxiomAdded extends OWLAxiomAdded<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomAdded(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class ObjectPropertyAxiomRemoved extends OWLAxiomRemoved<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomRemoved(OWLObjectPropertyAxiom axiom, OWLObjectProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class ObjectPropertyAxiomModified extends OWLAxiomModified<OWLObjectPropertyAxiom, OWLObjectProperty> {

        public ObjectPropertyAxiomModified(OWLObjectPropertyAxiom newAxiom, OWLObjectPropertyAxiom old, OWLObjectProperty owner) {
            super(newAxiom, old, owner);
        }
    }

    /**
     * OWLDataProperty addition and removal events
     */
    public static final class DataPropertyAxiomAdded extends OWLAxiomAdded<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomAdded(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class DataPropertyAxiomRemoved extends OWLAxiomRemoved<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomRemoved(OWLDataPropertyAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    public static final class DataPropertyAxiomModified extends OWLAxiomModified<OWLDataPropertyAxiom, OWLDataProperty> {

        public DataPropertyAxiomModified(OWLDataPropertyAxiom newAxiom, OWLDataPropertyAxiom old, OWLDataProperty owner) {
            super(newAxiom, old, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomAdded extends OWLAxiomAdded<OWLDataPropertyRangeAxiom, OWLDataProperty> {
        public DataRangeAxiomAdded(OWLDataPropertyRangeAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomRemoved extends OWLAxiomRemoved<OWLDataPropertyRangeAxiom, OWLDataProperty> {

        public DataRangeAxiomRemoved(OWLDataPropertyRangeAxiom axiom, OWLDataProperty owner) {
            super(axiom, owner);
        }
    }

    @SuppressWarnings({"unused"})
    public static final class DataRangeAxiomModified extends OWLAxiomModified<OWLDataPropertyRangeAxiom, OWLDataProperty> {
        public DataRangeAxiomModified(OWLDataPropertyRangeAxiom newAxiom, OWLDataPropertyRangeAxiom old, OWLDataProperty owner) {
            super(newAxiom, old, owner);
        }
    }

    /**
     * SWRLAPI Rule addition and removal events
     */
    public static final class RuleAdded extends OWLAxiomAdded<SWRLAPIRule, OWLOntology> {

        public RuleAdded(SWRLAPIRule axiom, OWLOntology owner) {
            super(axiom, owner);
        }
    }

    public static final class RuleRemoved extends OWLAxiomRemoved<SWRLAPIRule, OWLOntology> {

        public RuleRemoved(SWRLAPIRule axiom, OWLOntology owner) {
            super(axiom, owner);
        }
    }

    public static final class RuleModified extends OWLAxiomModified<SWRLAPIRule, OWLOntology> {

        public RuleModified(SWRLAPIRule newAxiom, SWRLAPIRule old, OWLOntology owner) {
            super(newAxiom, old, owner);
        }
    }


}
