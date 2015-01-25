package vn.edu.uit.owleditor.view.component;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObject;
import vn.edu.uit.owleditor.data.property.OWLObjectSource;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/4/2014.
 */
public abstract class AbstractExpressionPanel<T extends OWLObject> extends Panel implements HasReasoning {

    protected final VerticalLayout root = new VerticalLayout();
    protected OWLObjectSource<T> dataSource = initDataSource();


    public AbstractExpressionPanel(String caption) {

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setCaption(caption);
        setContent(root);
        setHeight("100%");
        root.setSpacing(true);
    }

    protected abstract OWLObjectSource<T> initDataSource();

    protected abstract void initActionADD();

    protected abstract void initActionVIEW();

    public MenuBar.Command getAddCmd() {
        return add -> {
            if (dataSource.getValue() != null) {
                initActionADD();
            } else {
                Notification.show("Notification",
                        "Please choose an " + dataSource.getType(),
                        Notification.Type.WARNING_MESSAGE);
            }
        };
    }

    public void addMoreExpression(Component component) {
        root.addComponent(component);
    }

    public void removeExpression(OWLObject owlObject) {
        Iterator<Component> labels = root.iterator();
        while (labels.hasNext()) {
            Component label = labels.next();
            if (label instanceof OWLLabel) {
                if (((OWLLabel) label).getPropertyDataSource().getValue().equals(owlObject)) {
                    labels.remove();
                    root.removeComponent(label);
                }
            }
        }
    }
    public void removeInferredExpressions() {
        Iterator<Component> labels = root.iterator();
        while (labels.hasNext()) {
            Component label = labels.next();
            if (label instanceof InferredLabel) {               
                labels.remove();
                root.removeComponent(label);                
            }
        }
        
    }
    public void addInferredExpressions() { }
    
    public void setPropertyDataSource(@Nonnull Property property) {
        if (property.getValue() != null) {
            dataSource = (OWLObjectSource<T>) property;
            root.removeAllComponents();
            initActionVIEW();
        }
    }
}