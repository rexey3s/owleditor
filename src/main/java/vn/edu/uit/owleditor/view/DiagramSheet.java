package vn.edu.uit.owleditor.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.diagram.AbstractDiagramLayout;
import vn.edu.uit.owleditor.view.diagram.DnDTree;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Random;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
@UIScope
@VaadinComponent
public class DiagramSheet extends VerticalLayout {
    private static final int SIZE = 400;
    private static Logger LOG = LoggerFactory.getLogger(DnDTree.class);
    private final TabSheet tabSheet = new TabSheet();
    private final ClassDnDTree classDnDTree;
    private final EntityDnDTree entityDnDTree;
    @Autowired
    OWLEditorKit editorKit;
    
    public DiagramSheet() {
//        editorKit = OWLEditorUI.getEditorKit();
        classDnDTree = new ClassDnDTree(editorKit.getActiveOntology());
        entityDnDTree = new EntityDnDTree(editorKit.getActiveOntology());

        tabSheet.addTab(classDnDTree, "Classes");
        tabSheet.addTab(entityDnDTree, "Entities");
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.setSizeFull();
        setMargin(true);
        addComponent(tabSheet);
        setSizeFull();
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void reloadAll() {
        classDnDTree.reload();
        entityDnDTree.reload();
    }


    public static class EntityDnDTree extends AbstractDiagramLayout<OWLClass> {
        private final JsonObject thingObject = new JsonObject();
        private JsonArray thingArray;

        public EntityDnDTree(@Nonnull OWLOntology ontology) {
            super(ontology);
            thingObject.addProperty("name", "Thing");
            thingObject.add("children", thingArray);
            thingArray = new JsonArray();
            reload();
        }

        @Override
        protected void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
            visited.add(child);
            final JsonObject childObject = new JsonObject();
            childObject.addProperty("name", OWLEditorKit.getShortForm(child));
            childObject.addProperty("type", "class");
            childObject.addProperty("size", randInt(2, 10) * SIZE);

            if (parent != null && parentObject.has("children")) {

                parentObject.get("children").getAsJsonArray().add(childObject);

            } else if (parentObject.has("children")) {
                if (EntitySearcher.getSuperClasses(child, ontology).size() == 0)
                    parentObject.get("children").getAsJsonArray().add(childObject);

            }
            Collection<OWLClassExpression> childs = EntitySearcher.getSubClasses(child, ontology);

            Collection<OWLIndividual> individuals = EntitySearcher.getIndividuals(child, ontology);


            if (individuals.size() > 0 || childs.size() > 0) {
                final JsonArray childArray = new JsonArray();
                childObject.add("children", childArray);
                individuals.stream().filter(i -> i.isNamed()).forEach(i -> {
                    final JsonObject iObject = new JsonObject();
                    iObject.addProperty("name", "Individual: " + OWLEditorKit.getShortForm(i.asOWLNamedIndividual()));
                    iObject.addProperty("type", "individual");
                    childArray.add(iObject);
                });

                childObject.remove("size");
                childs.forEach(
                        ce -> ce.accept(new OWLClassExpressionVisitorAdapter() {
                            public void visit(OWLClass owlClass) {
                                recursive(ontology, owlClass, child, childObject);
                            }
                        })
                );
            }

        }

        @Override
        protected OWLObjectVisitor initPopulationEngine(OWLOntology activeOntology, JsonObject topObject) {
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
                        recursive(activeOntology, owlClass, null, thingObject);
                    }
                }
            };
        }

        @Override
        public void reload() {
            visited.clear();
            thingObject.remove("children");
            thingArray = new JsonArray();
            thingObject.add("children", thingArray);

            activeOntology.accept(initPopulationEngine(activeOntology, thingObject));
            LOG.info(thingObject.toString());
            graph.setData(thingObject.toString());
        }
    }

    public static class ClassDnDTree extends AbstractDiagramLayout<OWLClass> {
        private final JsonObject thingObject = new JsonObject();
        private JsonArray thingArray;

        public ClassDnDTree(@Nonnull OWLOntology ontology) {
            super(ontology);
            thingObject.addProperty("name", "Thing");
            thingObject.add("children", thingArray);
            thingArray = new JsonArray();
            reload();
        }

        @Override
        public void reload() {
            visited.clear();
            thingObject.remove("children");
            thingArray = new JsonArray();
            thingObject.add("children", thingArray);

            activeOntology.accept(initPopulationEngine(activeOntology, thingObject));
            LOG.info(thingObject.toString());
            graph.setData(thingObject.toString());
        }

        @Override
        protected void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
            visited.add(child);
            final JsonObject childObject = new JsonObject();
            childObject.addProperty("name", OWLEditorKit.getShortForm(child));
            childObject.addProperty("size", randInt(2, 10) * SIZE);

            if (parent != null && parentObject.has("children")) {

                parentObject.get("children").getAsJsonArray().add(childObject);

            } else if (parentObject.has("children")) {
                if (EntitySearcher.getSuperClasses(child, ontology).size() == 0)
                    parentObject.get("children").getAsJsonArray().add(childObject);

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

        protected OWLObjectVisitorAdapter initPopulationEngine(OWLOntology activeOntology, JsonObject thingObject) {
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
                        recursive(activeOntology, owlClass, null, thingObject);
                    }
                }
            };
        }
    }


}
