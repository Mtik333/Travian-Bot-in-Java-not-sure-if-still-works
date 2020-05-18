package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import helpers.AllStrings;
import helpers.VariousHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import restutils.AllRequestsHelper;

import java.util.Map;

abstract class AbstractView {

    private static final Logger LOGGER = LogManager.getLogger(AbstractView.class);

    static Div addLabelsToDiv(String divText, Map<String, String> entriesForLabel) {
        LOGGER.info("Entering addLabelsToDiv: divText: " + divText);
        Div grid = new Div();
        grid.setText(divText);
        grid.setTitle(divText);
        for (Map.Entry entry : entriesForLabel.entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue() + "\t");
            grid.add(label);
        }
        return grid;
    }

    static boolean validateTextfield(TextField textField, boolean isNegativeAllowed, boolean isNumeric) {
        LOGGER.info("Entering validateTextfield: textField: " + textField.getLabel() + ", isNegativeAllowed: " + isNegativeAllowed + ", isNumeric: " + isNumeric);
        if (textField.isEmpty()) {
            textField.setInvalid(true);
            Notification.show("Put value on " + textField.getLabel());
            return false;
        } else if (isNumeric && !NumberUtils.isCreatable(textField.getValue())) {
            textField.setInvalid(true);
            Notification.show(textField.getLabel() + ": Value must be numeric!");
            return false;
        } else {
            if (isNumeric && !isNegativeAllowed) {
                if (textField.getValue().contains("-")) {
                    textField.setInvalid(true);
                    Notification.show(textField.getLabel() + ": Value cannot be negative");
                    return false;
                }
            }
            //if value not bigger than X
            textField.setInvalid(false);
            return true;
        }
    }

    static boolean validateComboBox(ComboBox combobox, Map<String, String> currentResources, Map<String, String> requiredResources, boolean military) {
        LOGGER.info("Entering validateComboBox: combobox: " + combobox.getLabel() + ", military: " + military);
        if (combobox.isInvalid()) {
            Notification.show(combobox.getLabel() + ": No selection!");
            return false;
        } else if (!military && !VariousHelper.isEnoughResources(currentResources, requiredResources)) {
            Notification.show(combobox.getLabel() + ": Not enough resources!");
            return false;
        } else if (!military && AllRequestsHelper.getTimeToFinishBuilding() == 0) {
            return true;
        } else if (!military) {
            Notification.show("Current task: " + AllStrings.CURRENT_TASK_BUILD + ", time to finish: " + AllStrings.TIME_TO_FINISH_BUILD);
            return false;
        } else return true;
    }

    static void returnToMain(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering returnToMain");
        Notification.show("Entering returnToMain");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.getPage().getHistory().back());
    }


}
