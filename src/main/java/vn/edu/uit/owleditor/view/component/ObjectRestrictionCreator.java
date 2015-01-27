package vn.edu.uit.owleditor.view.component;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.semanticweb.owlapi.model.OWLClassExpression;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.property.OWLClassExpressionSource;
import vn.edu.uit.owleditor.utils.EditorUtils;
import vn.edu.uit.owleditor.view.panel.ClassHierarchicalPanel;
import vn.edu.uit.owleditor.view.panel.ObjectPropertyHierarchicalPanel;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/6/2014.
 */
public class ObjectRestrictionCreator extends VerticalLayout {

    private final OWLClassExpressionSource dataSource = new OWLClassExpressionSource();
    private final HorizontalLayout restrictionWrapper = new HorizontalLayout();
    private final OWLEditorKit editorKit;
    private ObjectPropertyHierarchicalPanel objectPropertyHierarchy;
    private ClassHierarchicalPanel classHierarchy;
    private ComboBox wordBox;
    private TextField numberField;

    public ObjectRestrictionCreator() {

        initialize();
        editorKit = OWLEditorUI.getEditorKit();
    }

    private void initialize() {
        classHierarchy = new ClassHierarchicalPanel();
        objectPropertyHierarchy = new ObjectPropertyHierarchicalPanel();
        wordBox = new ComboBox();
        numberField = new TextField();

        HorizontalLayout hcWrapper = new HorizontalLayout();
        hcWrapper.addComponents(objectPropertyHierarchy, classHierarchy);
        hcWrapper.setSizeFull();
        wordBox.setWidth("100%");
        numberField.setWidth("100%");
        restrictionWrapper.setWidth("100%");
        restrictionWrapper.addComponents(wordBox, numberField);
        restrictionWrapper.setExpandRatio(wordBox, 1);
        restrictionWrapper.setExpandRatio(numberField, 1);
        addComponents(hcWrapper, restrictionWrapper);
        setExpandRatio(hcWrapper, 1);
//        setExpandRatio(restrictionWrapper, 1);
        setSizeFull();
//        wordBox.addValueChangeListener(valueChangeEvent -> {
//            if (ObjectRestrictionVocabulary.OBJECT_MIN_CARDINALITY.toString().equals(valueChangeEvent.getProperty())
//                    || ObjectRestrictionVocabulary.OBJECT_MAX_CARDINALITY.toString().equals(valueChangeEvent.getProperty())
//                    || ObjectRestrictionVocabulary.OBJECT_EXACT_CARDINALITY.toString().equals(valueChangeEvent.getProperty())) {
//                restrictionWrapper.addComponent(numberField);
//            } else {
//                restrictionWrapper.removeComponent(numberField);
//            }
//
//        });
        numberField.setConverter(new StringToIntegerConverter());
        wordBox.setNullSelectionAllowed(false);
        wordBox.addItems(
                ObjectRestrictionVocabulary.OBJECT_ALL_VALUES_FROM,
                ObjectRestrictionVocabulary.OBJECT_SOME_VALUES_FROM,
                ObjectRestrictionVocabulary.OBJECT_MIN_CARDINALITY,
                ObjectRestrictionVocabulary.OBJECT_MAX_CARDINALITY,
                ObjectRestrictionVocabulary.OBJECT_EXACT_CARDINALITY
        );
    }

    public OWLClassExpressionSource getDataProperty() {
        dataSource.setValue(buildExpression());
        return dataSource;
    }

    private OWLClassExpression buildExpression() {
        EditorUtils.checkNotNull(objectPropertyHierarchy.getSelectedItem().getValue(),
                "Please select an Object Property");
        EditorUtils.checkNotNull(classHierarchy.getSelectedItem().getValue(),
                "Please select a Class");
//        EditorUtils.checkNotNull(wordBox.getValue(),"Please select your quantizer");
        if (wordBox.getValue() == ObjectRestrictionVocabulary.OBJECT_ALL_VALUES_FROM) {
            return editorKit.getOWLDataFactory().getOWLObjectAllValuesFrom(
                    objectPropertyHierarchy.getSelectedItem().getValue(),
                    classHierarchy.getSelectedItem().getValue()
            );
        } else if (wordBox.getValue() == ObjectRestrictionVocabulary.OBJECT_MIN_CARDINALITY) {
            return editorKit.getOWLDataFactory().getOWLObjectMinCardinality((Integer) numberField.getConvertedValue(),
                    objectPropertyHierarchy.getSelectedItem().getValue(),
                    classHierarchy.getSelectedItem().getValue()
            );
        } else if (wordBox.getValue() == ObjectRestrictionVocabulary.OBJECT_MAX_CARDINALITY) {
            return editorKit.getOWLDataFactory().getOWLObjectMaxCardinality((Integer) numberField.getConvertedValue(),
                    objectPropertyHierarchy.getSelectedItem().getValue(),
                    classHierarchy.getSelectedItem().getValue()
            );
        } else if (wordBox.getValue() == ObjectRestrictionVocabulary.OBJECT_EXACT_CARDINALITY) {
            return editorKit.getOWLDataFactory().getOWLObjectExactCardinality((Integer) numberField.getConvertedValue(),
                    objectPropertyHierarchy.getSelectedItem().getValue(),
                    classHierarchy.getSelectedItem().getValue()
            );
        } else {
            return editorKit.getOWLDataFactory().getOWLObjectSomeValuesFrom(
                    objectPropertyHierarchy.getSelectedItem().getValue(),
                    classHierarchy.getSelectedItem().getValue()
            );
        }
    }


    public static enum ObjectRestrictionVocabulary {
        OBJECT_SOME_VALUES_FROM("some"),
        OBJECT_ALL_VALUES_FROM("only"),
        OBJECT_MIN_CARDINALITY("min"),
        OBJECT_MAX_CARDINALITY("max"),
        OBJECT_EXACT_CARDINALITY("exact");

        final String shortForm;

        ObjectRestrictionVocabulary(String shortForm) {
            this.shortForm = shortForm;
        }

        @Override
        public String toString() {
            return shortForm;
        }

    }

}
