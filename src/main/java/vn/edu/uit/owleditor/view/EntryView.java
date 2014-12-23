package vn.edu.uit.owleditor.view;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.utils.converter.OWLObjectConverterFactory;

import java.io.File;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/13/14.
 */

@VaadinComponent
public class EntryView extends VerticalLayout {
    private static final String TEMP_FILE_DIR = "./";
    private final UploadField uploadField = new UploadField();
    private final TextField urlField = new TextField();
    @Autowired
    EventBus eventBus;

    public EntryView() {
        final Component entriesPanel = buildEntryPanel();
        addComponent(entriesPanel);
        setComponentAlignment(entriesPanel, Alignment.MIDDLE_CENTER);
        addStyleName("owleditor-entry-view");
        setSizeFull();
    }

    private Component buildEntryPanel() {
        final VerticalLayout entryPanel = new VerticalLayout();
        entryPanel.setSizeUndefined();
        entryPanel.setSpacing(true);
        entryPanel.addComponent(buildUrlEntry());
        entryPanel.addComponent(buildUploadEntry());
        Responsive.makeResponsive(entryPanel);
        entryPanel.addStyleName("entry-panel");
        return entryPanel;
    }

    private Component buildUrlEntry() {
        final HorizontalLayout urlFieldWrapper = new HorizontalLayout();
        final Button openBtn = new Button("Open");

//        urlField.addValidator(new URLValidator("Invalid URL"));
        urlField.setIcon(FontAwesome.EXTERNAL_LINK);
        urlField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        openBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);

        urlFieldWrapper.addComponents(urlField, openBtn);
        urlFieldWrapper.setComponentAlignment(openBtn, Alignment.BOTTOM_LEFT);
        urlFieldWrapper.setSpacing(true);
        urlFieldWrapper.setSizeUndefined();

        openBtn.addListener((Button.ClickEvent event) -> {
            try {

                OWLEditorKit eKit = new OWLEditorKit(IRI.create(urlField.getValue()));
                eventBus.publish(EventScope.APPLICATION, EntryView.this, eKit);
                UI.getCurrent().getSession().setAttribute("kit", eKit);
                UI.getCurrent().getSession().getCurrent().setConverterFactory(
                        new OWLObjectConverterFactory(eKit));
                UI.getCurrent().setContent(new MainView());

            } catch (OWLOntologyCreationException e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            } catch (Exception e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        return urlFieldWrapper;
    }

    private Component buildUploadEntry() {
        final HorizontalLayout uploadWrapper = new HorizontalLayout();
        final Button openBtn = new Button("Open file");
        uploadWrapper.addComponents(uploadField, openBtn);
        uploadWrapper.setSizeUndefined();

        openBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        uploadWrapper.setComponentAlignment(openBtn, Alignment.BOTTOM_LEFT);
        uploadField.setFieldType(UploadField.FieldType.FILE);
        uploadField.setFileFactory((fileName, mimeType) -> new File(TEMP_FILE_DIR + fileName));
        openBtn.addListener((Button.ClickEvent event) -> {
            try {
                File file = (File) uploadField.getValue();
                if (file.exists()) {
                    OWLEditorKit eKit = new OWLEditorKit(IRI.create(file));

                    UI.getCurrent().getSession().setAttribute("kit", eKit);

                    UI.getCurrent().getSession().getCurrent().setConverterFactory(
                            new OWLObjectConverterFactory(eKit));

                    UI.getCurrent().setContent(new MainView());
                }
            } catch (NullPointerException nullEx) {
                Notification.show("Please upload your file", Notification.Type.ERROR_MESSAGE);
            } catch (OWLOntologyCreationException e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        return uploadWrapper;
    }



}
