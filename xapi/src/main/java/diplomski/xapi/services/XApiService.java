/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.rusticisoftware.tincan.Activity;
import com.rusticisoftware.tincan.ActivityDefinition;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.AgentAccount;
import com.rusticisoftware.tincan.LanguageMap;
import com.rusticisoftware.tincan.QueryableStatementTarget;
import com.rusticisoftware.tincan.RemoteLRS;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.StatementTarget;
import com.rusticisoftware.tincan.StatementsQuery;
import com.rusticisoftware.tincan.StatementsResult;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class XApiService {

    private RemoteLRS configureLRS() throws MalformedURLException {
        RemoteLRS lrs;
        lrs = new RemoteLRS();
        lrs.setEndpoint("https://cloud.scorm.com/lrs/MVWK6547FO/");
        lrs.setVersion(TCAPIVersion.V095);
        lrs.setUsername("1GimU18456ndrLgC3zo");
        lrs.setPassword("uOtW0EYCphLHsSXls1M");
        return lrs;
    }

    // PUBLISHING STATEMETS
    public void publishStatement(Agent agent, Verb verb, Activity activity) throws MalformedURLException, Exception {
        RemoteLRS lrs = configureLRS();
        Statement st = new Statement();
        st.setActor(agent);
        st.setVerb(verb);
        st.setObject(activity);
        lrs.saveStatement(st);
    }

    public void userReadPageStatement(String phonenumber, int pageNumber, String courseId) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        Verb verb = new Verb("https://w3id.org/xapi/adb/verbs/read");
        String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "Read page");
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);
    }

    public void userFinishedReadingCourseStatement(String phonenumber, String courseId) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/completed");
        String activityURI = "http://dusanDiplomski/finishedReadingCourse/course:" + courseId;
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "Finished reading cours");
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);

    }

    public void userOpenedApp(String phonenumber) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        Verb verb = new Verb("https://brindlewaye.com/xAPITerms/verbs/loggedin/");
        String activityURI = "http://dusanDiplomski.com/openedApp";
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "User opened ussd learning service");
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);
    }

    public void userAnswerWasCorrect(String phonenumber, String quizId, String question, int questionNumber, String answer) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        agent.setName(phonenumber);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered");
        String activityURI = "http://dusanDiplomski/correctAnswer/quiz:" + quizId + "-" + "question:" + questionNumber;
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "User answer was correct on question (" + question + ")");
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);

    }

    public void userAnswerWasWrong(String phonenumber, String quizId, String question, int questionNumber, String userAnswer, String correctAnswer) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        agent.setName(phonenumber);
        agent.setName(phonenumber);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered");
        String activityURI = "http://dusanDiplomski/wrongAnswer/quiz:" + quizId + "-" + "question:" + questionNumber;
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "User answer was wrong on question (" + question + ")");
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);
    }

    public void userScoreOnTest(String phonenumber, String quizId, String score) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        agent.setName(phonenumber);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/scored");
        String activityURI = "http://dusanDiplomski/score/quiz:" + quizId;
        Activity activity = new Activity(activityURI);
        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "User score :  " + score + "on quiz " + quizId);
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);
    }

    public void userCompletedQuiz(String phonenumber, String quizId) throws MalformedURLException, Exception {
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        agent.setName(phonenumber);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/completed");
        String activityURI = "http://dusanDiplomski/completedQuiz/quiz:" + quizId;
        Activity activity = new Activity(activityURI);

        ActivityDefinition ad = new ActivityDefinition();
        LanguageMap lm = new LanguageMap();
        lm.put("en-ts", "User has completed quiz " + quizId);
        ad.setName(lm);
        activity.setDefinition(ad);
        publishStatement(agent, verb, activity);
    }

    public List<Statement> getUserAnswersOnAllQuizes(String phonenumber) throws MalformedURLException, Exception {
        RemoteLRS lrs = configureLRS();
        StatementsQuery sq = new StatementsQuery();
        //          NE RADI IM FILTER PO AGENTU ! ( API IMA BUG, NE FILTRIRA PO USER-U)
        Agent agent = new Agent();
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
        agentAccount.setName(phonenumber);
        agent.setAccount(agentAccount);
        agent.setName(phonenumber);

        sq.setActor(agent);
        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/answered");

        sq.setVerbID(verb.getId().toString());
        StatementsResult queryStatements = lrs.queryStatements(sq);

        ArrayList<Statement> statements = queryStatements.getStatements();
        ArrayList<Statement> answers = new ArrayList<>();
        for (Statement s : statements) {
            if (s.getActor() != null && s.getActor().getAccount() != null && s.getActor().getAccount().getName() != null) {
                if (s.getActor().getAccount().getHomePage().equals(agent.getAccount().getHomePage())) {
                    if (s.getActor().getAccount().getName().equals(agent.getAccount().getName())) {
                        answers.add(s);
                    }
                }
            }
        }
        return answers;

    }

    // COLLECTING STATEMENTS
    public List<Statement> getAllAnswersOnAllQuizesForUser(String userPhoneNumber) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        return filterStatements;
    }

    public List<Statement> getAllCorrectAnswersOnAllQuizesForUser(String userPhoneNumber) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/correctAnswer")) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllWrongAnswersOnAllQuizesForUser(String userPhoneNumber) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/wrongAnswer")) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllAnswersForUserOnQuiz(String userPhoneNumber, String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllCorrectAnswersForUserOnQuiz(String userPhoneNumber, String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/correctAnswer/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllWrongAnswersForUserOnQuiz(String userPhoneNumber, String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/wrongAnswer/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> gettAllAnswersForQuiz(String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllCorrectAnswersForQuiz(String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/correctAnswer/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllWrongAnswersForQuiz(String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/answered");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/wrongAnswer/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getUserLoggedInAppStatementsForUser(String userPhoneNumber) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "https://brindlewaye.com/xAPITerms/verbs/loggedin/");
        return filterStatements;
    }

    public List<Statement> getUserLoggedInAppStatements() throws Exception {
        List<Statement> filterStatements = filterStatements(null, "https://brindlewaye.com/xAPITerms/verbs/loggedin/");
        return filterStatements;
    }

    public List<Statement> getUserReadPageForCourse(String userPhoneNumber, String courseId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "https://w3id.org/xapi/adb/verbs/read");

        //String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/readCoursePage/course:" + courseId)) {
                returnList.add(s);
            }
        });
        return returnList;

    }

    public List<Statement> getReadPagesForCourse(String courseId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "https://w3id.org/xapi/adb/verbs/read");

        //String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/readCoursePage/course:" + courseId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getUserFinishedReadingCourseStatements(String userPhoneNumber) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/completed");
        //String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/finishedReadingCourse/course:")) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllUsersWhoFinishedReadingCourseForCourse(String courseId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/completed");
        //String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/finishedReadingCourse/course:" + courseId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getUserCompletedQuiz(String userPhoneNumber, String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/completed");
        //String activityURI = "http://dusanDiplomski/readCoursePage/course:" + courseId + "#" + "page:" + pageNumber;
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/completedQuiz/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;

    }

    public List<Statement> getAllUsersWhoCompletedQuizForQuiz(String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/completed");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/completedQuiz/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getUserScoreForQuizStatement(String userPhoneNumber, String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(userPhoneNumber, "http://adlnet.gov/expapi/verbs/scored");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/score/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    public List<Statement> getAllUserScoresForQuiz(String quizId) throws Exception {
        List<Statement> filterStatements = filterStatements(null, "http://adlnet.gov/expapi/verbs/scored");
        ArrayList<Statement> returnList = new ArrayList<>();
        filterStatements.forEach((s) -> {
            JsonNode statementObject = s.getObject().toJSONNode(TCAPIVersion.V095);
            JsonNode objectId = statementObject.get("id");
            String objectIdString = objectId.textValue();
            if (objectIdString.contains("http://dusanDiplomski/score/quiz:" + quizId)) {
                returnList.add(s);
            }
        });
        return returnList;
    }

    // HELPER FUNCTIONS
    public List<Statement> filterStatements(String agentPhoneNumber, String verbId) throws MalformedURLException, Exception {
        if (agentPhoneNumber == null && verbId == null) {
            throw new Exception("No filters in query");
        }
        RemoteLRS lrs = configureLRS();
        StatementsQuery sq = new StatementsQuery();
        Agent agent = null;
        Verb verb = null;
        if (agentPhoneNumber != null) {
            agent = new Agent();
            AgentAccount agentAccount = new AgentAccount();
            agentAccount.setHomePage("https://simulator.africastalking.com:1517/simulator/ussd");
            agentAccount.setName(agentPhoneNumber);
            agent.setAccount(agentAccount);
            agent.setName(agentPhoneNumber);
            sq.setActor(agent);
        }
        if (verbId != null) {
            verb = new Verb(verbId);
            sq.setVerbID(verb);
        }
        ArrayList<Statement> filteredStatements = new ArrayList<>();
        // query LRS
        StatementsResult queryStatements = lrs.queryStatements(sq);
        ArrayList<Statement> statements = queryStatements.getStatements();
        // FILTER PO AGENTU NE RADI ZA JAVA API, PA MORAMO SAMI TO DA ODRADIMO ! ( filter po Verb-u i Object-u radi )
        if (agent != null) {
            List<Statement> filterByAgentStatements = this.filterByAgent(agent, statements);
            filteredStatements = (ArrayList<Statement>) filterByAgentStatements;
        } else {
            filteredStatements = statements;
        }
        return filteredStatements;

    }

    private List<Statement> filterByAgent(Agent agent, List<Statement> list) {
        ArrayList<Statement> agentStatements = new ArrayList<>();
        for (Statement s : list) {
            if (s.getActor() != null && s.getActor().getAccount() != null && s.getActor().getAccount().getName() != null) {
                if (s.getActor().getAccount().getHomePage().equals(agent.getAccount().getHomePage())) {
                    if (s.getActor().getAccount().getName().equals(agent.getAccount().getName())) {
                        agentStatements.add(s);
                    }
                }
            }
        }
        return agentStatements;
    }

}
