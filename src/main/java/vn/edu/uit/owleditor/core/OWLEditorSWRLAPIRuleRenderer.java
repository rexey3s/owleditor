package vn.edu.uit.owleditor.core;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.XSDVocabulary;
import org.swrlapi.builtins.arguments.*;
import org.swrlapi.core.SWRLAPIBuiltInAtom;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRenderer;
import org.swrlapi.sqwrl.SQWRLNames;
import org.swrlapi.sqwrl.SQWRLQuery;

import java.util.Iterator;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/10/2014.
 */
public class OWLEditorSWRLAPIRuleRenderer implements SWRLAPIRenderer {
    private final OWLOntology ontology;
    private final DefaultPrefixManager prefixManager;

    public OWLEditorSWRLAPIRuleRenderer(SWRLAPIOWLOntology swrlapiowlOntology) {
        this.ontology = swrlapiowlOntology.getOWLOntology();
        this.prefixManager = swrlapiowlOntology.getPrefixManager();
    }

    public String renderSWRLRule(SWRLRule rule) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.renderBodyAtoms(rule.getBody().iterator()));
        sb.append(" -> ");
        sb.append(this.renderHeadAtoms(rule.getHead().iterator()));
        return sb.toString();
    }

    public String renderSQWRLQuery(SQWRLQuery query) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.renderBodyAtoms(query.getBodyAtoms().iterator()));
        sb.append(" -> ");
        sb.append(this.renderHeadAtoms(query.getHeadAtoms().iterator()));
        return sb.toString();
    }

    private StringBuilder renderBodyAtoms(Iterator<SWRLAtom> bodyAtomIterator) {
        StringBuilder sb = new StringBuilder();
        boolean collectionMakeEncountered = false;
        boolean collectionOperationEncountered = false;

        for (boolean isFirst = true; bodyAtomIterator.hasNext(); isFirst = false) {
            SWRLAtom atom = (SWRLAtom) bodyAtomIterator.next();
            if (this.isSQWRLCollectionMakeBuiltInAtom(atom)) {
                if (collectionMakeEncountered) {
                    sb.append(" ^ ");
                } else {
                    sb.append('˚');
                    collectionMakeEncountered = true;
                }
            } else if (this.isSQWRLCollectionOperateBuiltInAtom(atom)) {
                if (collectionOperationEncountered) {
                    sb.append(" ^ ");
                } else {
                    sb.append('˚');
                    collectionOperationEncountered = true;
                }
            } else if (!isFirst) {
                sb.append(" ^ ");
            }

            sb.append((String) atom.accept(this));
        }

        return sb;
    }

    private StringBuilder renderHeadAtoms(Iterator<SWRLAtom> headAtomIterator) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (isFirst = true; headAtomIterator.hasNext(); isFirst = false) {
            SWRLAtom atom = (SWRLAtom) headAtomIterator.next();
            if (!isFirst) {
                sb.append("  ");
            }

            sb.append((String) atom.accept(this));
        }

        return sb;
    }

    public String visit(SWRLRule swrlRule) {
        return this.renderSWRLRule(swrlRule);
    }

    public String visit(SWRLClassAtom classAtom) {
        OWLClassExpression classExpression = classAtom.getPredicate();
        SWRLIArgument iArgument = (SWRLIArgument) classAtom.getArgument();
        StringBuilder sb = new StringBuilder();
        sb.append(this.visit((OWLClassExpression) classExpression));
        sb.append("(" + this.visit((SWRLIArgument) iArgument) + ")");
        return sb.toString();
    }

    public String visit(SWRLDataRangeAtom dataRangeAtom) {
        OWLDataRange dataRange = dataRangeAtom.getPredicate();
        SWRLDArgument dArgument = (SWRLDArgument) dataRangeAtom.getArgument();
        StringBuilder sb = new StringBuilder();
        sb.append(this.visit((OWLDataRange) dataRange));
        sb.append("(" + this.visit((SWRLDArgument) dArgument) + ")");
        return sb.toString();
    }

    public String visit(SWRLObjectPropertyAtom objectPropertyAtom) {
        OWLObjectPropertyExpression objectPropertyExpression = objectPropertyAtom.getPredicate();
        SWRLIArgument iArgument1 = (SWRLIArgument) objectPropertyAtom.getFirstArgument();
        SWRLIArgument iArgument2 = (SWRLIArgument) objectPropertyAtom.getSecondArgument();
        StringBuilder sb = new StringBuilder();
        sb.append(this.visit((OWLObjectPropertyExpression) objectPropertyExpression));
        sb.append("(" + this.visit((SWRLIArgument) iArgument1) + ", " + this.visit((SWRLIArgument) iArgument2) + ")");
        return sb.toString();
    }

    public String visit(SWRLDataPropertyAtom dataPropertyAtom) {
        OWLDataPropertyExpression dataPropertyExpression = dataPropertyAtom.getPredicate();
        SWRLIArgument iArgument1 = (SWRLIArgument) dataPropertyAtom.getFirstArgument();
        SWRLDArgument dArgument2 = (SWRLDArgument) dataPropertyAtom.getSecondArgument();
        StringBuilder sb = new StringBuilder();
        sb.append(this.visit((OWLDataPropertyExpression) dataPropertyExpression));
        sb.append("(" + this.visit((SWRLIArgument) iArgument1) + ", " + this.visit((SWRLDArgument) dArgument2) + ")");
        return sb.toString();
    }

    public String visit(SWRLBuiltInAtom builtInAtom) {
        IRI iri = builtInAtom.getPredicate();
        String builtInPrefixedName = this.prefixManager.getPrefixIRI(iri);
        StringBuilder sb = new StringBuilder();
        sb.append(builtInPrefixedName + "(");
        boolean isFirst = true;

        for (Iterator var6 = builtInAtom.getArguments().iterator(); var6.hasNext(); isFirst = false) {
            SWRLDArgument argument = (SWRLDArgument) var6.next();
            if (!isFirst) {
                sb.append(", ");
            }

            sb.append((String) argument.accept(this));
        }

        sb.append(")");
        return sb.toString();
    }

    public String visit(SWRLAPIBuiltInAtom swrlapiBuiltInAtom) {
        String builtInPrefixedName = swrlapiBuiltInAtom.getBuiltInPrefixedName();
        StringBuilder sb = new StringBuilder(builtInPrefixedName + "(");
        boolean isFirst = true;

        for (Iterator var5 = swrlapiBuiltInAtom.getBuiltInArguments().iterator(); var5.hasNext(); isFirst = false) {
            SWRLBuiltInArgument argument = (SWRLBuiltInArgument) var5.next();
            if (!isFirst) {
                sb.append(", ");
            }

            sb.append((String) argument.accept((SWRLBuiltInArgumentVisitorEx<String>) this));
        }

        sb.append(")");
        return sb.toString();
    }

    public String visit(SWRLVariable variable) {
        IRI variableIRI = variable.getIRI();
        String variablePrefixedName;
        if (this.ontology.containsEntityInSignature(variableIRI, Imports.INCLUDED)) {
            variablePrefixedName = this.prefixManager.getShortForm(variableIRI);
            return variablePrefixedName.startsWith(":") ? variablePrefixedName.substring(1) : variablePrefixedName;
        } else {
            variablePrefixedName = this.prefixManager.getPrefixIRI(variableIRI);
            return this.variablePrefixedName2VariableName(variablePrefixedName);
        }
    }

    public String visit(SWRLIndividualArgument individualArgument) {
        return this.visit((OWLIndividual) individualArgument.getIndividual());
    }

    public String visit(SWRLLiteralArgument literalArgument) {
        OWLLiteral literal = literalArgument.getLiteral();
        return this.visit((OWLLiteral) literal);
    }

    public String visit(SWRLSameIndividualAtom sameIndividualAtom) {
        SWRLIArgument iArgument1 = (SWRLIArgument) sameIndividualAtom.getFirstArgument();
        SWRLIArgument iArgument2 = (SWRLIArgument) sameIndividualAtom.getSecondArgument();
        StringBuilder sb = new StringBuilder();
        sb.append("sameAs");
        sb.append("(" + this.visit((SWRLIArgument) iArgument1) + ", " + this.visit((SWRLIArgument) iArgument2) + ")");
        return sb.toString();
    }

    public String visit(SWRLDifferentIndividualsAtom differentIndividualsAtom) {
        SWRLIArgument iArgument1 = (SWRLIArgument) differentIndividualsAtom.getFirstArgument();
        SWRLIArgument iArgument2 = (SWRLIArgument) differentIndividualsAtom.getSecondArgument();
        StringBuilder sb = new StringBuilder();
        sb.append("differentFrom");
        sb.append("(" + this.visit((SWRLIArgument) iArgument1) + ", " + this.visit((SWRLIArgument) iArgument2) + ")");
        return sb.toString();
    }

    private String visit(SWRLIArgument argument) {
        StringBuilder sb = new StringBuilder();
        if (argument instanceof SWRLIndividualArgument) {
            SWRLIndividualArgument variableArgument = (SWRLIndividualArgument) argument;
            sb.append((String) variableArgument.accept(this));
        } else if (argument instanceof SWRLVariable) {
            SWRLVariable variableArgument1 = (SWRLVariable) argument;
            sb.append((String) variableArgument1.accept(this));
        } else {
            sb.append("[Unknown " + SWRLIArgument.class.getName() + " type " + argument.getClass().getName() + "]");
        }

        return sb.toString();
    }

    private String visit(SWRLDArgument argument) {
        StringBuilder sb = new StringBuilder();
        if (argument instanceof SWRLBuiltInArgument) {
            SWRLBuiltInArgument variableArgument = (SWRLBuiltInArgument) argument;
            sb.append((String) variableArgument.accept((SWRLBuiltInArgumentVisitorEx<String>) this));
        } else if (argument instanceof SWRLLiteralArgument) {
            SWRLLiteralArgument variableArgument1 = (SWRLLiteralArgument) argument;
            sb.append((String) variableArgument1.accept(this));
        } else if (argument instanceof SWRLVariable) {
            SWRLVariable variableArgument2 = (SWRLVariable) argument;
            sb.append((String) variableArgument2.accept(this));
        } else {
            sb.append("[Unknown " + SWRLDArgument.class.getName() + " type " + argument.getClass().getName() + "]");
        }

        return sb.toString();
    }

    private String visit(OWLClassExpression classExpression) {
        if (classExpression.isAnonymous()) {
            return classExpression.toString();
        } else {
            OWLClass cls = classExpression.asOWLClass();
            return this.visit((OWLClass) cls);
        }
    }

    private String visit(OWLClass cls) {
        String classNameShortForm = this.prefixManager.getShortForm(cls.getIRI());
        return classNameShortForm.startsWith(":") ? classNameShortForm.substring(1) : classNameShortForm;
    }

    private String visit(OWLIndividual individual) {
        if (individual.isNamed()) {
            String individualNameShortForm = this.prefixManager.getShortForm(individual.asOWLNamedIndividual().getIRI());
            return individualNameShortForm.startsWith(":") ? individualNameShortForm.substring(1) : individualNameShortForm;
        } else {
            return individual.toString();
        }
    }

    private String visit(OWLObjectPropertyExpression objectPropertyExpression) {
        return objectPropertyExpression.isAnonymous() ? objectPropertyExpression.toString() : this.visit((OWLObjectProperty) objectPropertyExpression.asOWLObjectProperty());
    }

    private String visit(OWLObjectProperty property) {
        String objectPropertyNameShortForm = this.prefixManager.getPrefixIRI(property.getIRI());
        return objectPropertyNameShortForm.startsWith(":") ? objectPropertyNameShortForm.substring(1) : objectPropertyNameShortForm;
    }

    private String visit(OWLDataPropertyExpression dataPropertyExpression) {
        return dataPropertyExpression.isAnonymous() ? dataPropertyExpression.toString() : this.visit((OWLDataProperty) dataPropertyExpression.asOWLDataProperty());
    }

    private String visit(OWLDataProperty property) {
        String dataPropertyNameShortForm = this.prefixManager.getPrefixIRI(property.getIRI());
        return dataPropertyNameShortForm.startsWith(":") ? dataPropertyNameShortForm.substring(1) : dataPropertyNameShortForm;
    }

    private String visit(OWLDataRange dataRange) {
        if (dataRange.isDatatype()) {
            OWLDatatype datatype = dataRange.asOWLDatatype();
            return this.prefixManager.getShortForm(datatype.getIRI());
        } else {
            return dataRange.toString();
        }
    }

    public String visit(SWRLClassBuiltInArgument argument) {
        OWLClass cls = argument.getOWLClass();
        String classNameShortForm = this.prefixManager.getShortForm(cls.getIRI());
        return classNameShortForm.startsWith(":") ? classNameShortForm.substring(1) : classNameShortForm;
    }

    public String visit(SWRLNamedIndividualBuiltInArgument argument) {
        OWLNamedIndividual individual = argument.getOWLNamedIndividual();
        String individualNameShortForm = this.prefixManager.getShortForm(individual.getIRI());
        return individualNameShortForm.startsWith(":") ? individualNameShortForm.substring(1) : individualNameShortForm;
    }

    public String visit(SWRLObjectPropertyBuiltInArgument argument) {
        OWLObjectProperty property = argument.getOWLObjectProperty();
        String objectPropertyNameShortForm = this.prefixManager.getShortForm(property.getIRI());
        return objectPropertyNameShortForm.startsWith(":") ? objectPropertyNameShortForm.substring(1) : objectPropertyNameShortForm;
    }

    public String visit(SWRLDataPropertyBuiltInArgument argument) {
        OWLDataProperty property = argument.getOWLDataProperty();
        String dataPropertyNameShortForm = this.prefixManager.getShortForm(property.getIRI());
        return dataPropertyNameShortForm.startsWith(":") ? dataPropertyNameShortForm.substring(1) : dataPropertyNameShortForm;
    }

    public String visit(SWRLAnnotationPropertyBuiltInArgument argument) {
        OWLAnnotationProperty property = argument.getOWLAnnotationProperty();
        String annotationPropertyNameShortForm = this.prefixManager.getShortForm(property.getIRI());
        return annotationPropertyNameShortForm.startsWith(":") ? annotationPropertyNameShortForm.substring(1) : annotationPropertyNameShortForm;
    }

    public String visit(SWRLDatatypeBuiltInArgument argument) {
        OWLDatatype datatype = argument.getOWLDatatype();
        return this.prefixManager.getShortForm(datatype.getIRI());
    }

    public String visit(SWRLLiteralBuiltInArgument argument) {
        return this.visit((OWLLiteral) argument.getLiteral());
    }

    public String visit(SWRLVariableBuiltInArgument argument) {
        IRI variableIRI = argument.getIRI();
        String variablePrefixedName;
        if (this.ontology.containsEntityInSignature(variableIRI, Imports.INCLUDED)) {
            variablePrefixedName = this.prefixManager.getShortForm(variableIRI);
            return variablePrefixedName.startsWith(":") ? variablePrefixedName.substring(1) : variablePrefixedName;
        } else {
            variablePrefixedName = this.prefixManager.getPrefixIRI(variableIRI);
            return this.variablePrefixedName2VariableName(variablePrefixedName);
        }
    }

    public String visit(SWRLMultiValueVariableBuiltInArgument argument) {
        String variablePrefixedName = argument.getVariablePrefixedName();
        return this.variablePrefixedName2VariableName(variablePrefixedName);
    }

    public String visit(SQWRLCollectionVariableBuiltInArgument argument) {
        String variablePrefixedName = argument.getVariablePrefixedName();
        return this.variablePrefixedName2VariableName(variablePrefixedName);
    }

    private String visit(OWLLiteral literal) {
        OWLDatatype datatype = literal.getDatatype();
        String value = literal.getLiteral();
        return datatype.isString() ? "\"" + value + "\"" : (datatype.isFloat() ? value : (datatype.isBoolean() ? value : (datatype.getIRI().equals(XSDVocabulary.INT.getIRI()) ? value : "\"" + value + "\"^^\"" + this.visit((OWLDataRange) datatype) + "\"")));
    }

    private String variablePrefixedName2VariableName(String variablePrefixedName) {
        return variablePrefixedName.startsWith(":") ? "?" + variablePrefixedName.substring(1) : "?" + variablePrefixedName;
    }

    private boolean isSQWRLCollectionMakeBuiltInAtom(SWRLAtom atom) {
        return atom instanceof SWRLAPIBuiltInAtom && SQWRLNames.isSQWRLCollectionMakeBuiltIn(((SWRLAPIBuiltInAtom) atom).getBuiltInPrefixedName());
    }

    private boolean isSQWRLCollectionOperateBuiltInAtom(SWRLAtom atom) {
        return atom instanceof SWRLAPIBuiltInAtom && SQWRLNames.isSQWRLCollectionOperationBuiltIn(((SWRLAPIBuiltInAtom) atom).getBuiltInPrefixedName());
    }
}
