package vn.edu.uit.owleditor.view.window;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.data.list.OWL2DatatypeContainer;
import vn.edu.uit.owleditor.event.OWLExpressionAddHandler;
import vn.edu.uit.owleditor.event.OWLExpressionUpdateHandler;
import vn.edu.uit.owleditor.utils.OWLEditorData;
import vn.edu.uit.owleditor.view.component.DataRangeExpressionEditor;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxParserException;
import org.semanticweb.owlapi.model.OWLDataRange;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/1/2014.
 */
public class DataRangeEditorWindow extends AbstractOWLExpressionEditorWindow<OWLDataRange> {

    private static Log LOG = LogFactory.getLog(DataRangeEditorWindow.class);
    private final ListSelect datatypeList = new ListSelect();
    private final DataRangeExpressionEditor editor = new DataRangeExpressionEditor();
    /**
     *  Edit mode
     * @param expression
     * @param modifyExpression1
     */
    public DataRangeEditorWindow(@Nonnull OWLEditorKit eKit,
                                 @Nonnull Property<OWLDataRange> expression,
                                 @Nonnull OWLExpressionUpdateHandler<OWLDataRange> modifyExpression1) {
        super(eKit, expression, modifyExpression1);
        initialise();
        editor.setValue(OWLEditorKit.render(expression.getValue()));
        addMoreTab(datatypeList, "Data type");
        addMoreTab(editor, "Data range expression");

    }

    /**
     * Add mode
     * @param addExpression1
     */
    public DataRangeEditorWindow(@Nonnull OWLEditorKit eKit,
                                 @Nonnull OWLExpressionAddHandler<OWLDataRange> addExpression1) {
        super(eKit, addExpression1);
        initialise();
        addMoreTab(datatypeList, "Data type");
        addMoreTab(editor, "Data range expression");
    }

    private void initialise() {
        setCaption("DataRange Editor");
        datatypeList.setContainerDataSource(new OWL2DatatypeContainer());
        datatypeList.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        datatypeList.setItemCaptionPropertyId(OWLEditorData.OWL2BuiltInDataType);
        datatypeList.setSizeFull();
    }
    @Override
    protected Button.ClickListener initSaveListener() {
        return clickEvent -> {
            if (getSelectedTab() instanceof DataRangeExpressionEditor) {
                if (addExpression != null) {
                    try {
                        editorKit.getParser().setStringToParse(editor.getValue());

                        OWLDataRange dataRange = editorKit.getParser().parseDataRange();
                        OWLEditorUI.getEventBus().post(addExpression.addingExpression(dataRange));
                        close();

                    } catch (ManchesterOWLSyntaxParserException parserEx) {
                        Notification.show(parserEx.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }

                } else if (modifyExpression != null) {
                    try {
                        editorKit.getParser().setStringToParse(editor.getValue());
                        OWLDataRange dataRange = editorKit.getParser().parseDataRange();
                        OWLEditorUI.getEventBus().post(modifyExpression.modifyingExpression(dataRange));
                        close();

                    } catch (ManchesterOWLSyntaxParserException parserEx) {
                        Notification.show(parserEx.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }
                }


            } else if (getSelectedTab() instanceof ListSelect) {
                try {
                    if (addExpression != null) {
                        OWLEditorUI.getEventBus().post(addExpression.addingExpression((OWLDataRange) datatypeList.getValue()));
                    } else if (modifyExpression != null) {
                        OWLEditorUI.getEventBus().post(modifyExpression.modifyingExpression((OWLDataRange) datatypeList.getValue()));
                    }
                    close();
                } catch (ManchesterOWLSyntaxParserException parserEx) {
                    LOG.error(parserEx.getMessage());
                }
            }
        };
    }

}
