/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import diplomski.xapi.states.interfaces.StateMenu;
import java.util.Stack;

/**
 *
 * @author Dusan
 */
public class App {

    private StateMenu currentState;//trenutno stanje aplikacije
    private Stack<StateMenu> previousStates;//sva stanja koja je korisnik menjao
    private boolean connectionIsAlive;

    /*Informacije koje su vezane za afrika talcking*/
    private String userPhoneNumber;
    private String africaTalkingSession;

    public App() {
        currentState = null;
        previousStates = new Stack<>();
        connectionIsAlive = true;
        userPhoneNumber = "";
        africaTalkingSession = "";
    }

    public StateMenu getCurrentState() {
        return currentState;
    }

    public void setCurrentState(StateMenu currentState) {
        this.currentState = currentState;
    }

    public Stack<StateMenu> getPreviousStates() {
        return previousStates;
    }

    public void setPreviousStates(Stack<StateMenu> previousStates) {
        this.previousStates = previousStates;
    }

    public boolean getConnectionAlive() {
        return connectionIsAlive;
    }

    public void setConnectionAlive(boolean connectionIsAlive) {
        this.connectionIsAlive = connectionIsAlive;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getAfricaTalkingSession() {
        return africaTalkingSession;
    }

    public void setAfricaTalkingSession(String africaTalkingSession) {
        this.africaTalkingSession = africaTalkingSession;
    }
}
