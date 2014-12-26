package vn.edu.uit.owleditor.view;

import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.OWLObjectPropertySource;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.view.panel.ObjectPropertyExpressionPanelContainer;
import vn.edu.uit.owleditor.view.panel.ObjectPropertyHierarchicalPanel;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/21/14.
 */
public class ObjectPropertiesSheet extends HorizontalLayout implements Property.ValueChangeListener {

    private final OWLEditorKit editorKit;
    private ObjectPropertyHierarchicalPanel hcLayout;
    private ObjectPropertyAttributes attributes;
    private ObjectPropertyExpressionPanelContainer objectPropertyPanels;


    public ObjectPropertiesSheet() {
        editorKit = ((OWLEditorUI) UI.getCurrent()).getEditorKit();
        initialise();

    }

    private void initialise() {
        hcLayout = new ObjectPropertyHierarchicalPanel(editorKit);
        attributes = new ObjectPropertyAttributes(editorKit);
        objectPropertyPanels = new ObjectPropertyExpressionPanelContainer(editorKit);
        hcLayout.addValueChangeListener(this);
        hcLayout.setImmediate(true);
        VerticalLayout ver = new VerticalLayout();
        ver.addComponents(attributes, objectPropertyPanels);
        ver.setExpandRatio(objectPropertyPanels, 1.0f);
        ver.setSizeFull();

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName("dashboard-tabsheet");
        tabs.addStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
        tabs.addTab(ver, "Property Description");
        tabs.addTab(new Panel(), "Annotation");

        addComponent(hcLayout);
        addComponent(tabs);
        setExpandRatio(hcLayout, 1.0f);
        setExpandRatio(tabs, 3.0f);
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }


    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event.getProperty().getValue() != null) {
            attributes.setPropertyDataSource(hcLayout.getSelectedProperty());
            objectPropertyPanels.setPropertyDataSource(hcLayout.getSelectedProperty());
        }
    }

    public static class ObjectPropertyAttributes extends CssLayout implements Property.Viewer {

        private final OWLEditorKit editorKit;
        CheckBox isFunctional = new CheckBox(OWLEditorData.OWLFunctionalProperty.toString());
        CheckBox isInverse = new CheckBox(OWLEditorData.OWLInverseFunctionalObjectProperty.toString());
        CheckBox isTransitive = new CheckBox(OWLEditorData.OWLTransitiveObjectProperty.toString());
        CheckBox isSymmetric = new CheckBox(OWLEditorData.OWLSymmetricObjectProperty.toString());
        CheckBox isAsymmetric = new CheckBox(OWLEditorData.OWLAsymmtricObjectProperty.toString());
        CheckBox isReflexive = new CheckBox(OWLEditorData.OWLReflexiveObjectProperty.toString());
        CheckBox isIrreflexive = new CheckBox(OWLEditorData.OWLIrreflexiveObjectProperty.toString());
        private OWLObjectPropertySource dataSource;

        public ObjectPropertyAttributes(@Nonnull OWLEditorKit eKit) {
            editorKit = eKit;
            Responsive.makeResponsive(this);
            setWidth("100%");
            addStyleName(ValoTheme.LAYOUT_CARD);
            addStyleName("property-attr-bar");
            addComponents(
                    buildCell(isFunctional), buildCell(isInverse),
                    buildCell(isTransitive),
                    buildCell(isSymmetric), buildCell(isAsymmetric),
                    buildCell(isReflexive), buildCell(isIrreflexive));


        }

        @Override
        public Property getPropertyDataSource() {
            return dataSource;
        }

        @Override
        public void setPropertyDataSource(Property newDataSource) {
            dataSource = (OWLObjectPropertySource) newDataSource;
            if (dataSource.getValue() != null) {
                OWLObjectProperty owlProperty = dataSource.getValue();

                isFunctional.setPropertyDataSource(editorKit.getDataFactory().getOWLIsFunctionalProperty(owlProperty));

                isInverse.setPropertyDataSource(editorKit.getDataFactory().getIsInverseFunctionalProperty(owlProperty));

                isTransitive.setPropertyDataSource(editorKit.getDataFactory().getIsTransitiveProperty(owlProperty));

                isSymmetric.setPropertyDataSource(editorKit.getDataFactory().getIsSymmetricProperty(owlProperty));

                isAsymmetric.setPropertyDataSource(editorKit.getDataFactory().getIsASymmetricProperty(owlProperty));

                isReflexive.setPropertyDataSource(editorKit.getDataFactory().getIsReflexiveProperty(owlProperty));

                isIrreflexive.setPropertyDataSource(editorKit.getDataFactory().getIsIrreflexiveProperty(owlProperty));
            }
        }

        private Component buildCell(Component content) {
            VerticalLayout cell = new VerticalLayout();
            cell.setSizeUndefined();
            cell.setMargin(true);
            cell.addStyleName("attr-cell");
            cell.addComponents(content);
            return cell;
        }


    }


}
