package vn.edu.uit.owleditor.data.property;

import com.vaadin.data.Property;
import org.swrlapi.core.SWRLAPIRule;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/9/2014.
 */
public class SWRLAPIRuleSource implements SWRLRuleSource<SWRLAPIRule> {
    private boolean readOnly = false;

    private SWRLAPIRule rule;

    public SWRLAPIRuleSource() {
    }

    public SWRLAPIRuleSource(@Nonnull SWRLAPIRule rule1) {
        rule = rule1;
    }

    @Override
    public SWRLAPIRule getValue() throws NullPointerException {
        if (rule != null)
            return rule;
        else
            throw new NullPointerException("A rule cannot be null, please select a rule");
    }

    @Override
    public void setValue(SWRLAPIRule newValue) throws Property.ReadOnlyException {
        if (readOnly) {
            throw new Property.ReadOnlyException("Read-only SWRLAPIRule data source");
        }
        this.rule = newValue;
    }

    @Override
    public Class<? extends SWRLAPIRule> getType() {
        return SWRLAPIRule.class;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean newStatus) {
        readOnly = newStatus;
    }

}
