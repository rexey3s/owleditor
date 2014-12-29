package vn.edu.uit.owleditor.core.swrlapi;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.swrlapi.core.SWRLAPIOWLOntology;

import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/25/14.
 */
public class AtomSearcher {

    public static Multimap<OWLObjectProperty, Multimap<Set<OWLClass>, Set<SWRLAtom>>> getObjectPropertySuggestion(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new ObjectPropertyDependenciesSearcher(ontology).getObjectPropertySuggestionByClass(owlClass);
    }

    public static JsonObject getObjectPropertySuggestionAsJson(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new ObjectPropertyDependenciesSearcher(ontology).getDataSet(owlClass);
    }
    
    public static Multimap<OWLDataProperty, Multimap<Object, Set<SWRLAtom>>> getDataProperySuggestion(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new DataPropertyDependenciesSearcher(ontology).getDataSuggestionByClass(owlClass);
    }

    public static JsonObject getDataProperySuggestionAsJson(OWLClass owlClass, SWRLAPIOWLOntology ontology) {
        return new DataPropertyDependenciesSearcher(ontology).getDataSet(owlClass);
        
    }
}
