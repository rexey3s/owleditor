package vn.edu.uit.owleditor.view.panel;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.uit.owleditor.data.property.OWLObjectSource;
import vn.edu.uit.owleditor.view.component.HasReasoning;
import vn.edu.uit.owleditor.view.component.InferredLabel;
import vn.edu.uit.owleditor.view.component.OWLLabel;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/4/2014.
 */
public abstract class AbstractExpressionPanel<T extends OWLEntity> extends Panel implements HasReasoning {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractExpressionPanel.class);

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
                Notification.show("Notification", "Please choose an " + dataSource.getType(),
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
    public void addInferredExpressionsWithConsistency() {
        try {
            addInferredExpressions();
        } catch (InconsistentOntologyException inconsistentEx) {
            Notification.show("Inconsistent Ontology", "Please check your ontology axioms!", Notification.Type.ERROR_MESSAGE);
        } catch (NullPointerException nullEx) {
            LOG.info(nullEx.getMessage(), this);
        }
        
    }

    public void addInferredExpressions() throws InconsistentOntologyException, NullPointerException {
    }

    public void setPropertyDataSource(@Nonnull OWLObjectSource newDataSource) {
        if (newDataSource.getValue() != null) {
            this.dataSource = (OWLObjectSource<T>) newDataSource;
            root.removeAllComponents();
            initActionVIEW();
        }

    }


}