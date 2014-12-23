package vn.edu.uit.owleditor.utils.converter;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/30/2014.
 */
public class StringToNamedIndividualConverter extends AbstractStringToOWLObjectConverter<OWLNamedIndividual> {

    public StringToNamedIndividualConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLNamedIndividual convertToModel(String value, Class<? extends OWLNamedIndividual> targetType,
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
                .getOWLNamedIndividual(value.replace(" ", "_"), editorKit.getPrefixManager());
    }

    @Override
    public String convertToPresentation(OWLNamedIndividual value, Class<? extends String> targetType,
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
    public Class<OWLNamedIndividual> getModelType() {
        return OWLNamedIndividual.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
