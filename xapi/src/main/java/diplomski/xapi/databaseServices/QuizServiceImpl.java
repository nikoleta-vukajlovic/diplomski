/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices;

import diplomski.xapi.databaseServices.interfaces.QuizService;
import diplomski.xapi.jooq.model.Tables;
import diplomski.xapi.jooq.model.tables.pojos.Quiz;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    DSLContext context;

    @Override
    public void addQuiz(Quiz quiz) {
        try {
            Quiz addedQuiz = new Quiz();
            addedQuiz = context.insertInto(Tables.QUIZ,
                    Tables.QUIZ.ID)
                    .values(quiz.getId())
                    .returning()
                    .fetchOne()
                    .into(Quiz.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public Quiz getQuiz(String quizId) {
        try {
            Quiz quiz = new Quiz();
            quiz = context.selectFrom(Tables.QUIZ)
                    .where(Tables.QUIZ.ID.equal(quizId))
                    .fetchOne()
                    .into(Quiz.class);
            return quiz;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
