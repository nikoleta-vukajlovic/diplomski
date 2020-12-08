/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.databaseServices;

import diplomski.xapi.databaseServices.interfaces.CourseService;
import diplomski.xapi.jooq.model.Tables;
import diplomski.xapi.jooq.model.tables.pojos.Course;
import diplomski.xapi.jooq.model.tables.records.CourseRecord;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Component
public class CourseServiceImpl implements CourseService {

    @Autowired
    DSLContext context;

    @Override
    public void addCourse(Course course) {
        try {
            Course addedCourse = new Course();
            addedCourse = context.insertInto(Tables.COURSE,
                    Tables.COURSE.NAME, Tables.COURSE.ID)
                    .values(course.getName(), course.getId())
                    .returning()
                    .fetchOne()
                    .into(Course.class);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public Course getCourse(String courseId) {
        try {
            Course course = new Course();
            course = context.selectFrom(Tables.COURSE)
                    .where(Tables.COURSE.ID.equal(courseId))
                    .fetchOne()
                    .into(Course.class);
            return course;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
