package vn.edu.uit.owleditor.utils;

import org.semanticweb.owlapi.util.OWLAPIPreconditions;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/6/2014.
 */
public class EditorUtils {

    public static String writePercentageCoordinator(int textLength) {
        double x = ((textLength % 10.5) + 0.05) * 1;
        double y = ((textLength / 10.5) + 0.05) * 3;
        return new StringBuilder("top:" + y + "%;" + "left:" + x + "%;").toString();
    }

    public static String writePixelCoordinator(int textLength) {
        double x = ((textLength % 53) + 1) * 5;
        double y = ((textLength / 53) + 1) * 15;
        return new StringBuilder("top:" + y + "px;" + "left:" + x + "px;").toString();
    }

    public static void checkNotNull(Object object, @Nonnull String message) throws NullPointerException {
        OWLAPIPreconditions.checkNotNull(object, message);
    }
}
