/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices.interfaces;

import diplomski.xapi.jooq.model.tables.pojos.QuizPages;
import java.util.List;

/**
 *
 * @author Dusan
 */
public interface QuizPageService {

    void addQuizPage(QuizPages quizPage);

    List<QuizPages> getQuizPagesForCourse(String quizId);
}
