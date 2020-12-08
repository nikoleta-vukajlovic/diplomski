/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rusticisoftware.tincan.Activity;
import com.rusticisoftware.tincan.ActivityDefinition;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.AgentAccount;
import com.rusticisoftware.tincan.LanguageMap;
import com.rusticisoftware.tincan.Verb;
import diplomski.xapi.config.ApplicationContextUtils;
import diplomski.xapi.services.ScormCloudApiService;
import diplomski.xapi.services.XApiService;
import diplomski.xapi.states.enums.xApiEnum;
import diplomski.xapi.states.interfaces.StateMenu;
import diplomski.xapi.states.notificationMenus.CourseNotFoundMenu;
import diplomski.xapi.states.notificationMenus.PageNotFoundMenu;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dusan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseMenu implements StateMenu {

    private String text;
    private int page;
    private String courseId;
    private int numberOfPages;

    @Autowired
    ScormCloudApiService scormService;

    @Autowired
    XApiService xapiService;

    public CourseMenu() {
        ApplicationContextUtils.registerObject(this);
    }

    public CourseMenu(String text, int page, String courseId, int numberOfPages) {
        this.text = text;
        this.page = page;
        this.courseId = courseId;
        this.numberOfPages = numberOfPages;
        ApplicationContextUtils.registerObject(this);
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON PAGE : " + page + "\n");
        builder.append(text + "\n");
        builder.append("99. Back \n");
        builder.append("101. Next page \n");
        builder.append("111. +10 pages \n");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String sessionId) {
        try {
            if (inputText.equals("99")) {
                this.goBack(app);
            } else if (inputText.equals("101")) {
                changePage(1, app, courseId);
            } else if (inputText.equals("111")) {
                changePage(10, app, courseId);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    private void changePage(int pageOffset, App app, String inputCode) {
        try {
            int newPageNumber = page + pageOffset;
            app.getPreviousStates().push(this);
            String textDataForCourse = scormService.getTextDataForCourse(inputCode, newPageNumber);
            if (textDataForCourse.equals(xApiEnum.COURSE_DOESNT_EXIST.toString())) {
                app.getPreviousStates().push(this);
                app.setCurrentState(new CourseNotFoundMenu("Course with that CODE not found"));
            } else if (textDataForCourse.equals(xApiEnum.PAGE_OUT_OF_BOUNDS)) {
                app.getPreviousStates().push(this);
                app.setCurrentState(new PageNotFoundMenu("Page " + newPageNumber + "doesn't exist"));
            } else {
                app.getPreviousStates().push(this);
                app.setCurrentState(new CourseMenu(textDataForCourse, newPageNumber, courseId, this.numberOfPages));
                xapiService.userReadPageStatement(app.getUserPhoneNumber(), newPageNumber, courseId);
                if (newPageNumber == this.getNumberOfPages()) {
                    xapiService.userFinishedReadingCourseStatement(app.getUserPhoneNumber(), courseId);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CourseMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void goBack(App app) {
        app.setCurrentState(app.getPreviousStates().pop());
    }

    @Override
    public String stateName() {
        return "COURSE_MENU";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

}
