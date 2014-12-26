package vn.edu.uit.owleditor.core.swrlapi;

import org.swrlapi.builtins.arguments.*;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/26/14.
 */
public class SWRLBuilInArgumentVisitorExAdapter<T> implements SWRLBuiltInArgumentVisitorEx<T> {
    @Nonnull
    private final T defaultReturnValue;

    /**
     * @param defaultReturnValue default return value
     */
    public SWRLBuilInArgumentVisitorExAdapter(@Nonnull T defaultReturnValue) {
        this.defaultReturnValue = defaultReturnValue;
    }

    @Override
    public T visit(SWRLClassBuiltInArgument swrlClassBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLNamedIndividualBuiltInArgument swrlNamedIndividualBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLObjectPropertyBuiltInArgument swrlObjectPropertyBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLDataPropertyBuiltInArgument swrlDataPropertyBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLAnnotationPropertyBuiltInArgument swrlAnnotationPropertyBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLDatatypeBuiltInArgument swrlDatatypeBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLLiteralBuiltInArgument swrlLiteralBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLVariableBuiltInArgument swrlVariableBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SWRLMultiValueVariableBuiltInArgument swrlMultiValueVariableBuiltInArgument) {
        return defaultReturnValue;
    }

    @Override
    public T visit(SQWRLCollectionVariableBuiltInArgument sqwrlCollectionVariableBuiltInArgument) {
        return defaultReturnValue;
    }
}
