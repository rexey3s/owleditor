package vn.edu.uit.owleditor.utils.validator;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import com.vaadin.data.Validator;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/13/2014.
 */
public abstract class AbstractIRIValidator<T extends OWLEntity> implements Validator {

    protected OWLEditorKit editorKit;

    public AbstractIRIValidator(@Nonnull OWLEditorKit eKit) {
        editorKit = eKit;
    }

    public Boolean isDeclared(T entity) {
        return editorKit.getActiveOntology().isDeclared(entity);
    }


}
