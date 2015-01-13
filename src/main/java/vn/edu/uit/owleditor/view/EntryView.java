package vn.edu.uit.owleditor.view;

import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final UploadField uploadField = new UploadField();
    private final MTextField urlField = new MTextField().withFullWidth();

    public EntryView() {
        final Component entriesPanel = buildEntryPanel();
        Responsive.makeResponsive(entriesPanel);
        addComponent(entriesPanel);
        setComponentAlignment(entriesPanel, Alignment.MIDDLE_CENTER);
        addStyleName("owleditor-entry-view");
        setSizeFull();
    }


    private Layout buildEntryPanel() {
        return new MVerticalLayout(buildUrlEntry(), buildUploadEntry())
                .withStyleName("entry-panel")
                .withSpacing(true);
    }

    private Layout buildUrlEntry() {
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
                .withAlign(openBtn, Alignment.BOTTOM_LEFT)
                .withSpacing(true);
    }

    private Layout buildUploadEntry() {
        final MButton openBtn = new MButton("Open file", click -> {
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
                .withAlign(openBtn, Alignment.BOTTOM_LEFT)
                .withSpacing(true);
    }
    

}
