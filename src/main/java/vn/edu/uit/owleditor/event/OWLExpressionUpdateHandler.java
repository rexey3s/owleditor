package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public interface OWLExpressionUpdateHandler<EXPRESSION extends OWLObject> extends Serializable {
    public static final Method MODIFYING_EXPRESSION_METHOD = ReflectTools
            .findMethod(OWLExpressionUpdateHandler.class, "modifyingExpression",
                    OWLObject.class);

    public OWLEditorEvent.OWLAxiomModified modifyingExpression(EXPRESSION expression);
}
