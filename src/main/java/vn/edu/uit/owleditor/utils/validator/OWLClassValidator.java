package vn.edu.uit.owleditor.utils.validator;

import com.vaadin.data.Validator;
import org.semanticweb.owlapi.model.OWLClass;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/7/14.
 */
public class OWLClassValidator extends AbstractIRIValidator<OWLClass> implements Validator {

    public OWLClassValidator(@Nonnull OWLEditorKit kit) {
        super(kit);
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (editorKit.getActiveOntology().isDeclared((OWLClass) value)) {
            throw new InvalidValueException(
                    OWLEditorKitImpl.getShortForm((OWLClass) value)
                            + " was already defined in this ontology, " +
                            "please enter a another name");
        }
    }


}

