package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public interface OWLExpressionAddHandler<EXPRESSION extends OWLObject> extends Serializable {
    public static final Method ADD_EXPRESSION_METHOD = ReflectTools
            .findMethod(OWLExpressionAddHandler.class, "addingExpression",
                    OWLObject.class);

    public OWLEditorEvent.OWLAxiomAdded addingExpression(EXPRESSION expression);
}
