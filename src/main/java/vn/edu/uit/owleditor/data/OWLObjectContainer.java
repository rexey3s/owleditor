package vn.edu.uit.owleditor.data;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/18/14.
 */
public interface OWLObjectContainer {


    public OWLOntology getActiveOntology();

    public void setActiveOntology(OWLOntology ontology);

    /**
     * Provide short form
     *
     * @param entity
     * @return
     */
    public String sf(@Nonnull OWLEntity entity);


}
