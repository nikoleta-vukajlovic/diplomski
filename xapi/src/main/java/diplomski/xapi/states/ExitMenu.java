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
public class ExitMenu implements StateMenu {

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("END Thank you for using our service, come again");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String userSession) {
        return;
    }

    @Override
    public void goBack(App app) {
        return;
    }

    @Override
    public String stateName() {
        return "EXIT_MENU";
    }

}
