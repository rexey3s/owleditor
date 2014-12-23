package vn.edu.uit.owleditor.utils.converter;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import com.vaadin.data.util.converter.Converter;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/13/14.
 */
public abstract class AbstractStringToOWLObjectConverter<T extends OWLObject>
        implements Converter<String, T> {

    protected final OWLEditorKit editorKit;

    public AbstractStringToOWLObjectConverter(@Nonnull OWLEditorKit eKit) {
        this.editorKit = eKit;
    }
}
