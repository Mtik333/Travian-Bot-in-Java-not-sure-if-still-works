package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import helpers.AllStrings;
import helpers.VariousHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import restutils.AllRequestsHelper;

import java.util.List;
import java.util.Map;

import static views.AbstractView.addLabelsToDiv;

@Component
@UIScope
@Route(value = "new-building")
@Push
public class NewBuildingView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(UpgradeBuildingView.class);
    private static Map<String, String> requiredResourcesToUpgrade;
    private static Map<String, String> currentResources;
    private ComboBox resourcesBox;
    private Div resourcesDiv;
    private ComboBox emptyFieldsBox;

    public NewBuildingView() {
        LOGGER.info("Entering NewBuildingView");
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        FormLayout nameLayout = new FormLayout();
        resourcesBox = new ComboBox();
        resourcesBox.setLabel("Select building");
        resourcesDiv = new Div();
        currentResources = AllRequestsHelper.getCurrentResources();
        List<String> buildingsList = AllRequestsHelper.getAvailableBuildingToBuild();
        List<String> emptyFields = AllStrings.EMPTY_FIELDS_LIST;
        resourcesBox.setItems(buildingsList);
        resourcesBox.setRequired(true);
        resourcesBox.setInvalid(true);
        resourcesBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                String value = event.getValue().toString();
                requiredResourcesToUpgrade = AllRequestsHelper.getResourcesRequiredForNewBuilding(value);
                Div resourcesGrid = addLabelsToDiv("Required resources\n", requiredResourcesToUpgrade);
                Div currentResourcesDiv = addLabelsToDiv("Current resources\n", currentResources);
                resourcesDiv.removeAll();
                resourcesDiv.add(resourcesGrid, currentResourcesDiv);
                resourcesBox.setInvalid(false);
            } else {
                Notification.show("Select resource!");
                resourcesBox.setInvalid(true);
                resourcesDiv.removeAll();
            }
        });
        emptyFieldsBox = new ComboBox();
        emptyFieldsBox.setLabel("Select field id");
        emptyFieldsBox.setItems(emptyFields);
        emptyFieldsBox.setRequired(true);
        emptyFieldsBox.setInvalid(true);
        emptyFieldsBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                //if id on list then
                emptyFieldsBox.setInvalid(false);
            } else {
                Notification.show("Select valid id!");
                emptyFieldsBox.setInvalid(true);
            }
        });
        Button saveMessageButton = new Button("Build");
        saveMessageButton.addClickListener(buttonClickEvent -> {
            if (resourcesBox.isInvalid()) {
                Notification.show("Select building!");
            }
            if (emptyFieldsBox.isInvalid()) {
                Notification.show("Select field");
            } else if (!VariousHelper.isEnoughResources(currentResources, requiredResourcesToUpgrade))
                Notification.show("Not enough resources!");
            else if (AllRequestsHelper.getTimeToFinishBuilding() == 0) {
                AllRequestsHelper.getConstructNewBuilding(resourcesBox.getValue().toString(), emptyFieldsBox.getValue().toString());
                Notification.show("Building: " + resourcesBox.getValue());
                buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
            } else {
                Notification.show("Current task: " + AllStrings.CURRENT_TASK_BUILD + ", time to finish: " + AllStrings.TIME_TO_FINISH_BUILD);
            }
        });
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        nameLayout.add(resourcesBox, emptyFieldsBox, saveMessageButton);
        vl.add(nameLayout, resourcesDiv, returnButton);
        add(vl);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        resetView();
    }

    private void resetView() {
        resourcesBox.setValue(null);
        emptyFieldsBox.setValue(null);
        resourcesDiv.removeAll();
        currentResources = AllRequestsHelper.getCurrentResources();
        List<String> buildingsList = AllRequestsHelper.getAvailableBuildingToBuild();
        List<String> emptyFields = AllStrings.EMPTY_FIELDS_LIST;
        resourcesBox.setItems(buildingsList);
        emptyFieldsBox.setItems(emptyFields);
    }
}
