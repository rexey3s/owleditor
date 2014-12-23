package vn.edu.uit.owleditor.utils.validator;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import com.vaadin.data.Validator;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/21/14.
 */
public class OWLObjectPropertyValidator extends AbstractIRIValidator<OWLObjectProperty> implements Validator {

    public OWLObjectPropertyValidator(@Nonnull OWLEditorKit kit) {
        super(kit);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (isDeclared((OWLObjectProperty) value)) {
            throw new InvalidValueException(
                    OWLEditorKit.getShortForm((OWLObjectProperty) value)
                            + " was already defined in this ontology, please enter a another name");
        }
    }
}
