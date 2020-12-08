/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states.interfaces;

import diplomski.xapi.states.App;

/**
 *
 * @author Dusan
 */
public interface StateMenu {

    public String message();

    public void invoke(String inputText, App app, String sessionId);

    public void goBack(App app);

    public String stateName();
}
