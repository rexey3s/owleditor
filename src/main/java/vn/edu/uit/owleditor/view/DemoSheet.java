package vn.edu.uit.owleditor.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.demo.DemoPanel;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
@VaadinUIScope
@VaadinView(name = DemoSheet.NAME)
public class DemoSheet extends HorizontalLayout implements View {
    public static final String NAME = "Demo";
    private ClassHierarchicalPanel hierarchy;

    private IndividualsSheet.IndividualList individualsList;
    private DemoPanel demoPanel;
    private OWLEditorKit editorKit;
    public DemoSheet() {
        editorKit = OWLEditorUI.getEditorKit();
        initialise();
    }

    private void initialise() {
        hierarchy = new ClassHierarchicalPanel();
        individualsList = new IndividualsSheet.IndividualList();
        demoPanel = new DemoPanel();
        hierarchy.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null) {
                OWLClass clz = hierarchy.getSelectedProperty().getValue();
                if (clz.isOWLThing()) {
                    individualsList.setContainerDataSource(editorKit
                            .getDataFactory()
                            .getOWLIndividualListContainer());
                } else
                    individualsList.setContainerDataSource(editorKit
                            .getDataFactory()
                            .getOWLIndividualListContainer(clz));

            }
        });
        individualsList.addValueChangeListener(event -> {
            if (event.getProperty().getValue() != null && hierarchy.getSelectedProperty().getValue() != null) {
                demoPanel.setSuggestionSource(
                        hierarchy.getSelectedProperty().getValue(), (OWLNamedIndividual) event.getProperty().getValue());
            }
        });
        hierarchy.setImmediate(true);
        individualsList.setImmediate(true);
        VerticalLayout listWrapper = new VerticalLayout();
        listWrapper.addComponents(hierarchy, individualsList);
        listWrapper.setSpacing(true);
        listWrapper.setSizeFull();


        addComponents(listWrapper, demoPanel);
        setExpandRatio(listWrapper, 1.0f);
        setExpandRatio(demoPanel, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        
    }
}
