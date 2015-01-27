package vn.edu.uit.owleditor.event;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/12/14.
 */

public interface OWLEntityActionHandler<SUB extends OWLEditorEvent.EntityAddEvent,
        SIBLING extends OWLEditorEvent.EntityAddEvent,
        REMOVE extends OWLEditorEvent.EntityRemoveEvent> {


    public void handleAddSubEntityEvent(SUB event);

    public void handleAddSiblingEntityEvent(SIBLING event);

    public void handleRemoveEntityEvent(REMOVE event);

}
