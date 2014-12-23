package vn.edu.uit.owleditor.utils.converter;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public class StringToOWLObjectPropertyExpressionConverter extends AbstractStringToOWLObjectConverter<OWLObjectPropertyExpression> {

    public StringToOWLObjectPropertyExpressionConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLObjectPropertyExpression convertToModel(String value, Class<? extends OWLObjectPropertyExpression> targetType,
                                                      Locale locale) throws ConversionException {
        if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports "
                    + getModelType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        // Found any way to parse yet
        return null;
    }

    @Override
    public String convertToPresentation(OWLObjectPropertyExpression value, Class<? extends String> targetType,
                                        Locale locale) throws ConversionException {
        if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKit.render(value);
    }

    @Override
    public Class<OWLObjectPropertyExpression> getModelType() {
        return OWLObjectPropertyExpression.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
