package vn.edu.uit.owleditor.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
@RestController
@RequestMapping(value = "/api/")
public class RestAPI {
    private static final int SIZE = 400;
    private static final Logger LOG = LoggerFactory.getLogger(RestAPI.class);
    private final JsonObject thingObject = new JsonObject();
    private final JsonArray thingArray = new JsonArray();
    private final Set<OWLClass> visited = new HashSet<>();
    @Autowired
    OWLEditorKit eKit;

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @RequestMapping(value = "/owl/class", method = RequestMethod.GET)
    public
    @ResponseBody
    String getHierarchy() {
        try {
//            OWLEditorKit eKit = (OWLEditorKit) session.getAttribute("OWLEditorKit");
            Assert.notNull(eKit, "Editor Kit should not be null");
            Assert.notNull(eKit.getActiveOntology(), "Your ontology has not been ready yet!");
            thingObject.addProperty("name", "Thing");
            thingObject.add("children", thingArray);
            eKit.getActiveOntology().accept(initPopulationEngine(eKit.getActiveOntology()));
            return thingObject.toString();
        } catch (NullPointerException ex) {
            LOG.error(ex.getMessage());
        }
        return "No service";
    }

    private void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
        visited.add(child);
        final JsonObject childObject = new JsonObject();
        childObject.addProperty("name", OWLEditorKitImpl.getShortForm(child));
        childObject.addProperty("size", randInt(2, 10) * SIZE);

        if (parent != null && parentObject.has("children")) {

            parentObject.get("children").getAsJsonArray().add(childObject);

        } else {

            if (EntitySearcher.getSuperClasses(child, ontology).size() == 0)
                thingArray.add(childObject);
        }
        Collection<OWLClassExpression> childs = EntitySearcher.getSubClasses(child, ontology);
        if (childs.size() > 0) {
            childObject.remove("size");
            final JsonArray childArray = new JsonArray();
            childObject.add("children", childArray);
            childs.forEach(
                    ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                        public void visit(OWLClass owlClass) {
                            recursive(ontology, owlClass, child, childObject);
                        }
                    })
            );
        }
    }

    private OWLObjectVisitorAdapter initPopulationEngine(OWLOntology activeOntology) {
        return new OWLObjectVisitorAdapter() {
            @Override
            public void visit(@Nonnull OWLOntology ontology) {
                ontology.getClassesInSignature()
                        .stream().filter(c -> !c.isOWLThing())
                        .forEach(c -> c.accept(this));
            }

            @Override
            public void visit(@Nonnull OWLClass owlClass) {
                if (!visited.contains(owlClass)) {
                    recursive(activeOntology, owlClass, null, null);
                }
            }
        };
    }

}
