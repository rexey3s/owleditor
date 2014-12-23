package vn.edu.uit.owleditor.utils.converter;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClassExpression;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public class StringToOWLClassExpressionConverter extends AbstractStringToOWLObjectConverter<OWLClassExpression> {


    public StringToOWLClassExpressionConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLClassExpression convertToModel(String value, Class<? extends OWLClassExpression> targetType,
                                             Locale locale) throws ConversionException {
        if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports "
                    + getModelType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        editorKit.getParser().setStringToParse(value);
        return editorKit.getParser().parseClassExpression();
    }

    @Override
    public String convertToPresentation(OWLClassExpression value, Class<? extends String> targetType,
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
    public Class<OWLClassExpression> getModelType() {
        return OWLClassExpression.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
