package vn.edu.uit.owleditor.event;

import com.vaadin.util.ReflectTools;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/15/14.
 */
public interface ExplanationHandler extends Serializable {
    public static final Method ADD_EXPRESSION_METHOD = ReflectTools
            .findMethod(ExplanationHandler.class, "onGenerateExplanation");

    public ExplanationTree onGenerateExplanation();
}
