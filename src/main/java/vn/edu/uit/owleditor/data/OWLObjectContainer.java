package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import vn.edu.uit.owleditor.utils.exception.OWLEditorException;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 11/18/14.
 */
public interface OWLObjectContainer {


    public OWLOntology getActiveOntology();

    public void setActiveOntology(OWLOntology ontology) throws OWLEditorException.DuplicatedActiveOntologyException;

    /**
     * Provide short form
     *
     * @param entity
     * @return
     */
    public String sf(@Nonnull OWLEntity entity);


}
