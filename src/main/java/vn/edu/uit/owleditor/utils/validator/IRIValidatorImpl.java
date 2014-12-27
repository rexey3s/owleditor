package vn.edu.uit.owleditor.utils.validator;

import com.vaadin.data.Validator;
import org.semanticweb.owlapi.model.OWLEntity;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/12/2014.
 */
public class IRIValidatorImpl<T extends OWLEntity> implements Validator {
    private final OWLEditorKit editorKit;

    public IRIValidatorImpl(@Nonnull OWLEditorKit eKit) {
        editorKit = eKit;
    }

    @Override
    public void validate(Object o) throws InvalidValueException {
        if (editorKit.getActiveOntology().isDeclared((T) o)) {
            throw new InvalidValueException(
                    OWLEditorKitImpl.getShortForm((T) o)
                            + " was already defined in this ontology, " +
                            "please enter a another name");
        }
    }
}
