package vn.edu.uit.owleditor.core;

import com.vaadin.data.Property;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class ReasonserSwitcher implements Property<Boolean> {
    private final OWLEditorKit editorKit;

    public ReasonserSwitcher(@Nonnull OWLEditorKit eKit) {
        editorKit = eKit;
    }

    @Override
    public Boolean getValue() {
        return editorKit.getReasonerStatus();
    }

    @Override
    public void setValue(Boolean aBoolean) throws ReadOnlyException {
        editorKit.setReasonerStatus(aBoolean);
    }

    @Override
    public Class<? extends Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setReadOnly(boolean b) {

    }

}
