package vn.edu.uit.owleditor.utils.validator;

import com.vaadin.data.validator.AbstractValidator;
import org.apache.commons.validator.UrlValidator;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/14/14.
 */
public class URLValidator extends AbstractValidator {

    protected String[] schemes = {"http", "https"};
    protected UrlValidator urlValidator = new UrlValidator(schemes);

    public URLValidator(String errorMessage) {
        super(errorMessage);
    }


    @Override
    protected boolean isValidValue(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("UrlValidator expects String input, got a " + value.getClass());
        }
        String url = (String) value;
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return urlValidator.isValid(url);
    }

    @Override
    public Class getType() {
        return URLValidator.class;
    }
}
