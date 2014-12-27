package vn.edu.uit.owleditor.utils.converter;

import com.vaadin.data.util.converter.Converter;
import org.semanticweb.owlapi.model.OWLDataProperty;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/22/2014.
 */
public class StringToOWLDataPropertyConverter extends AbstractStringToOWLObjectConverter<OWLDataProperty> {


    public StringToOWLDataPropertyConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLDataProperty convertToModel(String value,
                                          Class<? extends OWLDataProperty> targetType,
                                          Locale locale) throws Converter.ConversionException {
        if (targetType != getModelType()) {
            throw new Converter.ConversionException("Converter only supports "
                    + getModelType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        if (value.matches("[^a-zA-Z0-9]_ ")) {
            throw new Converter.ConversionException("Please enter only alphanumeric characters");
        }

        return editorKit.getOWLDataFactory()
                .getOWLDataProperty(value.replace(" ", "_"), editorKit.getPrefixManager());
    }

    @Override
    public String convertToPresentation(OWLDataProperty value,
                                        Class<? extends String> targetType,
                                        Locale locale) throws Converter.ConversionException {
        if (targetType != getPresentationType()) {
            throw new Converter.ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKitImpl.getShortForm(value);
    }

    @Override
    public Class<OWLDataProperty> getModelType() {
        return OWLDataProperty.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
