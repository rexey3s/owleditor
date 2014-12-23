package vn.edu.uit.owleditor.view.panel;

import vn.edu.uit.owleditor.ui.OWLEditorUI;
import vn.edu.uit.owleditor.core.OWLEditorKit;
import vn.edu.uit.owleditor.view.component.AbstractExpressionPanel;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.Nonnull;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/4/2014.
 */
public abstract class AbstractPanelContainer extends Panel {

    protected final OWLEditorKit editorKit;
    private final VerticalLayout root = new VerticalLayout();

    protected CssLayout descriptionPanels = new CssLayout();


    public AbstractPanelContainer(@Nonnull OWLEditorKit eKit) {
        editorKit = eKit;
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();


        root.setSizeFull();
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        OWLEditorUI.getEventBus().register(this);
    }


    protected Component createContentWrapper(final AbstractExpressionPanel content, String... customStyles) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");
        for (String style : customStyles) {
            slot.addStyleName(style);
        }

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuBar.MenuItem max = tools.addItem("", FontAwesome.EXPAND, selectedItem -> {
            if (!slot.getStyleName().contains("max")) {
                selectedItem.setIcon(FontAwesome.COMPRESS);
                toggleMaximized(slot, true);
            } else {
                slot.removeStyleName("max");
                selectedItem.setIcon(FontAwesome.EXPAND);
                toggleMaximized(slot, false);
            }
        });
        max.setStyleName("icon-only");
        tools.addItem("", FontAwesome.PLUS, content.getAddCmd());

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    protected void toggleMaximized(final Component panel, final boolean maximized) {
        for (Component it : root) {
            it.setVisible(!maximized);
        }
        descriptionPanels.setVisible(true);

        for (Component c : descriptionPanels) {
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

    protected void addRootStyleName(String... styleName) {
        for (String style : styleName) {
            root.addStyleName(style);
        }
    }


    protected abstract Component buildContent();

    public abstract void setPropertyDataSource(@Nonnull Property newDataSource);


}