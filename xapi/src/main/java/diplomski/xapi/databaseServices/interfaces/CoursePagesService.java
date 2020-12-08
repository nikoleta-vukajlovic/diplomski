/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices.interfaces;

import diplomski.xapi.jooq.model.tables.pojos.CoursePages;
import java.util.List;

/**
 *
 * @author Dusan
 */
public interface CoursePagesService {

    void addCoursePage(CoursePages coursePage);

    CoursePages getCoursePage(String courseId, int pageNumber);

    List<CoursePages> getCoursePagesForCourse(String courseid);

    Integer numberOfPagesForCourse(String courseId);

}
