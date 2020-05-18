package views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
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
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.UIScope;
import helpers.AllStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.vaadin.erik.TimerBar;
import restutils.AllRequestsHelper;

import java.util.Map;

import static views.AbstractView.*;

@Component
@UIScope
@Route(value = "train-units")
@Push
public class TrainUnitsView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(TrainUnitsView.class);
    private static Map<String, String> unitsToTrain;
    private TextField numOfSoldiers;
    private ComboBox soldierTypes;
    private VerticalLayout vl;
    private Div resourceDiv;

    public TrainUnitsView() {
        LOGGER.info("Entering TrainUnitsView");
        vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        resourceDiv = new Div();
        FormLayout nameLayout = new FormLayout();
        numOfSoldiers = new TextField();
        numOfSoldiers.setRequired(true);
        numOfSoldiers.setLabel("Number of soldiers to train");
        numOfSoldiers.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) textFieldStringComponentValueChangeEvent -> validateNumberToTrain(false));
        Button saveMessageButton = new Button("Train");
        saveMessageButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> validateNumberToTrain(true));
        soldierTypes = new ComboBox();
        soldierTypes.setLabel("Select type of soldier");
        unitsToTrain = AllRequestsHelper.getPossibleUnitsToTrain();
        soldierTypes.setItems(unitsToTrain.keySet());
        soldierTypes.setRequired(true);
        soldierTypes.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                addInfoAboutUnit(event.getValue().toString());
                soldierTypes.setInvalid(false);
            } else {
                soldierTypes.setInvalid(true);
                Notification.show("Select soldier type");
                vl.remove(resourceDiv);
            }
        });
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        nameLayout.add(soldierTypes, numOfSoldiers, saveMessageButton);
        vl.add(nameLayout, returnButton, resourceDiv);
        fillCurrentTaskLabel();
        add(vl);
    }

    private void addInfoAboutUnit(String value) {
        vl.remove(resourceDiv);
        LOGGER.info("Entering addInfoAboutUnit: value: " + value);
        String maxUnits = unitsToTrain.get(value);
        Map<String, String> resourcePerUnit = AllRequestsHelper.getResourcesRequiredForUnit(value);
        Label labelMaxUnits = new Label("Maximum number of units to train now: " + maxUnits);
        Div productionGrid = addLabelsToDiv("Required resources for unit\n", resourcePerUnit);
        resourceDiv.removeAll();
        resourceDiv.add(labelMaxUnits, productionGrid);
        vl.add(resourceDiv);
    }

    private void validateNumberToTrain(boolean toSend) {
        LOGGER.info("Entering validateNumberToTrain");
        if (validateComboBox(soldierTypes, null, null, true) && validateTextfield(numOfSoldiers, false, true)) {
            int curValue = Integer.parseInt(numOfSoldiers.getValue());
            int maxValue = Integer.parseInt(unitsToTrain.get(soldierTypes.getValue().toString()));
            if (curValue > maxValue) {
                Notification.show("Cannot train more units than possible!");
                numOfSoldiers.setInvalid(true);
            } else {
                numOfSoldiers.setInvalid(false);
                if (toSend) {
                    boolean result = AllRequestsHelper.postTrainSoldiers(soldierTypes.getValue().toString(), numOfSoldiers.getValue());
                    if (result) {
                        Notification.show(soldierTypes.getValue().toString() + ": " + numOfSoldiers.getValue() + " are being trained!");
                        fillCurrentTaskLabel();
                        //buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
                    }
                }
            }
        }
    }

    private void fillCurrentTaskLabel() {
        LOGGER.info("Entering fillCurrentTaskLabel");
        StringBuilder sb = new StringBuilder();
        AllRequestsHelper.getTimeToFinishTrain();
        if (!AllStrings.CURRENT_TROOP_TRAINING.isEmpty()) {
            sb.append(AllStrings.CURRENT_TROOP_TRAINING).append(", time to finish: ").append((AllStrings.TIME_TO_FINISH_TRAINING));
            Label currentTask = new Label(sb.toString());
            TimerBar timerBar = new TimerBar((AllStrings.TIME_TO_FINISH_TRAINING + 1) * 1000);
            timerBar.addTimerEndedListener((ComponentEventListener<TimerBar.TimerEndedEvent>) timerEndedEvent -> timerEndedEvent.getSource().getUI().ifPresent(ui -> ui.access((Command) () -> {
                Notification.show("Training: " + AllStrings.CURRENT_TROOP_TRAINING + " finished");
                resourceDiv.remove(currentTask, timerBar);
                AllStrings.CURRENT_TROOP_TRAINING = "";
                AllStrings.TIME_TO_FINISH_TRAINING = 0;
            })));
            resourceDiv.removeAll();
            resourceDiv.add(currentTask, timerBar);
            timerBar.start();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        resetView();
    }

    private void resetView() {
        numOfSoldiers.setValue("");
        soldierTypes.setValue(null);
        resourceDiv.removeAll();
        vl.remove(resourceDiv);
        resourceDiv = new Div();
        unitsToTrain = AllRequestsHelper.getPossibleUnitsToTrain();
        soldierTypes.setItems(unitsToTrain.keySet());
        fillCurrentTaskLabel();
        vl.add(resourceDiv);
    }
}
