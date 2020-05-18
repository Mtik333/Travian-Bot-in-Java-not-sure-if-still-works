package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.spring.annotation.UIScope;
import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import restutils.AllRequestsHelper;

import java.util.Map;

import static views.AbstractView.validateComboBox;
import static views.AbstractView.validateTextfield;

@Component
@UIScope
@Route(value = "send-army")
@Push
public class SendTroopsView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(SendTroopsView.class);
    private static Map<String, String> infoValues;
    private Button sendButton;
    private Button doChanges;
    private Document html;
    private Div infoDiv;
    private TextField phalanx;
    private TextField swordsman;
    private TextField pathfinder;
    private TextField theutates;
    private TextField druidrider;
    private TextField haeduan;
    private TextField ram;
    private TextField trebuchet;
    private TextField chieftain;
    private TextField settler;
    private TextField hero;
    private TextField xField;
    private TextField yField;
    private ComboBox comboBox;
    private FormLayout nameLayout;

    public SendTroopsView() {
        LOGGER.info("Entering SendTroopsView");
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        infoDiv = new Div();
        sendButton = new Button("Send troops");
        sendButton.setEnabled(false);
        doChanges = new Button("Edit form");
        doChanges.setEnabled(false);
        nameLayout = new FormLayout();
        phalanx = new TextField();
        phalanx.setLabel(AllStrings.SOLDIER_PHALANX);
        phalanx.setRequired(true);
        swordsman = new TextField();
        swordsman.setLabel(AllStrings.SOLDIER_SWORDSMAN);
        swordsman.setRequired(true);
        pathfinder = new TextField();
        pathfinder.setLabel(AllStrings.SOLDIER_PATHFINDER);
        pathfinder.setRequired(true);
        theutates = new TextField();
        theutates.setLabel(AllStrings.SOLDIER_THEUTATES_THUNDER);
        theutates.setRequired(true);
        druidrider = new TextField();
        druidrider.setLabel(AllStrings.SOLDIER_DRUIDRIDER);
        druidrider.setRequired(true);
        haeduan = new TextField();
        haeduan.setLabel(AllStrings.SOLDIER_HAEDUAN);
        haeduan.setRequired(true);
        ram = new TextField();
        ram.setLabel(AllStrings.SOLDIER_RAM);
        ram.setRequired(true);
        trebuchet = new TextField();
        trebuchet.setLabel(AllStrings.SOLDIER_TREBUCHET);
        trebuchet.setRequired(true);
        chieftain = new TextField();
        chieftain.setLabel(AllStrings.SOLDIER_CHIEFTAIN);
        chieftain.setRequired(true);
        settler = new TextField();
        settler.setLabel(AllStrings.SOLDIER_SETTLER);
        settler.setRequired(true);
        hero = new TextField();
        hero.setLabel(AllStrings.SOLDIER_HERO);
        hero.setRequired(true);
        xField = new TextField();
        xField.setLabel("X coordinate");
        xField.setRequired(true);
        yField = new TextField();
        yField.setLabel("Y coordinate");
        yField.setRequired(true);
        Button saveMessageButton = new Button("Prepare troops");
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        comboBox = new ComboBox();
        comboBox.setLabel("Type of attack");
        comboBox.setRequired(true);
        comboBox.setItems(AllStrings.ATTACK_REINFORCEMENT, AllStrings.ATTACK_NORMAL, AllStrings.ATTACK_RAID);
        comboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                comboBox.setInvalid(false);
            } else {
                comboBox.setInvalid(true);
                Notification.show("Select attack type");
            }
        });
        saveMessageButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            //if validation
            if (validateTextfield(phalanx, false, true)
                    && validateTextfield(swordsman, false, true)
                    && validateTextfield(pathfinder, false, true)
                    && validateTextfield(theutates, false, true)
                    && validateTextfield(druidrider, false, true)
                    && validateTextfield(haeduan, false, true)
                    && validateTextfield(ram, false, true)
                    && validateTextfield(trebuchet, false, true)
                    && validateTextfield(chieftain, false, true)
                    && validateTextfield(settler, false, true)
                    && validateTextfield(hero, false, true)
                    && validateTextfield(xField, true, true)
                    && validateTextfield(yField, true, true)
                    && validateComboBox(comboBox, null, null, true)
                    && !comboBox.isInvalid()) {
                html = AllRequestsHelper.postPrepareTroops(phalanx.getValue(), swordsman.getValue(), pathfinder.getValue(), theutates.getValue(), druidrider.getValue(), haeduan.getValue(),
                        ram.getValue(), trebuchet.getValue(), chieftain.getValue(), settler.getValue(), hero.getValue(), xField.getValue(), yField.getValue(), comboBox.getValue().toString());
                Elements errors = html.getElementsByClass(AllStrings.ERROR_HTML);
                if (errors.size() == 0) {
                    sendButton.setVisible(true);
                    sendButton.setEnabled(true);
                    nameLayout.setEnabled(false);
                    doChanges.setVisible(true);
                    doChanges.setEnabled(true);
                    infoDiv.setVisible(true);
                    infoValues = AllRequestsHelper.getInfoAboutTroopsToSend(html);
                    for (Map.Entry entry : infoValues.entrySet()) {
                        Label resourceLabel = new Label(entry.getKey() + " " + entry.getValue() + "\t");
                        infoDiv.add(resourceLabel);
                    }
                } else {
                    Notification.show(errors.first().text());
                }
            }
        });
        sendButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            if (buttonClickEvent.getSource().isEnabled()) {
                AllRequestsHelper.postSendTroops(html);
                Notification.show("Army sent!");
                buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
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
        nameLayout.add(phalanx, swordsman, pathfinder, theutates, druidrider, haeduan, ram, trebuchet, chieftain, settler, hero, xField, yField, comboBox, saveMessageButton);
        vl.add(nameLayout, sendButton, doChanges, infoDiv, returnButton);
        add(vl);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        resetView();
    }

    private void resetView() {
        phalanx.setValue("");
        swordsman.setValue("");
        pathfinder.setValue("");
        theutates.setValue("");
        druidrider.setValue("");
        haeduan.setValue("");
        ram.setValue("");
        trebuchet.setValue("");
        chieftain.setValue("");
        settler.setValue("");
        hero.setValue("");
        xField.setValue("");
        yField.setValue("");
        comboBox.setValue(null);
        if (infoDiv.isVisible()) {
            doChanges.setEnabled(false);
            sendButton.setEnabled(false);
            nameLayout.setEnabled(true);
            infoDiv.removeAll();
        }
    }
}
