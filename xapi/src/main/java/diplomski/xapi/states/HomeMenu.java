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
public class HomeMenu implements StateMenu {

    public HomeMenu() {
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON Welcome to homepage\n");
        builder.append("1. Search course by courseId\n");
        builder.append("2. Search test by testId\n");
        builder.append("00. Exit\n");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String userSession) {
        if (inputText.equals("1")) {
            app.getPreviousStates().push(this);
            app.setCurrentState(new SearchCourseMenu());
        } else if (inputText.equals("2")) {
            app.getPreviousStates().push(this);
            app.setCurrentState(new SearchQuizMenu());
        } else if (inputText.equals("00")) {
            app.setConnectionAlive(false);
            app.setCurrentState(new ExitMenu());
            app.getPreviousStates().clear();
        }

    }

    @Override
    public void goBack(App app) {
        //nema promene
        return;
    }

    @Override
    public String stateName() {
        return "HOME_MENU";
    }

}
