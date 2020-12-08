/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import diplomski.xapi.states.App;

/**
 *
 * @author Dusan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractNotificationMenu implements StateMenu {

    String notificationMessage;

    public AbstractNotificationMenu() {

    }

    public AbstractNotificationMenu(String notification) {
        this.notificationMessage = notification;
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON " + notificationMessage + "\n");
        builder.append("Type any number to continue \n");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String sessionId) {
        goBack(app);
    }

    @Override
    public abstract void goBack(App app);

    @Override
    public String stateName() {
        return "NOTIFICATION_MENU";
    }

}
