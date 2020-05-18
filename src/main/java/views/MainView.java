package views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
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

import static views.AbstractView.addLabelsToDiv;

@Component
@UIScope
@Route(value = "eee")
@Push
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private static final Logger LOGGER = LogManager.getLogger(MainView.class);
    private static Map<String, String> villages;
    private VerticalLayout vl;
    private Div refreshElements;
    private Label villageName;

    public MainView() {
        try {
            LOGGER.info("Entering MainView");
            vl = new VerticalLayout();
            refreshElements = new Div();
            vl.setSizeFull();
            vl.setJustifyContentMode(JustifyContentMode.CENTER);
            villageName = new Label("Current village: ");
            Button refreshData = new Button("Refresh", buttonClickEvent -> refreshData());
            Button switchBetweenVillage = new Button("Choose village", buttonClickEvent -> showVillages());
            Button sendArmy = new Button("Send army", this::goToSendArmy);
            Button checkIfAttack = new Button("Check if attack coming on village", buttonClickEvent -> checkIfVillageIsAttacked());
            Button newBuilding = new Button("Build building", this::goToBuildBuilding);
            Button upgradeBuilding = new Button("Upgrade building", this::goToUpgradeBuilding);
            Button upgradeResource = new Button("Upgrade resource", this::goToUpgradeResource);
            Button buildMilitary = new Button("Train military units", this::goToTrainUnits);
            Button sendTradesman = new Button("Send merchant", this::goToSendMerchants);
            Button sendMessage = new Button("Send message", this::goToSendMessage);
            FormLayout formLayout = new FormLayout();
            formLayout.add(refreshData, switchBetweenVillage, sendArmy, checkIfAttack, newBuilding, upgradeBuilding, upgradeResource, buildMilitary, sendTradesman, sendMessage);
            vl.add(villageName, formLayout);
            refresh();
        } catch (Exception exp) {
            LOGGER.fatal(exp.getMessage());
            Notification.show("No Internet connection");
        }
    }

    private void refresh() {
        LOGGER.info("Refreshing MainView");
        refreshElements.removeAll();
        vl.remove(refreshElements);
        AllRequestsHelper.getClientFactory();
        Map<String, String> resources = AllRequestsHelper.getCurrentResources();
        Map<String, String> production = AllRequestsHelper.getProductionPerHour();
        Map<String, String> troopsInVillage = AllRequestsHelper.getMilitaryInVillage();
        villageName.setText("Current village: " + AllStrings.CURRENT_VILLAGE_NAME);
        Div resourcesGrid = addLabelsToDiv("Current resources\n", resources);
        Div productionGrid = addLabelsToDiv("Production per hour\n", production);
        Div militaryGrid = addLabelsToDiv("Military in village\n", troopsInVillage);
        refreshElements.add(resourcesGrid, productionGrid, militaryGrid);
        vl.add(refreshElements);
        fillCurrentTaskLabel();
        fillCurrentAttack();
        add(vl);
    }

    private void goToBuildBuilding(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToBuildBuilding");
        Notification.show("Entering buildBuilding");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("new-building"));
    }

    private void goToUpgradeResource(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToUpgradeResource");
        Notification.show("Entering upgradeResource");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("upgrade-resource"));
    }

    private void goToUpgradeBuilding(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToUpgradeBuilding");
        Notification.show("Entering upgradeBuilding");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("upgrade-building"));
    }

    private void goToSendMessage(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToSendMessage");
        Notification.show("Entering sendMessage");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("message"));
    }

    private void goToTrainUnits(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToTrainUnits");
        Notification.show("Entering trainUnits");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("train-units"));
    }

    private void goToSendMerchants(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToSendMerchants");
        Notification.show("Entering sendMerchants");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("send-merchants"));
    }

    private void goToSendArmy(ClickEvent<Button> buttonClickEvent) {
        LOGGER.info("Entering goToSendArmy");
        Notification.show("Entering sendArmy");
        buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate("send-army"));
    }

    private void checkIfVillageIsAttacked() {
        LOGGER.info("Entering checkIfVillageIsAttacked");
        AllRequestsHelper.getMilitaryActivity();
        if (AllRequestsHelper.isAttackIncoming()) {
            Notification.show("Attack incoming");
        } else {
            Notification.show("No attacks");
        }
    }

    private void refreshData() {
        LOGGER.info("Entering refreshData");
        Notification.show("Entering refreshData");
        refresh();
    }

    private void showVillages() {
        LOGGER.info("Entering showVillages");
        if (villages == null)
            villages = AllRequestsHelper.getAllVillages();
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        ComboBox soldierTypes = new ComboBox();
        soldierTypes.setLabel("Select village");
        soldierTypes.setItems(villages.keySet());
        soldierTypes.setRequired(true);
        soldierTypes.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                Notification.show("Select village");
            }
        });
        Button changeButton = new Button("Change village");
        changeButton.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent1 -> {
            if (soldierTypes.getValue() != null) {
                AllRequestsHelper.changeVillage(villages.get(soldierTypes.getValue()));
                refresh();
                dialog.close();
            }
        });
        dialog.add(soldierTypes, changeButton);
        dialog.open();
    }

    private void fillCurrentTaskLabel() {
        LOGGER.info("Entering fillCurrentTaskLabel");
        StringBuilder sb = new StringBuilder();
        AllRequestsHelper.getTimeToFinishBuilding();
        if (!AllStrings.CURRENT_TASK_BUILD.isEmpty()) {
            sb.append(AllStrings.CURRENT_TASK_BUILD).append(", time to finish: ").append((AllStrings.TIME_TO_FINISH_BUILD)).append(" seconds");
            Label currentTask = new Label(sb.toString());
            Button cancelTask = new Button("Cancel task");
            TimerBar timerBar = new TimerBar((AllStrings.TIME_TO_FINISH_BUILD + 1) * 1000);
            timerBar.addTimerEndedListener((ComponentEventListener<TimerBar.TimerEndedEvent>) timerEndedEvent -> timerEndedEvent.getSource().getUI().ifPresent(ui -> ui.access((Command) () -> {
                Notification.show("Task: " + AllStrings.CURRENT_TASK_BUILD + " finished");
                refreshElements.remove(currentTask, timerBar, cancelTask);
                AllStrings.CURRENT_TASK_BUILD = "";
                AllStrings.TIME_TO_FINISH_BUILD = 0;
            })));
            cancelTask.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                if (AllRequestsHelper.cancelCurrentBuildTask()) {
                    Notification.show("Task: " + AllStrings.CURRENT_TASK_BUILD + " canceled");
                    refreshElements.remove(currentTask, timerBar, cancelTask);
                    AllStrings.CURRENT_TASK_BUILD = "";
                    AllStrings.TIME_TO_FINISH_BUILD = 0;
                }
            });
            timerBar.start();
            refreshElements.add(currentTask, timerBar, cancelTask);
        }
    }

    private void fillCurrentAttack() {
        StringBuilder sb = new StringBuilder();
        AllRequestsHelper.getMilitaryActivity();
        if (!AllStrings.CURRENT_ATTACK_OUTGOING.isEmpty()) {
            sb.append(AllStrings.CURRENT_ATTACK_OUTGOING).append(", time to reach: ").append(AllStrings.TIME_TO_ARRIVE_ATTACK).append(" seconds");
            Span span = new Span();
            Label currentTask = new Label(sb.toString());
            Button cancelTask = new Button("Cancel attack");
            TimerBar timerBar = new TimerBar((AllStrings.TIME_TO_ARRIVE_ATTACK + 1) * 1000);
            timerBar.addTimerEndedListener((ComponentEventListener<TimerBar.TimerEndedEvent>) timerEndedEvent -> timerEndedEvent.getSource().getUI().ifPresent(ui -> ui.access((Command) () -> {
                Notification.show("Attack: " + AllStrings.CURRENT_ATTACK_OUTGOING + " finished");
                refreshElements.remove(currentTask, timerBar, cancelTask);
                AllStrings.CURRENT_ATTACK_OUTGOING = "";
                AllStrings.TIME_TO_ARRIVE_ATTACK = 0;
            })));
            cancelTask.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                if (AllRequestsHelper.cancelMilitaryActivity()) {
                    Notification.show("Attack: " + AllStrings.CURRENT_ATTACK_OUTGOING + " canceled");
                    refreshElements.remove(currentTask, timerBar, cancelTask);
                    AllStrings.CURRENT_ATTACK_OUTGOING = "";
                    AllStrings.TIME_TO_ARRIVE_ATTACK = 0;
                }
            });
            timerBar.start();
            refreshElements.add(currentTask, timerBar, cancelTask, span);
        }

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        LOGGER.info("Entering beforeEnter");
        refreshData();
    }
}
