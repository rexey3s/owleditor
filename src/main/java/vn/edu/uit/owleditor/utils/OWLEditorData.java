package vn.edu.uit.owleditor.utils;

/**
 * Created by Chuong Dang on 11/10/14.
 */
public enum OWLEditorData {
    OWLEntityIcon("OWLEntityIcon"),
    OWL2BuiltInDataType("OWL2BuiltInDataType"),
    OWLClassName("Class Name"),
    OWLObjectPropertyName("Object Property Name"),
    OWLDataPropertyName("Data Property Name"),
    OWLNamedIndividualName("Individual Name"),
    OWLFunctionalProperty("Functional"),
    OWLInverseFunctionalObjectProperty("Inverse Functional"),
    OWLSymmetricObjectProperty("Symmetric"),
    OWLAsymmtricObjectProperty("Symmetric"),
    OWLTransitiveObjectProperty("Transitive"),
    OWLReflexiveObjectProperty("Reflexive"),
    OWLIrreflexiveObjectProperty("Irreflexive");


    final String fullname;

    OWLEditorData(String s) {
        fullname = s;
    }

    @Override
    public String toString() {
        return fullname;
    }

}
