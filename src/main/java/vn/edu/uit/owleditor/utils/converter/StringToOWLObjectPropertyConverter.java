package vn.edu.uit.owleditor.utils.converter;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/21/14.
 */
public class StringToOWLObjectPropertyConverter extends
        AbstractStringToOWLObjectConverter<OWLObjectProperty> {


    public StringToOWLObjectPropertyConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLObjectProperty convertToModel(String value,
                                            Class<? extends OWLObjectProperty> targetType,
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
                .getOWLObjectProperty(value.replace(" ", "_"), editorKit.getPrefixManager());
    }

    @Override
    public String convertToPresentation(OWLObjectProperty value,
                                        Class<? extends String> targetType,
                                        Locale locale) throws ConversionException {

        if (targetType != getPresentationType()) {
            throw new ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKitImpl.getShortForm(value);
    }

    @Override
    public Class<OWLObjectProperty> getModelType() {
        return OWLObjectProperty.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
