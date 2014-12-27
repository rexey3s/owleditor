package vn.edu.uit.owleditor.utils.converter;

import com.vaadin.data.util.converter.Converter;
import org.semanticweb.owlapi.model.OWLDataRange;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/5/2014.
 */
public class StringToDataRangeConverter extends AbstractStringToOWLObjectConverter<OWLDataRange> {

    public StringToDataRangeConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLDataRange convertToModel(String value, Class<? extends OWLDataRange> targetType,
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

        return null;
    }

    @Override
    public String convertToPresentation(OWLDataRange value, Class<? extends String> targetType,
                                        Locale locale) throws Converter.ConversionException {
        if (targetType != getPresentationType()) {
            throw new Converter.ConversionException("Converter only supports "
                    + getPresentationType().getName() + " (targetType was "
                    + targetType.getName() + ")");
        }
        if (value == null) {
            return null;
        }
        return OWLEditorKitImpl.render(value);
    }

    @Override
    public Class<OWLDataRange> getModelType() {
        return OWLDataRange.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
