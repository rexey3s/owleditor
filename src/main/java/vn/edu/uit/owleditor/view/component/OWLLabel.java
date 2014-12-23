package vn.edu.uit.owleditor.view.component;

import vn.edu.uit.owleditor.data.property.OWLObjectSource;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public interface OWLLabel<T extends OWLObject> {
    OWLObjectSource<T> getPropertyDataSource();

}
