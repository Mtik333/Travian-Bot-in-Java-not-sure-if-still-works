package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.UIScope;
import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import restutils.AllRequestsHelper;

import java.util.List;
import java.util.Map;

import static views.AbstractView.validateTextfield;

@Component
@UIScope
@Route(value = "send-merchants")
@Push
public class SendMerchantView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(SendMerchantView.class);
    private static Map<String, String> infoValues;
    private TextField villageName;
    private TextField lumberField;
    private TextField clayField;
    private TextField ironField;
    private TextField cropField;
    private TextField xField;
    private TextField yField;
    private Button sendButton;
    private Button doChanges;
    private FormLayout nameLayout;
    private Div infoDiv;
    private VillageListThread thread;

    public SendMerchantView() {
        LOGGER.info("Entering SendMerchantView");
        VerticalLayout vl = new VerticalLayout();
        sendButton = new Button("Send merchants");
        doChanges = new Button("Edit form");
        infoDiv = new Div();
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        sendButton.setEnabled(false);
        doChanges.setEnabled(false);
        nameLayout = new FormLayout();
        lumberField = new TextField();
        lumberField.setLabel("Lumber");
        clayField = new TextField();
        clayField.setLabel("Clay");
        ironField = new TextField();
        ironField.setLabel("Iron");
        cropField = new TextField();
        cropField.setLabel("Crop");
        xField = new TextField();
        xField.setLabel("X coordinate");
        yField = new TextField();
        yField.setLabel("Y coordinate");
        villageName = new TextField();
        villageName.setLabel("Village name");
        Button autoCompleteName = new Button("Autocomplete village name");
        autoCompleteName.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            thread = new VillageListThread(buttonClickEvent.getSource().getUI().get(), this, villageName.getValue());
            thread.start();
        });
        Button saveMessageButton = new Button("Prepare merchants");
        saveMessageButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            boolean validFields = false;
            if (validateTextfield(lumberField, false, true)
                    && validateTextfield(clayField, false, true)
                    && validateTextfield(ironField, false, true)
                    && validateTextfield(cropField, false, true)) {
                if ((validateTextfield(xField, true, true) && validateTextfield(yField, true, true)) || (validateTextfield(villageName, false, false)))
                    validFields = true;
                else Notification.show("Either coordinates or village name must be filled");
            }
            if (validFields) {
                boolean response = AllRequestsHelper.prepareMerchantResponse(lumberField.getValue(), clayField.getValue(), ironField.getValue(), cropField.getValue(), xField.getValue(), yField.getValue(), villageName.getValue());
                if (response) {
                    nameLayout.setEnabled(false);
                    doChanges.setVisible(true);
                    doChanges.setEnabled(true);
                    sendButton.setVisible(true);
                    sendButton.setEnabled(true);
                    infoDiv.setVisible(true);
                    infoValues = AllRequestsHelper.getInfoAboutMerchantForm();
                    for (Map.Entry entry : infoValues.entrySet()) {
                        Label resourceLabel = new Label(entry.getKey() + " " + entry.getValue() + "\t");
                        infoDiv.add(resourceLabel);
                    }
                } else {
                    Notification.show(AllStrings.LAST_MERCHANT_JSON.getResponse().getData().getErrorMessage());
                }
            }
        });
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        nameLayout.add(lumberField, clayField, ironField, cropField, xField, yField, villageName, autoCompleteName, saveMessageButton);
        sendButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (buttonClickEvent.getSource().isEnabled()) {
                boolean sendResponse = AllRequestsHelper.sendMerchant(lumberField.getValue(), clayField.getValue(), ironField.getValue(), cropField.getValue());
                if (sendResponse) {
                    Notification.show("Merchants send");
                    if (thread != null) {
                        thread.interrupt();
                        thread = null;
                    }
                    buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
                }
            }
        });
        doChanges.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (buttonClickEvent.getSource().isEnabled()) {
                nameLayout.setEnabled(true);
                doChanges.setEnabled(false);
                sendButton.setEnabled(false);
                infoDiv.removeAll();
            }
        });
        infoDiv.setVisible(false);
        vl.add(nameLayout, sendButton, doChanges, infoDiv, returnButton);
        add(vl);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        resetView();
    }

    private void resetView() {
        lumberField.setValue("");
        clayField.setValue("");
        ironField.setValue("");
        cropField.setValue("");
        xField.setValue("");
        yField.setValue("");
        if (infoDiv.isVisible()) {
            doChanges.setEnabled(false);
            sendButton.setEnabled(false);
            nameLayout.setEnabled(true);
            infoDiv.removeAll();
        }
    }

    private static class VillageListThread extends Thread {
        private final UI ui;
        private final SendMerchantView view;
        private final String village;

        VillageListThread(UI ui, SendMerchantView view, String village) {
            this.ui = ui;
            this.view = view;
            this.village = village;
        }

        @Override
        public void run() {
            LOGGER.info("Entering VillageListThread");
            List<String> test = AllRequestsHelper.getAutocompleteVillageName(village);
            ui.access((Command) () -> view.villageName.setValue(test.get(0)));
        }
    }
}
