/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.StatementTarget;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;
import diplomski.xapi.databaseServices.interfaces.CoursePagesService;
import diplomski.xapi.databaseServices.interfaces.CourseService;
import diplomski.xapi.jooq.model.tables.pojos.Course;
import diplomski.xapi.jooq.model.tables.pojos.CoursePages;
import diplomski.xapi.models.StatementsResponse;
import diplomski.xapi.services.ScormCloudApiService;
import diplomski.xapi.services.Service;
import diplomski.xapi.services.UssdInteractionService;
import diplomski.xapi.services.XApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dusan
 */
@RestController
public class AfricaController {

    @Autowired
    ScormCloudApiService scormCloudService;

    @Autowired
    UssdInteractionService appService;

    @Autowired
    XApiService xapiService;

    @RequestMapping(value = "/getPage", method = RequestMethod.POST/*, consumes = {"application/x-www-form-urlencoded"}*/)
    @ResponseBody
    public String getPage(HttpServletRequest request) throws InterruptedException, ExecutionException, JsonProcessingException, Exception {
        String phoneNumber = request.getParameter("phoneNumber");
        String networkCode = request.getParameter("networkCode");
        String serviceCode = request.getParameter("serviceCode");
        String text = request.getParameter("text");
        String sessionId = request.getParameter("sessionId");
        String lastUserAction = "";

        if (!text.equals("")) {
            //uzimamo samo poslenju korisnikovu akciju i prosledjujemo je stanju u kome je trenutno korisnik
            String[] split = text.split("\\*");
            lastUserAction = split[split.length - 1];
        }
        String userSession = sessionId + ":" + phoneNumber;
        String resolveStateMessage = appService.invokeUserAction(phoneNumber, lastUserAction, userSession);
        return resolveStateMessage;//Odgovor korisniku

    }

    @GetMapping("/test")
    public StatementsResponse testMethod() {
        try {
            //scormCloudService.getTextDataForCourse("random123", 1);
            //xapiService.getAllWrongAnswersForUserOnQuiz("+381609998881", "kvizDusan1");
            List<Statement> userLoggedInAppStatementsForUser = xapiService.getUserLoggedInAppStatementsForUser("+381609998881");
            ArrayList<String> statementsToString = new ArrayList<>();
            for (int i = 0; i < userLoggedInAppStatementsForUser.size(); i++) {
                statementsToString.add(userLoggedInAppStatementsForUser.get(i).toString());
            }
            StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
            return resp;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @RequestMapping(value = "/getAllStatementsWhereUserHasLoggedInApp", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllStatementsWhereUserHasLoggedInApp(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        List<Statement> statements = xapiService.getUserLoggedInAppStatementsForUser(username);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString);
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @GetMapping("/getAllUsersWhoHaveLoggedInApplication")
    public StatementsResponse getAllUsersWhoHaveLoggedInApplication() throws Exception {
        List<Statement> statements = xapiService.getUserLoggedInAppStatements();
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;

    }

    @RequestMapping(value = "/getAllAnswersForQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllAnswersForQuiz(@RequestBody Map<String, String> request) throws Exception {
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.gettAllAnswersForQuiz(quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllCorrectAnswersForQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllCorrectAnswersForQuiz(@RequestBody Map<String, String> request) throws Exception {
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllCorrectAnswersForQuiz(quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllWrongAnswersForQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllWrongAnswersForQuiz(@RequestBody Map<String, String> request) throws Exception {
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllWrongAnswersForQuiz(quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllAnswersForUserAllQuizes", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllAnswersForUserAllQuizes(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        List<Statement> statements = xapiService.getAllAnswersOnAllQuizesForUser(username);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllCorrectAnswersForUserOnAllQuizes", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllCorrectAnswersForUserOnAllQuizes(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        List<Statement> statements = xapiService.getAllCorrectAnswersOnAllQuizesForUser(username);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllWrongAnswersForUserOnAllQuizes", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllWrongAnswersForUserOnAllQuizes(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        List<Statement> statements = xapiService.getAllWrongAnswersOnAllQuizesForUser(username);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllAnswersUserProducedOnQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllAnswersUserProducedOnQuiz(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllAnswersForUserOnQuiz(username, quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllCorrectAnswersUserProducedOnQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllCorrectAnswersUserProducedOnQuiz(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllCorrectAnswersForUserOnQuiz(username, quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllWrongAnswersUserProducedOnQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllWrongAnswersUserProducedOnQuiz(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllWrongAnswersForUserOnQuiz(username, quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllReadStatementsForCourse", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllReadStatementsForCourse(@RequestBody Map<String, String> request) throws Exception {
        String courseId = request.get("courseId");
        List<Statement> statements = xapiService.getReadPagesForCourse(courseId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllPagesUserReadForCourse", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllPagesUserReadForCourse(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String courseId = request.get("courseId");
        List<Statement> statements = xapiService.getUserReadPageForCourse(username, courseId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllCoursesUserHasRead", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllCoursesUserHasRead(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        List<Statement> statements = xapiService.getUserFinishedReadingCourseStatements(username);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllUsersWhoHaveReadCourse", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllUsersWhoHaveReadCourse(@RequestBody Map<String, String> request) throws Exception {
        String courseId = request.get("courseId");
        List<Statement> statements = xapiService.getAllUsersWhoFinishedReadingCourseForCourse(courseId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllUsersWhoHaveCompletedQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllUsersWhoHaveCompletedQuiz(@RequestBody Map<String, String> request) throws Exception {
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllUsersWhoCompletedQuizForQuiz(quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getUserHasCompletedQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getUserHasCompletedQuiz(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getUserScoreForQuizStatement(username, quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getAllScoresForQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getAllScoresForQuiz(@RequestBody Map<String, String> request) throws Exception {
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getAllUserScoresForQuiz(quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    @RequestMapping(value = "/getUserScoreForQuiz", method = RequestMethod.POST)
    @ResponseBody
    public StatementsResponse getUserScoreForQuiz(@RequestBody Map<String, String> request) throws Exception {
        String username = request.get("username");
        String quizId = request.get("quizId");
        List<Statement> statements = xapiService.getUserScoreForQuizStatement(username, quizId);
        ArrayList<String> statementsToString = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            String statementToString = this.statementToString(statements.get(i));
            statementsToString.add(statementToString.toString());;
        }
        StatementsResponse resp = new StatementsResponse("Hello", statementsToString);
        return resp;
    }

    private String statementToString(Statement s) {
        Statement st = s;
        Agent actor = st.getActor();
        Verb verb = st.getVerb();
        StatementTarget object = st.getObject();
        JsonNode objectNode = object.toJSONNode(TCAPIVersion.V095);
        JsonNode objectId = objectNode.get("id");
        JsonNode objectDefinition = objectNode.get("definition");
        StringBuilder builder = new StringBuilder();
        if (actor != null && actor.getAccount() != null) {
            if (actor.getAccount().getName() != null) {
                builder.append("Actor: " + actor.getAccount().getName().toString());
            }
        } else {
            builder.append("Actor: " + actor.getMbox().toString());
        }
        builder.append(" Verb : " + verb.getId().toString());
        builder.append(" Object: " + "id:" + objectId + " definition:" + objectDefinition + "\n");
        return builder.toString();
    }

}
