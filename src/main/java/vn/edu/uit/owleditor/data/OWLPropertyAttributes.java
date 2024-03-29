package vn.edu.uit.owleditor.data;

import com.vaadin.data.Property;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import java.util.List;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/23/2014.
 */
public abstract class OWLPropertyAttributes {
    public static class IsFunctionalDataProperty implements Property<Boolean> {
        private final OWLFunctionalDataPropertyAxiom functionalDataPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isFunctional;

        public IsFunctionalDataProperty(OWLDataProperty owlDataProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            functionalDataPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLFunctionalDataPropertyAxiom(owlDataProperty);
            isFunctional = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(functionalDataPropertyAxiom);
        }

        private void toggle() {
            if (isFunctional) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), functionalDataPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changes =
                        eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                functionalDataPropertyAxiom);

//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes.getDeclaringClass()) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isFunctional;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isFunctional = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }
    public static class IsFunctionalProperty implements Property<Boolean> {

        private final OWLFunctionalObjectPropertyAxiom functionalObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isFunctional;

        public IsFunctionalProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            functionalObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLFunctionalObjectPropertyAxiom(owlObjectProperty);
            isFunctional = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(functionalObjectPropertyAxiom);
        }

        private void toggle() {
            if (isFunctional) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), functionalObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                functionalObjectPropertyAxiom);

//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isFunctional;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isFunctional = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }

    }

    public static class IsInverseFunctionalProperty implements Property<Boolean> {

        private final OWLInverseFunctionalObjectPropertyAxiom inverseFunctionalObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isInverseFunctional;

        public IsInverseFunctionalProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            inverseFunctionalObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLInverseFunctionalObjectPropertyAxiom(owlObjectProperty);
            isInverseFunctional = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(inverseFunctionalObjectPropertyAxiom);
        }

        private void toggle() {
            if (isInverseFunctional) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), inverseFunctionalObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changeApplied = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                inverseFunctionalObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isInverseFunctional;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isInverseFunctional = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

    public static class IsSymmetricProperty implements Property<Boolean> {

        private final OWLSymmetricObjectPropertyAxiom symmetricObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isSymmetric;

        public IsSymmetricProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            symmetricObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLSymmetricObjectPropertyAxiom(owlObjectProperty);
            isSymmetric = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(symmetricObjectPropertyAxiom);
        }

        private void toggle() {
            if (isSymmetric) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), symmetricObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changes = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                symmetricObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isSymmetric;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isSymmetric = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

    public static class IsASymmetricProperty implements Property<Boolean> {

        private final OWLAsymmetricObjectPropertyAxiom owlaSymmetricObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isASymmetric;

        public IsASymmetricProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            owlaSymmetricObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLAsymmetricObjectPropertyAxiom(owlObjectProperty);
            isASymmetric = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(owlaSymmetricObjectPropertyAxiom);
        }

        private void toggle() {
            if (isASymmetric) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), owlaSymmetricObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changeApplied = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                owlaSymmetricObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isASymmetric;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isASymmetric = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

    public static class IsTransitiveProperty implements Property<Boolean> {

        private final OWLTransitiveObjectPropertyAxiom transitiveObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isTransitive;

        public IsTransitiveProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            transitiveObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLTransitiveObjectPropertyAxiom(owlObjectProperty);
            isTransitive = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(transitiveObjectPropertyAxiom);
        }

        private void toggle() {
            if (isTransitive) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), transitiveObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changeApplied = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                transitiveObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isTransitive;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isTransitive = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

    public static class IsReflexiveProperty implements Property<Boolean> {

        private final OWLReflexiveObjectPropertyAxiom reflexiveObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isReflexive;

        public IsReflexiveProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            reflexiveObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLReflexiveObjectPropertyAxiom(owlObjectProperty);
            isReflexive = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(reflexiveObjectPropertyAxiom);
        }

        private void toggle() {
            if (isReflexive) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), reflexiveObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changes = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                reflexiveObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isReflexive;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isReflexive = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

    public static class IsIrreflexiveProperty implements Property<Boolean> {

        private final OWLIrreflexiveObjectPropertyAxiom irreflexiveObjectPropertyAxiom;
        private final OWLEditorKit eKit;
        private Boolean readOnly = false;
        private Boolean isIrreflexive;

        public IsIrreflexiveProperty(OWLObjectProperty owlObjectProperty) {
            this.eKit = OWLEditorUI.getEditorKit();
            irreflexiveObjectPropertyAxiom =
                    eKit.getOWLDataFactory().getOWLIrreflexiveObjectPropertyAxiom(owlObjectProperty);
            isIrreflexive = containedInOntology();
        }

        public Boolean containedInOntology() {
            return eKit.getActiveOntology().containsAxiom(irreflexiveObjectPropertyAxiom);
        }

        private void toggle() {
            if (isIrreflexive) {
                ChangeApplied applied = eKit.getModelManager().addAxiom
                        (eKit.getActiveOntology(), irreflexiveObjectPropertyAxiom);
                System.out.println(applied);

            } else {
                ChangeApplied changes = eKit.getModelManager().removeAxiom(eKit.getActiveOntology(),
                                irreflexiveObjectPropertyAxiom);
//                eKit.getModelManager().applyChanges(changes);
//                for (OWLOntologyChange change : changes) {
//                    System.out.println(OWLEditorKitImpl.render(change.getAxiom()));
//                }
            }
        }

        @Override
        public Boolean getValue() {
            return isIrreflexive;
        }

        @Override
        public void setValue(Boolean newValue) throws ReadOnlyException {
            if (readOnly) {
                throw new ReadOnlyException("Read-only " + this.getClass());
            }
            isIrreflexive = newValue;
            toggle();
        }

        @Override
        public Class<? extends Boolean> getType() {
            return Boolean.class;
        }


        @Override
        public boolean isReadOnly() {
            return readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            readOnly = newStatus;
        }
    }

}
