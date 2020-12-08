/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices;

import diplomski.xapi.databaseServices.interfaces.QuizPageService;
import diplomski.xapi.jooq.model.Tables;
import diplomski.xapi.jooq.model.tables.pojos.QuizPages;
import java.util.ArrayList;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class QuizPageServiceImpl implements QuizPageService {

    @Autowired
    DSLContext context;

    @Override
    public void addQuizPage(QuizPages quizPage) {
        try {
            QuizPages addedCoursePage = new QuizPages();
            addedCoursePage = context.insertInto(Tables.QUIZ_PAGES,
                    Tables.QUIZ_PAGES.QUIZID, Tables.QUIZ_PAGES.PAGE, Tables.QUIZ_PAGES.QUESTION, Tables.QUIZ_PAGES.ANSWER)
                    .values(quizPage.getQuizid(), quizPage.getPage(), quizPage.getQuestion(), quizPage.getAnswer())
                    .returning()
                    .fetchOne()
                    .into(QuizPages.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public List<QuizPages> getQuizPagesForCourse(String quizId) {
        try {
            ArrayList<QuizPages> quizPages = new ArrayList<QuizPages>();

            for (QuizPages page : context.selectFrom(Tables.QUIZ_PAGES)
                    .where(Tables.QUIZ_PAGES.QUIZID.equal(quizId))
                    .orderBy(Tables.QUIZ_PAGES.PAGE)
                    .fetch()
                    .into(QuizPages.class)) {
                quizPages.add(page);
            }
            return quizPages;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

}
