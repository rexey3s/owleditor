package vn.edu.uit.owleditor.view.diagram;

import com.google.gson.JsonObject;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import vn.edu.uit.owleditor.view.Reloadable;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/25/14.
 */
public abstract class AbstractDiagramLayout<T extends OWLEntity> extends VerticalLayout implements Reloadable {
    protected final DnDTree graph = new DnDTree();
    protected final Set<T> visited = new HashSet<>();
    protected final OWLOntology activeOntology;

    public AbstractDiagramLayout(@Nonnull OWLOntology ontology) {
        activeOntology = ontology;
        final HorizontalLayout diagramContainer = new HorizontalLayout();
        addStyleName("diagram-container");
        diagramContainer.addComponent(graph);
        diagramContainer.setSizeFull();
        addComponent(diagramContainer);
        setSizeFull();


    }

    protected abstract void recursive(OWLOntology ontology, T child, T parent, JsonObject parentObject);

    protected abstract OWLObjectVisitor initPopulationEngine(OWLOntology activeOntology, JsonObject topObject);
}