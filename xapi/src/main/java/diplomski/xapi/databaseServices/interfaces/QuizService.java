/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices.interfaces;

import diplomski.xapi.jooq.model.tables.pojos.Quiz;

/**
 *
 * @author Dusan
 */
public interface QuizService {

    void addQuiz(Quiz quiz);

    Quiz getQuiz(String quizId);

}
