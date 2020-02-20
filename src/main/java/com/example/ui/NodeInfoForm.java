package com.example.ui;

import com.example.parser.NodeInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class NodeInfoForm extends FormLayout{
	TextField id = new TextField("Id");

    TextField health = new TextField("Health");
    TextField baseBoardId = new TextField("baseBoardId");
    TextField baseBoardPosition = new TextField("baseBoardPosition");
    TextField state = new TextField("state");
    
    Binder<NodeInfo> binder = new BeanValidationBinder<>(NodeInfo.class);


    Button on = new Button("On");
    Button off = new Button("Off");
    Button reset = new Button("Reset");

    
    public NodeInfoForm() {
        addClassName("nodeinfo-form");
        binder.bindInstanceFields(this);
        id.setReadOnly(true);
        health.setReadOnly(true);
        baseBoardId.setReadOnly(true);
        baseBoardPosition.setReadOnly(true);
        state.setReadOnly(true);
        add(
            id,
            health,
            baseBoardId,
            baseBoardPosition,
            state,
            createButtonsLayout()
        );
    }

    private Component createButtonsLayout() {
        on.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        off.addThemeVariants(ButtonVariant.LUMO_ERROR);
        reset.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        on.addClickShortcut(Key.ENTER);
        off.addClickShortcut(Key.ESCAPE);
        
        on.addClickListener(click -> fireEvent(new OnEvent(this, binder.getBean())));
        off.addClickListener(click -> fireEvent(new OffEvent(this, binder.getBean()))); 
        reset.addClickListener(click -> fireEvent(new ResetEvent(this, binder.getBean())));
        
        binder.addStatusChangeListener(evt -> on.setEnabled(binder.isValid()));

        return new HorizontalLayout(on, off, reset);
    }
    
    public void setNodeInfo(NodeInfo nodeInfo) {
        binder.setBean(nodeInfo);
    }
    
    private void switchNodeOn() {
        if (binder.isValid()) {
        	fireEvent(new OnEvent(this, binder.getBean()));
        }
    }
    
    private void switchNodeOff() {
        if (binder.isValid()) {
        	fireEvent(new OffEvent(this, binder.getBean()));
        }
    }
    
    // Events
    public static abstract class NodeInfoFormEvent extends ComponentEvent<NodeInfoForm> {
      private NodeInfo  nodeInfo;

      protected NodeInfoFormEvent(NodeInfoForm source, NodeInfo nodeInfo) {
        super(source, false);
        this.nodeInfo = nodeInfo;
      }

      public NodeInfo getNodeInfo() {
        return nodeInfo;
      }
    }

    public static class OnEvent extends NodeInfoFormEvent {
      OnEvent(NodeInfoForm source, NodeInfo nodeInfo) {
        super(source, nodeInfo);
      }
    }

    public static class OffEvent extends NodeInfoFormEvent {
      OffEvent(NodeInfoForm source, NodeInfo nodeInfo) {
        super(source, nodeInfo);
      }

    }

    public static class ResetEvent extends NodeInfoFormEvent {
    	ResetEvent(NodeInfoForm source, NodeInfo nodeInfo) {
        super(source, nodeInfo);
      }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
      return getEventBus().addListener(eventType, listener);
    }
    
}
