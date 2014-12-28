package vn.edu.uit.owleditor.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import vn.edu.uit.owleditor.OWLEditorUI;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Created by Chuong Dang on 11/20/2014.
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
@UIScope
@VaadinComponent
public class OWLEditorEventBus implements SubscriberExceptionHandler, Serializable {

    private final EventBus eventBus = new EventBus(this);

    public static void post(@Nonnull final Object event) {
        OWLEditorUI.getGuavaEventBus().eventBus.post(event);
    }

    public static void register(@Nonnull final Object object) {
        OWLEditorUI.getGuavaEventBus().eventBus.register(object);
    }

    public static void unregister(@Nonnull final Object object) {
        OWLEditorUI.getGuavaEventBus().eventBus.unregister(object);
    }

    @Override
    public void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
