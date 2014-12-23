package vn.edu.uit.owleditor.event;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/12/14.
 */
public interface OWLEntityActionHandler<SUB extends OWLEditorEvent.OWLEntityCreatedEvent,
        SIBLING extends OWLEditorEvent.OWLEntityCreatedEvent,
        REMOVE extends OWLEditorEvent.OWLEntityRemovedEvent> {


    public void afterAddSubSaved(SUB event);

    public void afterAddSiblingSaved(SIBLING event);

    public void afterRemoved(REMOVE event);

}
