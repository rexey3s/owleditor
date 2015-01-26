package vn.edu.uit.owleditor.view.panel;

import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLLogicalEntity;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.OWLLogicalEntitySource;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/26/2014.
 */
public abstract class AbstractHierarchyPanel<T extends OWLLogicalEntity> extends VerticalLayout implements
        Action.Handler, Property.ValueChangeNotifier {

    protected Label caption = new Label();
    protected OWLEditorKit editorKit;
    public AbstractHierarchyPanel() {
        editorKit = OWLEditorUI.getEditorKit();
    }

    @Override
    public void setCaption(String cpt) {
        super.setCaption(null);
        caption.setValue(cpt);
        caption.setSizeUndefined();
    }

    public abstract Property<T> getSelectedProperty();

    abstract void handleSubNodeCreation();

    abstract void handleSiblingNodeCreation();

    abstract void handleRemovalNode();

    static interface TreeKit<T extends OWLLogicalEntitySource> extends Property.ValueChangeListener {

        public T getCurrentProperty();
    }
    
    protected void startReasonerClickListener() {
        editorKit.setReasonerStatus(!editorKit.getReasonerStatus());
        OWLEditorEventBus.post(new OWLEditorEvent.ReasonerToggleEvent(
                editorKit.getReasonerStatus(), getSelectedProperty().getValue()));

    }
}
