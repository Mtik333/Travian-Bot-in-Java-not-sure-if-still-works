package views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@UIScope
@Route(value = "withdraw-army")
@Push
public class DetectAttackView {

    private static final Logger LOGGER = LogManager.getLogger(DetectAttackView.class);
    private Button sendButton;
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

    public DetectAttackView(){

    }
}
