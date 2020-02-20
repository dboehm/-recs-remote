package com.example.ui;

import com.example.ui.LoginService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("")
@PWA(name = "RECS-Remote",
        shortName = "RECS-Remote App",
        description = "This is an app to drive node in christmann microserver system 'RECS|Box' Vaadin application.",
        enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired LoginService service) {
    	this.setVisible(true);
    	this.setSizeFull();
    	 // Use TextField for standard text input
        TextField textField = new TextField("IP-Address/hostname to apply");
        textField.setValue("localhost");
        service.setHostName(textField.getValue());
       
        LoginOverlay component = new LoginOverlay();
    	component.addLoginListener(e -> {
    		Boolean isAuthenticated = service.login(e.getUsername(), e.getPassword());
    		 if (isAuthenticated) {
    		        navigateToDetailsPage();
    		        component.close();
    		    } else {
    		        component.setError(true);
    		    }
    	
    	});
    	
    	Button open = new Button("Open login dialogue",
        	    e -> {component.setOpened(true);
        	    component.setTitle(String.format("RECS-Remote-Login to"));
        	    component.setDescription(String.format("'%s'",textField.getValue()));
        	    service.setHostName(textField.getValue());
        	    }
    	);
    	
        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        open.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        open.addClickShortcut(Key.ENTER);
        open.focus();

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, open);
    }

	private void navigateToDetailsPage() {
				UI.getCurrent().navigate("details");
	}

}
