package vn.edu.uit.owleditor.view.window;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.util.Assert;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 1/31/15.
 */
public class DownloadOntologyWindow extends Window {
    private final OWLEditorKit eKit;
    private final TextField ontologyName = new TextField();
    private final OWLDocumentFormat documentFormat;
    //        private OWLDocumentFormat targetFormat;
    private final ComboBox formats = new ComboBox();
    private final ObjectProperty<String> fileNameSource = new ObjectProperty<>("ont.owl", String.class, false);
    private OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
    private RDFXMLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
    private ManchesterSyntaxDocumentFormat manSyntaxFormat = new ManchesterSyntaxDocumentFormat();
    private FunctionalSyntaxDocumentFormat funcSyntaxFormat = new FunctionalSyntaxDocumentFormat();

    public DownloadOntologyWindow() {
        eKit = OWLEditorUI.getEditorKit();
        ontologyName.setPropertyDataSource(fileNameSource);
        documentFormat = eKit.getModelManager().getOntologyFormat(eKit.getActiveOntology());
        formats.setNullSelectionAllowed(false);
        formats.addContainerProperty("FORMAT", String.class, null);
        formats.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        formats.setItemCaptionPropertyId("FORMAT");
        initFormatsBox();
        initialize();


    }

    @SuppressWarnings("unchecked")
    private void initFormatsBox() {
        formats.addItem(rdfxmlFormat);
        formats.getContainerProperty(rdfxmlFormat, "FORMAT").setValue("RDF/XML");
        formats.addItem(owlxmlFormat);
        formats.getContainerProperty(owlxmlFormat, "FORMAT").setValue("OWL/XML");
        formats.addItem(manSyntaxFormat);
        formats.getContainerProperty(manSyntaxFormat, "FORMAT").setValue("ManchesterSyntax");
        formats.addItem(funcSyntaxFormat);
        formats.getContainerProperty(funcSyntaxFormat, "FORMAT").setValue("FunctionalSyntax");
    }

    private void initialize() {
        setModal(true);
        setClosable(false);
        setResizable(false);
        setWidth(300.0f, Unit.PIXELS);
        setHeight(250.0f, Unit.PIXELS);
        setContent(buildContent());
    }

    private OWLDocumentFormat selectFormat() {
        try {
            Assert.notNull(formats.getValue(), "Select format");

            if (documentFormat.isPrefixOWLOntologyFormat()) {
                if (formats.getValue() instanceof RDFXMLDocumentFormat) {
                    rdfxmlFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                    return rdfxmlFormat;
                }
                if (formats.getValue() instanceof OWLXMLDocumentFormat) {
                    owlxmlFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                    return owlxmlFormat;
                }
                if (formats.getValue() instanceof ManchesterSyntaxDocumentFormat) {
                    manSyntaxFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                    return manSyntaxFormat;
                }
                if (formats.getValue() instanceof FunctionalSyntaxDocumentFormat) {
                    funcSyntaxFormat.copyPrefixesFrom(documentFormat.asPrefixOWLOntologyFormat());
                    return funcSyntaxFormat;
                }
            }
        } catch (NullPointerException e) {
            Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);

        }
        return documentFormat;
    }

    private StreamResource createResource(@Nonnull String output) {
        return new StreamResource(() -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                eKit.getModelManager()
                        .saveOntology(eKit.getActiveOntology(), selectFormat(), new StreamDocumentTarget(bos));

            } catch (NullPointerException | OWLOntologyStorageException nullEx) {
                Notification.show(nullEx.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
            return new ByteArrayInputStream(bos.toByteArray());
        }, output);
    }

    private Component buildContent() {
        final VerticalLayout result = new VerticalLayout();
        result.setMargin(true);
        result.setSpacing(true);
        FormLayout form = new FormLayout();
        ontologyName.focus();
        ontologyName.setCaption("Filename");
        formats.setCaption("Format");

        form.addComponent(ontologyName);
        form.addComponent(formats);
        result.addComponent(form);
        result.addComponent(buildFooter());

        return result;
    }

    private Component buildFooter() {
        final HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event -> close());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        Button save = new Button("Save");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ontologyName.addValueChangeListener(change -> {
            StreamResource rs = createResource(Preconditions.checkNotNull(change.getProperty().getValue().toString()));
            FileDownloader fd = new FileDownloader(rs);
            fd.extend(save);
        });


        save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        return footer;

    }

}
