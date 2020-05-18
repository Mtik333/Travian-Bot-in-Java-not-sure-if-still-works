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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import restutils.AllRequestsHelper;

import java.util.Map;

import static views.AbstractView.addLabelsToDiv;
import static views.AbstractView.validateComboBox;

@Component
@UIScope
@Route(value = "upgrade-building")
@Push
public class UpgradeBuildingView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(UpgradeBuildingView.class);
    private static Map<String, String> buildingsList;
    private static Map<String, String> requiredResourcesToUpgrade;
    private static Map<String, String> currentResources;
    private Div resourcesDiv;
    private ComboBox resourcesBox;

    public UpgradeBuildingView() {
        LOGGER.info("Entering UpgradeResourceVIew");
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        FormLayout nameLayout = new FormLayout();
        resourcesBox = new ComboBox();
        resourcesDiv = new Div();
        buildingsList = AllRequestsHelper.getVillageBuildingsList();
        currentResources = AllRequestsHelper.getCurrentResources();
        resourcesBox.setLabel("Select building to upgrade");
        resourcesBox.setRequired(true);
        resourcesBox.setInvalid(true);
        resourcesBox.setItems(buildingsList.keySet());
        resourcesBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                LOGGER.info(buildingsList.get(event.getValue().toString()));
                String value = buildingsList.get(event.getValue().toString());
                requiredResourcesToUpgrade = AllRequestsHelper.getResourcesToUpgradeResourceBuilding(value);
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
        Button saveMessageButton = new Button("Upgrade");
        saveMessageButton.addClickListener(buttonClickEvent -> {
            //if resources match
            if (validateComboBox(resourcesBox, currentResources, requiredResourcesToUpgrade, false)) {
                AllRequestsHelper.getUpgradeResourceBuilding();
                Notification.show("Upgrading: " + resourcesBox.getValue());
                buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("eee"));
            }
        });
        Button returnButton = new Button("Return", (ComponentEventListener<ClickEvent<Button>>) AbstractView::returnToMain);
        nameLayout.add(resourcesBox, saveMessageButton);
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
        resourcesDiv.removeAll();
        buildingsList = AllRequestsHelper.getVillageBuildingsList();
        currentResources = AllRequestsHelper.getCurrentResources();
        resourcesBox.setItems(buildingsList.keySet());
    }
}
