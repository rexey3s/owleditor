package vn.edu.uit.owleditor.view;

import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import vn.edu.uit.owleditor.OWLEditorUI;
import vn.edu.uit.owleditor.utils.converter.OWLObjectConverterFactory;

import java.io.File;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */
@UIScope
@VaadinComponent
public class EntryView extends VerticalLayout {
    public  static final String NAME = "entryView";
    private static final Logger LOG = LoggerFactory.getLogger(EntryView.class);
    private static final String TEMP_FILE_DIR = "./";

    public EntryView() {
        final Component entriesPanel = buildEntryPanel();
        Responsive.makeResponsive(entriesPanel);
        addComponent(entriesPanel);
        setComponentAlignment(entriesPanel, Alignment.MIDDLE_CENTER);
        addStyleName("owleditor-entry-view");
        
        setSizeFull();
    }


    private Layout buildEntryPanel() {

        return new MVerticalLayout(buildUrlEntry(), buildUploadEntry(), buildCreateEntry())
                .withStyleName("entry-panel")
                .withWidth("550px")
                .withSpacing(true);
    }

    private Layout buildUrlEntry() {
        final MTextField urlField = new MTextField().withWidth("400px");
        final MButton openBtn = new MButton("Open", click -> {
            try {
                OWLEditorUI.getEditorKit().loadOntologyFromOntologyDocument(IRI.create(urlField.getValue()));
                VaadinSession.getCurrent()
                        .setConverterFactory(new OWLObjectConverterFactory(OWLEditorUI.getEditorKit()));

                OWLEditorUI.getHttpSession().setAttribute("OWLEditorKit", OWLEditorUI.getEditorKit());
                UI.getCurrent().setContent(new MainView());
            } 
            catch (NullPointerException nullEx) {
                
                Notification.show("The URL is invalid", Notification.Type.WARNING_MESSAGE);
                LOG.error(nullEx.getMessage());
            }
            catch (OWLOntologyCreationException e) {
                
                Notification.show("Ontology Creation Error", Notification.Type.ERROR_MESSAGE);
                LOG.error(e.getMessage());
            } 
            catch (Exception e) {
                LOG.error(e.getMessage());
            }

        }).withStyleName(ValoTheme.BUTTON_PRIMARY);
        

        return new MHorizontalLayout(urlField, openBtn)
                .withAlign(openBtn, Alignment.MIDDLE_RIGHT)
                .withSpacing(true);
    }

    private Layout buildUploadEntry() {
        final UploadField uploadField = new UploadField();
        final MButton openBtn = new MButton("OpenFile", click -> {
            try {
                File file = (File) uploadField.getValue();
                if (file.exists()) {

                    OWLEditorUI.getEditorKit().loadOntologyFromOntologyDocument(IRI.create(file));
                    VaadinSession.getCurrent().setConverterFactory(
                            new OWLObjectConverterFactory(OWLEditorUI.getEditorKit()));

                    OWLEditorUI.getHttpSession().setAttribute("OWLEditorKit", OWLEditorUI.getEditorKit());
                    UI.getCurrent().setContent(new MainView());
                }
            } 
            catch (NullPointerException nullEx) {
                
                Notification.show("Please upload your file", Notification.Type.ERROR_MESSAGE);
            } 
            catch (OWLOntologyCreationException e) {
                
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }).withStyleName(ValoTheme.BUTTON_PRIMARY);

        uploadField.setFieldType(UploadField.FieldType.FILE);
        uploadField.setFileFactory((fileName, mimeType) -> new File(TEMP_FILE_DIR + fileName));
        uploadField.addValueChangeListener(change -> {
            openBtn.setEnabled(change.getProperty().getValue() != null);
        });

        return new MHorizontalLayout(uploadField, openBtn)
                .withAlign(openBtn, Alignment.MIDDLE_RIGHT)
                .withSpacing(true);
    }
    
    private Layout buildCreateEntry() {
        final MTextField iriField = new MTextField().withWidth("400px")
                .withInputPrompt("http://www.semanticweb.org/ontologies/your_ontology_iri");
        final MButton open = new MButton("Create", click -> {
           try {
               Assert.notNull(iriField.getValue(), "Please enter an URI");
               OWLEditorUI.getEditorKit().createOntologyFromOntologyDocument(IRI.create(iriField.getValue()));
               VaadinSession.getCurrent().setConverterFactory(
                       new OWLObjectConverterFactory(OWLEditorUI.getEditorKit()));

               OWLEditorUI.getHttpSession().setAttribute("OWLEditorKit", OWLEditorUI.getEditorKit());
               UI.getCurrent().setContent(new MainView());
           }
           catch (NullPointerException emptyURI) {
               Notification.show(emptyURI.getMessage(), Notification.Type.WARNING_MESSAGE);
           }
           catch (OWLOntologyCreationException creationEx) {
               Notification.show(creationEx.getMessage(), Notification.Type.ERROR_MESSAGE);
           }
        });

        return new MHorizontalLayout(iriField, open)
                .withAlign(open, Alignment.MIDDLE_CENTER)
                .withSpacing(true);
    }
}
