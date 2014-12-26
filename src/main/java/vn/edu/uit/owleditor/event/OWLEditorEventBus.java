package vn.edu.uit.owleditor.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import vn.edu.uit.owleditor.ui.OWLEditorUI;

import javax.annotation.Nonnull;

/**
 * Created by Chuong Dang on 11/20/2014.

 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */

public class OWLEditorEventBus implements SubscriberExceptionHandler {

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
