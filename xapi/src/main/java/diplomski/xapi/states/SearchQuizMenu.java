/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import diplomski.xapi.config.ApplicationContextUtils;
import diplomski.xapi.jooq.model.tables.pojos.QuizPages;
import diplomski.xapi.services.ScormCloudApiService;
import diplomski.xapi.services.XApiService;
import diplomski.xapi.states.interfaces.StateMenu;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dusan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchQuizMenu implements StateMenu {

    @Autowired
    ScormCloudApiService scormService;

    @Autowired
    XApiService xapiService;

    public SearchQuizMenu() {
        ApplicationContextUtils.registerObject(this);
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON Enter test code to see it's content \n");
        builder.append("99. BACK \n");
        return builder.toString();
    }

    @Override
    public void invoke(String searchedQuizId, App app, String sessionId) {
        if (searchedQuizId.equals("99")) {
            goBack(app);
        } else {
            try {
                List<QuizPages> quizPages = scormService.getQuizPages(searchedQuizId);
                if (quizPages == null) {
                    return;
                } else {
                    app.getPreviousStates().push(this);
                    QuizMenu qm = new QuizMenu(quizPages, 0, 0);

                    app.setCurrentState(qm);

                }

            } catch (Exception ex) {
                Logger.getLogger(SearchCourseMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void goBack(App app) {
        app.setCurrentState(app.getPreviousStates().pop());
    }

    @Override
    public String stateName() {
        return "SEARCH_QUIZ_MENU";
    }

}
