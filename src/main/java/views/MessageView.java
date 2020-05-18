package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import restutils.AllRequestsHelper;

import static views.AbstractView.validateTextfield;


@Component
@UIScope
@Route(value = "message")
@Push
public class MessageView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(MessageView.class);
    private TextField username;
    private TextField subject;
    private TextArea textArea;

    public MessageView() {
        LOGGER.info("Entering MessageView");
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setJustifyContentMode(JustifyContentMode.CENTER);
        FormLayout nameLayout = new FormLayout();
        username = new TextField();
        username.setLabel("Username");
        username.setRequired(true);
        subject = new TextField();
        subject.setLabel("Subject");
        textArea = new TextArea();
        textArea.setLabel("Message");
        Button saveMessageButton = new Button("Send");
        saveMessageButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> sendMessageEvent(buttonClickEvent, username, subject, textArea));
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        nameLayout.add(username, subject, textArea, saveMessageButton);
        vl.add(nameLayout, returnButton);
        add(vl);
    }

    private void sendMessageEvent(ClickEvent<Button> buttonClickEvent, TextField username, TextField subject, TextArea textArea) {
        LOGGER.info("Entering sendMessageEvent");
        if (!validateTextfield(username, false, false)) {
            Notification.show("Username cannot be empty!");
        } else {
            boolean sendInfo = AllRequestsHelper.postSendMessage(username.getValue(), subject.getValue(), textArea.getValue());
            if (sendInfo) {
                Notification.show("Message send to: " + username.getValue());
                buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
            } else Notification.show("Error, looks like user: " + username.getValue() + " does not exist");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        resetView();
    }

    private void resetView() {
        username.setValue("");
        subject.setValue("");
        textArea.setValue("");
    }
}
