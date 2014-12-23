package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public interface OWLPropertyAssertionAddHandler<EXPRESSION extends OWLPropertyExpression, OBJECT extends OWLObject> extends Serializable {
    public static final Method ADD_RESTRICTION_METHOD = ReflectTools
            .findMethod(OWLPropertyAssertionAddHandler.class, "addingRestriction",
                    OWLProperty.class, OWLObject.class);

    public OWLEditorEvent.OWLAxiomAdded addingRestriction(EXPRESSION expression, OBJECT restriction);
}