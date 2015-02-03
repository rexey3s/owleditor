package vn.edu.uit.owleditor.utils.exception;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/8/2014.
 */
public class OWLEditorException {

    public static class OWLHierarichicalContainerException extends Exception {

        public OWLHierarichicalContainerException(String message) {
            super(message);
        }

    }

    public static class DuplicatedActiveOntologyException extends RuntimeException {


        public DuplicatedActiveOntologyException() {
        }

        public DuplicatedActiveOntologyException(String message) {
            super(message);
        }

        public DuplicatedActiveOntologyException(String message, Throwable cause) {
            super(message, cause);
        }

        public DuplicatedActiveOntologyException(Throwable cause) {
            super(cause);
        }
    }

}
