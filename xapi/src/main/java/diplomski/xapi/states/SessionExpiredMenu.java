/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import diplomski.xapi.states.interfaces.StateMenu;

/**
 *
 * @author Dusan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionExpiredMenu implements StateMenu {

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON Last time your session has expired\n");
        builder.append("Do you want to continue where you left ? \n");
        builder.append("1. Yes\n");
        builder.append("2. No, I want new session\n");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String userSession) {
        if (inputText.equals("1")) {
            app.setCurrentState(app.getPreviousStates().pop());
        } else if (inputText.equals("2")) {
            app.setCurrentState(new HomeMenu());
            app.getPreviousStates().clear();
        }
    }

    @Override
    public void goBack(App app) {
        return;
    }

    @Override
    public String stateName() {
        return "SESSION_EXPIRED_MENU";
    }

}
