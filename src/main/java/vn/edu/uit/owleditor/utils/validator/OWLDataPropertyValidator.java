package vn.edu.uit.owleditor.utils.validator;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import com.vaadin.data.Validator;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/22/2014.
 */
public class OWLDataPropertyValidator extends AbstractIRIValidator<OWLDataProperty> implements Validator {


    public OWLDataPropertyValidator(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (isDeclared((OWLDataProperty) value)) {
            throw new InvalidValueException(
                    OWLEditorKit.getShortForm((OWLDataProperty) value)
                            + " was already defined in this ontology, please enter a another name");
        }
    }
}
