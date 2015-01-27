package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public interface OWLExpressionRemoveHandler extends Serializable {
    public static final Method REMOVE_EXPRESSION_METHOD = ReflectTools
            .findMethod(OWLExpressionRemoveHandler.class, "removingExpression");

    public OWLEditorEvent.OWLAxiomRemoveEvent removingExpression();
}
