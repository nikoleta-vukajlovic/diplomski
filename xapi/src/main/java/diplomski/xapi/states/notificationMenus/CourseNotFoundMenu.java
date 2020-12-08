/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states.notificationMenus;

import diplomski.xapi.states.App;
import diplomski.xapi.states.HomeMenu;
import diplomski.xapi.states.interfaces.AbstractNotificationMenu;

/**
 *
 * @author Dusan
 */
public class CourseNotFoundMenu extends AbstractNotificationMenu {

    public CourseNotFoundMenu() {
    }

    public CourseNotFoundMenu(String notifString) {
        super(notifString);
    }

    @Override
    public void goBack(App app) {
        app.setCurrentState(app.getPreviousStates().pop());
    }

}
