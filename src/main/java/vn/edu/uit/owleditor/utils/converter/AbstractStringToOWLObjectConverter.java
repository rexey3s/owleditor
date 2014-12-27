package vn.edu.uit.owleditor.utils.converter;

import com.vaadin.data.util.converter.Converter;
import org.semanticweb.owlapi.model.OWLObject;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/13/14.
 */
public abstract class AbstractStringToOWLObjectConverter<T extends OWLObject>
        implements Converter<String, T> {

    protected final OWLEditorKit editorKit;

    public AbstractStringToOWLObjectConverter(@Nonnull OWLEditorKit eKit) {
        this.editorKit = eKit;
    }
}
