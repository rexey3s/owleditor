package vn.edu.uit.owleditor.utils.converter;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication
 * @date 11/8/14.
 */
public class StringToOWLClassConverter extends AbstractStringToOWLObjectConverter<OWLClass> {


    public StringToOWLClassConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLClass convertToModel(String value, Class<? extends OWLClass> targetType,
                                   Locale locale) throws ConversionException {
        if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports "
                    + getModelType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        if (value.matches("[^a-zA-Z0-9]_ ")) {
            throw new ConversionException("Please enter only alphanumeric characters");
        }

        return editorKit.getOWLDataFactory()
                .getOWLClass(value.replace(" ", "_"), editorKit.getPrefixManager());
    }

    @Override
    public String convertToPresentation(OWLClass value, Class<? extends String> targetType,
                                        Locale locale) throws ConversionException {
        if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKit.getShortForm(value);
    }

    @Override
    public Class<OWLClass> getModelType() {
        return OWLClass.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
