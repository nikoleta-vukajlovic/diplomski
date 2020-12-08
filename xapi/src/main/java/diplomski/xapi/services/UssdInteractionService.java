/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import diplomski.xapi.states.App;
import diplomski.xapi.states.SessionExpiredMenu;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class UssdInteractionService {

    @Autowired
    RedisService redisService;

    @Autowired
    XApiService xapiService;

    private static final String PREFIX_FOR_REDIS = "AFRICA_TALKING:USERS:";

    public String invokeUserAction(String phoneNumber, String text, String userSession) throws InterruptedException, ExecutionException, JsonProcessingException, Exception {
        String key = PREFIX_FOR_REDIS + phoneNumber;
        if (text.equals("")) {
            // Zatrazen je pocetak komunikacije (ako je istekla sesija opet se salje "" pa je potrebna provera da li je vec u Redisu taj phoneNumber)
            xapiService.userOpenedApp(phoneNumber);
            return communicationInitalization(phoneNumber, key);
        } else {
            App currentMainState = buildStateFromRedisEntry(phoneNumber);//trenutno stanje aplikacije u kom se nalazi korisnik
            if (currentMainState == null) {
                return "END Error";
            }
            currentMainState.getCurrentState().invoke(text, currentMainState, userSession);//promena stanja na osnovu korisnikove akcije
            saveState(key, currentMainState);//posle promene stanja, potrebno je upamtiti stanje u bazi
            if (!currentMainState.getConnectionAlive()) {
                deleteUser(key);//ako je korisnik zavrsio sa aplikacijom, ili izabraio u meniju da izlazi iz aplikacije
            }
            return currentMainState.getCurrentState().message();//sta treba da se prikaze korisniku
        }
    }

    private String communicationInitalization(String phoneNumber, String key) throws InterruptedException, JsonProcessingException {
        App loadedState = rebuildUserIfHeIsInTheSystem(phoneNumber); // doda mu SessionExpired za currentState
        if (loadedState == null) {
            App createdState = loginUser(phoneNumber);
            if (createdState == null) {
                return "END Error while doing registration for user";
            }
            return createdState.getCurrentState().message();
        } else {
            saveState(key, loadedState); // Promena treba da se zapamti jer mu je sada currentState = SessionExpiredMenu
            return loadedState.getCurrentState().message();
        }
    }

    public App buildStateFromRedisEntry(String phoneNumber) throws InterruptedException, JsonProcessingException {
        return redisService.buildStateFromRedisEntry(phoneNumber);
    }

    private App loginUser(String phoneNumber) throws JsonProcessingException {
        return redisService.loginUser(phoneNumber);
    }

    private void deleteUser(String key) {
        redisService.deleteUser(key);
    }

    //funkcija koja provera da li je korisnik vec nesto radio u aplikaciji i da li zeli da nastavi
    private App rebuildUserIfHeIsInTheSystem(String phoneNumber) throws InterruptedException, JsonProcessingException {
        App currentMainState = buildStateFromRedisEntry(phoneNumber);
        if (currentMainState == null) {
            return null;
        } else {
            //korisnik je vec bio u sistemu i proveravamo da li zeli da nastavi tamo gde je bio
            currentMainState.getPreviousStates().push(currentMainState.getCurrentState());
            currentMainState.setCurrentState(new SessionExpiredMenu());
            return currentMainState;
        }
    }

    //funkcija koja pamti korisniko stanje u redis
    private void saveState(String key, App state) throws JsonProcessingException {
        redisService.saveState(key, state);
    }

}
