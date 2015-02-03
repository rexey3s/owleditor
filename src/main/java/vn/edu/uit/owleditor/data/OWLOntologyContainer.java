package vn.edu.uit.owleditor.data;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.IndexedContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.annotation.VaadinComponent;
import vn.edu.uit.owleditor.event.OWLEditorEvent;
import vn.edu.uit.owleditor.event.OWLEditorEventBus;

import javax.annotation.PostConstruct;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 2/3/15.
 */
@VaadinComponent
public class OWLOntologyContainer extends IndexedContainer {
    private static final Logger LOG = LoggerFactory.getLogger(OWLOntologyContainer.class);

    public OWLOntologyContainer() {
        addContainerProperty("IRI", String.class, null);
    }

    @PostConstruct
    public void registerSubscriber() {
        OWLEditorEventBus.register(this);
    }

    @Subscribe
    public void handleAddOntologyDocumentEvent(OWLEditorEvent.AddOntologyDocumentEvent event) {
        addItem(event.getOntology());
        getContainerProperty(event.getOntology(), "IRI")
                .setValue(event.getOntology().getOntologyID().getDefaultDocumentIRI().get());
        LOG.info("Adding " + event.getOntology().getOntologyID().getDefaultDocumentIRI().get());
    }
}
