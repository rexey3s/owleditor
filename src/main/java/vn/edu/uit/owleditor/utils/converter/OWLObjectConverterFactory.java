package vn.edu.uit.owleditor.utils.converter;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;
import org.semanticweb.owlapi.model.*;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 11/8/14.
 */
public class OWLObjectConverterFactory extends DefaultConverterFactory {

    private OWLEditorKit editorKit;

    public OWLObjectConverterFactory(@Nonnull OWLEditorKit kit) {
        editorKit = kit;
    }

    @Override
    public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(
            Class<PRESENTATION> presentationType, Class<MODEL> modelType) {

        if(String.class == presentationType && OWLClass.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLClassConverter(editorKit);
        }
        if(String.class == presentationType && OWLObjectProperty.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLObjectPropertyConverter(editorKit);
        }
        if (String.class == presentationType && OWLDataProperty.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLDataPropertyConverter(editorKit);
        }
        if (String.class == presentationType && OWLClassExpression.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLClassExpressionConverter(editorKit);
        }
        if (String.class == presentationType && OWLNamedIndividual.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToNamedIndividualConverter(editorKit);
        }
        if (String.class == presentationType && OWLObjectPropertyExpression.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLObjectPropertyExpressionConverter(editorKit);
        }
        if (String.class == presentationType && OWLDataPropertyExpression.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLDataPropertyExpressionConverter(editorKit);
        }
        if (String.class == presentationType && OWLDataRange.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToDataRangeConverter(editorKit);
        }
        if (String.class == presentationType && OWLIndividualAxiom.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new StringToOWLIndividualAxiomConverter(editorKit);
        }
        return super.createConverter(presentationType, modelType);
    }
}
