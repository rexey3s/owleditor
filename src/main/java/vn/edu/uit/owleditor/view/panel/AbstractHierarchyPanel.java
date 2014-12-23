package vn.edu.uit.owleditor.view.panel;

import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.OWLLogicalEntitySource;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/26/2014.
 */
public abstract class AbstractHierarchyPanel<T extends OWLLogicalEntity> extends VerticalLayout implements
        Action.Handler, Property.ValueChangeNotifier {

    protected final OWLEditorKit editorKit;
    protected Label caption = new Label();

    public AbstractHierarchyPanel(@Nonnull OWLEditorKit eKit) {
        editorKit = eKit;
    }

    @Override
    public void setCaption(String cpt) {
        super.setCaption(null);
        caption.setValue(cpt);
    }

    public abstract Property<T> getSelectedProperty();

    abstract void handleSubNodeCreation();

    abstract void handleSiblingNodeCreation();

    abstract void handleRemovalNode();

    static interface TreeKit<T extends OWLLogicalEntitySource> extends Property.ValueChangeListener {

        public T getCurrentProperty();
    }
}
