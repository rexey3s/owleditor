package vn.edu.uit.owleditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vaadin.spring.VaadinSessionScope;

@SpringBootApplication
@VaadinSessionScope
public class OWLEditorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OWLEditorApplication.class, args);
    }
}
