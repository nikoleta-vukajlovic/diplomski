/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import diplomski.xapi.config.ApplicationContextUtils;
import diplomski.xapi.services.ScormCloudApiService;
import diplomski.xapi.services.XApiService;
import diplomski.xapi.states.enums.xApiEnum;
import diplomski.xapi.states.interfaces.StateMenu;
import diplomski.xapi.states.notificationMenus.CourseNotFoundMenu;
import diplomski.xapi.states.notificationMenus.PageNotFoundMenu;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dusan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCourseMenu implements StateMenu {

    @Autowired
    ScormCloudApiService scormService;

    @Autowired
    XApiService xapiService;

    public SearchCourseMenu() {
        ApplicationContextUtils.registerObject(this);
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON Enter course code to see it's content \n");
        builder.append("99. BACK \n");
        return builder.toString();
    }

    @Override
    public void invoke(String searchedCourseId, App app, String sessionId) {
        if (searchedCourseId.equals("99")) {
            goBack(app);
        } else {
            try {
                String textDataForCourse = scormService.getTextDataForCourse(searchedCourseId, 0);
                if (textDataForCourse.equals(xApiEnum.COURSE_DOESNT_EXIST.toString())) {
                    //app.getPreviousStates().push(this);
                    app.setCurrentState(new CourseNotFoundMenu("Course with that CODE not found"));
                } else if (textDataForCourse.equals(xApiEnum.PAGE_OUT_OF_BOUNDS)) {
                    app.getPreviousStates().push(this);
                    app.setCurrentState(new PageNotFoundMenu("Page " + "doesn't exist"));
                } else {
                    Integer numberOfPages = scormService.getLastPageForCourse(searchedCourseId);
                    app.getPreviousStates().push(this);
                    CourseMenu cm = new CourseMenu(textDataForCourse, 0, searchedCourseId, numberOfPages);
                    xapiService.userReadPageStatement(app.getUserPhoneNumber(), 0, searchedCourseId);
                    if (cm.getNumberOfPages() == 1) {
                        xapiService.userFinishedReadingCourseStatement(app.getUserPhoneNumber(), searchedCourseId);
                    }
                    app.setCurrentState(cm);

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
        return "SEARCH_COURSE_MENU";
    }

}
