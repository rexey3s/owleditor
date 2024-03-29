package vn.edu.uit.owleditor.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;
import vn.edu.uit.owleditor.view.diagram.AbstractDiagramLayout;
import vn.edu.uit.owleditor.view.diagram.DnDTree;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/23/14.
 */
@UIScope
@SpringView(name = DiagramSheet.NAME)
public class DiagramSheet extends VerticalLayout implements View {
    public static final String NAME = "Diagrams";
    private static final int SIZE = 400;
    private static Logger LOG = LoggerFactory.getLogger(DnDTree.class);
    private final TabSheet tabSheet = new TabSheet();
    private final ClassDnDTree classDnDTree;
    private final EntityDnDTree entityDnDTree;
    private final InferredEntityDnDTree afterClassify;
    //    private final ClassHierarchicalTree clzzTreeSource;
    public DiagramSheet() {
        classDnDTree = new ClassDnDTree(OWLEditorUI.getEditorKit().getActiveOntology());
        entityDnDTree = new EntityDnDTree(OWLEditorUI.getEditorKit().getActiveOntology());
        afterClassify = new InferredEntityDnDTree(OWLEditorUI.getEditorKit(), OWLEditorUI.getEditorKit().getActiveOntology());
//        clzzTreeSource = new ClassHierarchicalTree(OWLEditorUI.getEditorKit());
        DnDTree classTreeDiagram1 = new DnDTree();
        tabSheet.addTab(classDnDTree, "Classes");
        tabSheet.addTab(entityDnDTree, "Classes and Individuals");
        tabSheet.addTab(afterClassify, "Sau khi phân loại");
//        classTreeDiagram1.setData(clzzTreeSource.refreshTree());
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

    private Component createWrapper(DnDTree tree) {
        final HorizontalLayout diagramContainer = new HorizontalLayout();
        addStyleName("diagram-container");
        diagramContainer.addComponent(tree);
        diagramContainer.setSizeFull();
        addComponent(diagramContainer);
        setSizeFull();
        return diagramContainer;
    }

    public void reloadAll() {
        classDnDTree.reload();
        entityDnDTree.reload();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        
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
            childObject.addProperty("name", OWLEditorKitImpl.getShortForm(child));
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
                individuals.stream().filter(OWLIndividual::isNamed).forEach(i -> {
                    final JsonObject iObject = new JsonObject();
                    iObject.addProperty("name", "Individual: " + OWLEditorKitImpl.getShortForm(i.asOWLNamedIndividual()));
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
            graph.setData(thingObject.toString());
        }

        @Override
        protected void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
            visited.add(child);
            final JsonObject childObject = new JsonObject();
            childObject.addProperty("name", OWLEditorKitImpl.getShortForm(child));
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

    public static class InferredEntityDnDTree extends AbstractDiagramLayout<OWLClass> {
        private final JsonObject thingObject = new JsonObject();
        private final OWLEditorKit editorKit;
        private JsonArray thingArray;

        public InferredEntityDnDTree(@Nonnull OWLEditorKit editorKit, @Nonnull OWLOntology ontology) {
            super(ontology);
            this.editorKit = editorKit;
            thingObject.addProperty("name", "Thing");
            thingObject.add("children", thingArray);
            thingArray = new JsonArray();
            reload();
        }

        @Override
        protected void recursive(OWLOntology ontology, OWLClass child, OWLClass parent, JsonObject parentObject) {
            visited.add(child);
            final JsonObject childObject = new JsonObject();
            childObject.addProperty("name", OWLEditorKitImpl.getShortForm(child));
            childObject.addProperty("type", "class");
            childObject.addProperty("size", randInt(2, 10) * SIZE);

            if (parent != null && parentObject.has("children")) {

                parentObject.get("children").getAsJsonArray().add(childObject);

            } else if (parentObject.has("children")) {
                if (EntitySearcher.getSuperClasses(child, ontology).size() == 0)
                    parentObject.get("children").getAsJsonArray().add(childObject);

            }
            Collection<OWLClassExpression> childs = EntitySearcher.getSubClasses(child, ontology);

            Set<OWLNamedIndividual> individuals = editorKit.getReasoner().getInstances(child, true).getFlattened();


            if (individuals.size() > 0 || childs.size() > 0) {
                final JsonArray childArray = new JsonArray();
                childObject.add("children", childArray);
                individuals.stream().forEach(i -> {
                    final JsonObject iObject = new JsonObject();
                    iObject.addProperty("name", "Individual: " + OWLEditorKitImpl.getShortForm(i));
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
            editorKit.getReasoner().flush();
            visited.clear();
            thingObject.remove("children");
            thingArray = new JsonArray();
            thingObject.add("children", thingArray);
            activeOntology.accept(initPopulationEngine(activeOntology, thingObject));
            graph.setData(thingObject.toString());
        }
    }

}
