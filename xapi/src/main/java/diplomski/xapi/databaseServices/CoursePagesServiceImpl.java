/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices;

import diplomski.xapi.databaseServices.interfaces.CoursePagesService;
import diplomski.xapi.jooq.model.Tables;
import diplomski.xapi.jooq.model.tables.pojos.CoursePages;
import diplomski.xapi.jooq.model.tables.pojos.Course;
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
public class CoursePagesServiceImpl implements CoursePagesService {

    @Autowired
    DSLContext context;

    @Override
    public void addCoursePage(CoursePages coursePage) {
        try {
            CoursePages addedCoursePage = new CoursePages();
            addedCoursePage = context.insertInto(Tables.COURSE_PAGES,
                    Tables.COURSE_PAGES.COURSEID, Tables.COURSE_PAGES.PAGENUMBER, Tables.COURSE_PAGES.DATATEXT)
                    .values(coursePage.getCourseid(), coursePage.getPagenumber(), coursePage.getDatatext())
                    .returning()
                    .fetchOne()
                    .into(CoursePages.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public CoursePages getCoursePage(String courseId, int pageNumber) {
        try {
            CoursePages coursePage = new CoursePages();
            coursePage = context.selectFrom(Tables.COURSE_PAGES)
                    .where(Tables.COURSE_PAGES.COURSEID.equal(courseId).and(Tables.COURSE_PAGES.PAGENUMBER.equal(pageNumber)))
                    .fetchOne()
                    .into(CoursePages.class);
            return coursePage;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public List<CoursePages> getCoursePagesForCourse(String courseid) {
        try {
            ArrayList<CoursePages> coursePages = new ArrayList<CoursePages>();

            for (CoursePages page : context.selectFrom(Tables.COURSE_PAGES)
                    .where(Tables.COURSE_PAGES.COURSEID.equal(courseid))
                    .orderBy(Tables.COURSE_PAGES.PAGENUMBER)
                    .fetch()
                    .into(CoursePages.class)) {
                coursePages.add(page);
            }
            return coursePages;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public Integer numberOfPagesForCourse(String courseId) {
        try {
            List<CoursePages> courses = context.selectFrom(Tables.COURSE_PAGES)
                    .where(Tables.COURSE_PAGES.COURSEID.equal(courseId))
                    .orderBy(Tables.COURSE_PAGES.PAGENUMBER.desc())
                    .limit(1)
                    .fetch()
                    .into(CoursePages.class);
            CoursePages lastPage = courses.get(0);
            return lastPage.getPagenumber();

        } catch (Exception e) {
            return null;
        }
    }

}
