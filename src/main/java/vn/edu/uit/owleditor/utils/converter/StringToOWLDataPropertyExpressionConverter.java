package vn.edu.uit.owleditor.utils.converter;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public class StringToOWLDataPropertyExpressionConverter extends AbstractStringToOWLObjectConverter<OWLDataPropertyExpression> {

    public StringToOWLDataPropertyExpressionConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLDataPropertyExpression convertToModel(String value, Class<? extends OWLDataPropertyExpression> targetType,
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
    public String convertToPresentation(OWLDataPropertyExpression value, Class<? extends String> targetType,
                                        Locale locale) throws ConversionException {
        if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKitImpl.render(value);
    }

    @Override
    public Class<OWLDataPropertyExpression> getModelType() {
        return OWLDataPropertyExpression.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
