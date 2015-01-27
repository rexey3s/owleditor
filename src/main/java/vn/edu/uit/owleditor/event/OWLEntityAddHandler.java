package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/12/2014.
 */
public interface OWLEntityAddHandler<T extends OWLLogicalEntity> extends Serializable {
    public static final Method ADD_ENTITY_METHOD = ReflectTools
            .findMethod(OWLEntityAddHandler.class, "addingEntity",
                    OWLLogicalEntity.class);

    public OWLEditorEvent.OWLEntityCreatedEvent addingEntity(T subject);


}