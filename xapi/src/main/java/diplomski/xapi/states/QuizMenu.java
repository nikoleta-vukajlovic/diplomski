/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.states;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import diplomski.xapi.config.ApplicationContextUtils;
import diplomski.xapi.jooq.model.tables.pojos.QuizPages;
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
public class QuizMenu implements StateMenu {

    @Autowired
    XApiService xapiService;

    private List<QuizPages> quizPages;

    int currentQuestionNumber;

    int numOfCorrectAnswers;

    public QuizMenu() {
        ApplicationContextUtils.registerObject(this);
    }

    public QuizMenu(List<QuizPages> quizPages, int currentQuestionNumber, int numOfCorrectAnswers) {
        this.quizPages = quizPages;
        this.currentQuestionNumber = currentQuestionNumber;
        this.numOfCorrectAnswers = numOfCorrectAnswers;
        ApplicationContextUtils.registerObject(this);
    }

    @Override
    public String message() {
        StringBuilder builder = new StringBuilder();
        builder.append("CON Question : " + currentQuestionNumber + "\n");
        String question = quizPages.get(currentQuestionNumber).getQuestion();
        builder.append(question + "\n");
        builder.append("Answer this question and you will go to the next question");
        return builder.toString();
    }

    @Override
    public void invoke(String inputText, App app, String sessionId) {
        String question = quizPages.get(currentQuestionNumber).getQuestion();
        String quizid = quizPages.get(currentQuestionNumber).getQuizid();
        try {
            String correctAnswer = quizPages.get(currentQuestionNumber).getAnswer();
            if (inputText.equals(correctAnswer)) {
                xapiService.userAnswerWasCorrect(app.getUserPhoneNumber(), quizid, question, this.currentQuestionNumber, correctAnswer);
                this.numOfCorrectAnswers++;
            } else {
                xapiService.userAnswerWasWrong(app.getUserPhoneNumber(), quizid, question, this.currentQuestionNumber, inputText, correctAnswer);
            }

            if (currentQuestionNumber < quizPages.size() - 1) {
                currentQuestionNumber++; // ne menja se stanje vec sa samo povecava ovaj index
            } else {
                xapiService.userCompletedQuiz(app.getUserPhoneNumber(), quizid);
                xapiService.userScoreOnTest(app.getUserPhoneNumber(), quizid, Integer.toString(this.numOfCorrectAnswers) + "/" + quizPages.size());
                app.getPreviousStates().clear();
                app.setCurrentState(new HomeMenu());

            }
        } catch (Exception ex) {
            Logger.getLogger(QuizMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void goBack(App app) {
        if (currentQuestionNumber == 0) {
            app.setCurrentState(app.getPreviousStates().pop());
        } else {
            this.currentQuestionNumber--;
        }

    }

    @Override
    public String stateName() {
        return "QUIZ_MENU";
    }

    public List<QuizPages> getQuizPages() {
        return quizPages;
    }

    public void setQuizPages(List<QuizPages> quizPages) {
        this.quizPages = quizPages;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

    public void setCurrentQuestionNumber(int currentQuestionNumber) {
        this.currentQuestionNumber = currentQuestionNumber;
    }

    public int getNumOfCorrectAnswers() {
        return numOfCorrectAnswers;
    }

    public void setNumOfCorrectAnswers(int numOfCorrectAnswers) {
        this.numOfCorrectAnswers = numOfCorrectAnswers;
    }

}
