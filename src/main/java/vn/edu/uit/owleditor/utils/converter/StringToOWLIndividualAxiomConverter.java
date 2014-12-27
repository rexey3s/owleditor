package vn.edu.uit.owleditor.utils.converter;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.core.OWLEditorKitImpl;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/11/2014.
 */
public class StringToOWLIndividualAxiomConverter extends AbstractStringToOWLObjectConverter<OWLIndividualAxiom> {

    public StringToOWLIndividualAxiomConverter(@Nonnull OWLEditorKit eKit) {
        super(eKit);
    }

    @Override
    public OWLIndividualAxiom convertToModel(String s, Class<? extends OWLIndividualAxiom> aClass, Locale locale) throws ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(OWLIndividualAxiom owlAxiom, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return owlAxiom.accept(new OWLAxiomVisitorExAdapter<String>("Cannnot convert from OWLAxiom to String") {
            @Override
            public String visit(OWLDataPropertyAssertionAxiom axiom) {
                return OWLEditorKitImpl.render(axiom.getProperty())
                        + " " + OWLEditorKitImpl.render(axiom.getObject());
            }

            @Override
            public String visit(OWLObjectPropertyAssertionAxiom axiom) {
                return OWLEditorKitImpl.render(axiom.getProperty())
                        + " " + OWLEditorKitImpl.render(axiom.getObject());
            }

            @Override
            public String visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
                return OWLEditorKitImpl.render(axiom.getProperty())
                        + " " + OWLEditorKitImpl.render(axiom.getObject());
            }

            @Override
            public String visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
                return OWLEditorKitImpl.render(axiom.getProperty())
                        + " " + OWLEditorKitImpl.render(axiom.getObject());
            }
        });
    }

    @Override
    public Class<OWLIndividualAxiom> getModelType() {
        return OWLIndividualAxiom.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
