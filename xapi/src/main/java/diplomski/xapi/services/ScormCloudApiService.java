/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.services;

import ch.qos.logback.core.util.FileUtil;
import com.rusticisoftware.hostedengine.client.Configuration;
import com.rusticisoftware.hostedengine.client.ScormCloud;
import diplomski.xapi.databaseServices.interfaces.CoursePagesService;
import diplomski.xapi.databaseServices.interfaces.CourseService;
import diplomski.xapi.databaseServices.interfaces.QuizPageService;
import diplomski.xapi.databaseServices.interfaces.QuizService;
import diplomski.xapi.jooq.model.tables.pojos.QuizPages;
import diplomski.xapi.jooq.model.tables.pojos.Course;
import diplomski.xapi.jooq.model.tables.pojos.CoursePages;
import diplomski.xapi.jooq.model.tables.pojos.Quiz;
import diplomski.xapi.states.enums.xApiEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class ScormCloudApiService {

    @Autowired
    CourseService courseService;

    @Autowired
    CoursePagesService coursePagesService;

    @Autowired
    QuizService quizService;

    @Autowired
    QuizPageService quizPageService;

    private Configuration getScormCloudApiConfig() {
        Configuration cfg = new Configuration(
                "https://cloud.scorm.com/EngineWebServices",
                "MVWK6547FO",
                "4pi1EEmrWPxfWnnt6y4hBATPAmAYNxO5kPNr9mEe",
                "your origin description string");
        return cfg;
    }
    // COURSE SECTION
    // vraca text za dati page, upisuje sve u bazu ako nije vec upisano

    public String getTextDataForCourse(String courseId, int pageNumber) throws Exception {
        boolean test = testIfCourseCreatedCreateIfNot(courseId);
        if (test) {
            CoursePages coursePage = coursePagesService.getCoursePage(courseId, pageNumber);
            if (coursePage == null) {
                return xApiEnum.PAGE_OUT_OF_BOUNDS.toString();
            }
            return coursePage.getDatatext();
        } else {
            return xApiEnum.COURSE_DOESNT_EXIST.toString();
        }
    }

    private boolean testIfCourseCreatedCreateIfNot(String courseId) throws Exception {
        Course courseFromDatabase = courseService.getCourse(courseId);
        if (courseFromDatabase != null) {    // kurs vec postoji u bazi
            return true;
        } else {  // kurs ne postoji u bazi
            ScormCloud.setConfiguration(this.getScormCloudApiConfig());
            boolean Exists = ScormCloud.getCourseService().Exists(courseId);
            if (Exists) {  // kurs postoji na scorm-u
                String downloadedAndUnzipedContentText = downloadAndUnzipContent(courseId);
                String[] splitStringEvery = splitStringEvery(downloadedAndUnzipedContentText, 280);
                courseService.addCourse(new Course("kurs", courseId)); // PROMENITI NAME NA NESTO DRUGO
                for (int i = 0; i < splitStringEvery.length; i++) {
                    CoursePages coursePage = new CoursePages(i, splitStringEvery[i], courseId);
                    coursePagesService.addCoursePage(coursePage);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private String downloadAndUnzipContent(String courseId) throws Exception {
        File oldCourse = new File("kurs.zip"); // ako ne postoji nista ne uradi
        oldCourse.delete();
        ScormCloud.getCourseService().GetAssets("kurs.zip", courseId); //pribavlja kurs u .zip formatu
        String zipFilePath = "kurs.zip";
        String destDir = "courseData";
        File oldDestCourse = new File("courseData");
        FileUtils.deleteDirectory(oldDestCourse);
        unzip(zipFilePath, destDir);   // unzipuje loadovan kurs

        int pageNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String contents = null;
                Path path = Paths.get("courseData/res/data/slide" + pageNumber + ".js");
                contents = Files.readString(path, StandardCharsets.ISO_8859_1); // Handle exception
                String htmlString1 = contents.substring(109);
                String htmlString2 = htmlString1.substring(0, htmlString1.length() - 8);
                Document parsedHtml = Jsoup.parse(htmlString2);
                Elements elementsByTag = parsedHtml.getElementsByTag("span");
                Node parent = null;
                for (int i = 0; i < elementsByTag.size(); i++) {
                    if (parent == null) {
                        parent = elementsByTag.get(i).parentNode();
                    } else if (parent != elementsByTag.get(i).parentNode()) {
                        parent = elementsByTag.get(i).parentNode();
                        stringBuilder.append("\n");
                    }
                    boolean flagSpace = false;
                    Element childElement = elementsByTag.get(i);
                    String text = childElement.text();
                    if (text.charAt(text.length() - 1) == 'Â') {
                        flagSpace = true;
                    }
                    String replacedString = text.replaceAll("[^\\x20-\\x7e]", ""); // izbacuje sve non ascii karaktere iz stringa
                    if (flagSpace == true) {
                        replacedString += " ";
                    }
                    stringBuilder.append(replacedString);
                }
                stringBuilder.append("\n");
                pageNumber++;
            } catch (Exception e) {
                return stringBuilder.toString().trim().replaceAll("\\s{2,}", " ");
            }
        }
    }

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    public Integer getLastPageForCourse(String courseId) {
        return coursePagesService.numberOfPagesForCourse(courseId);
    }

    // QUIZ SECTION
    public List<QuizPages> getQuizPages(String quizId) throws Exception {
        boolean test = testIfQuizCreatedCreateIfNot(quizId);
        if (test) {
            List<QuizPages> quizPages = quizPageService.getQuizPagesForCourse(quizId);
            return quizPages;
        } else {
            return null;
        }

    }

    private boolean testIfQuizCreatedCreateIfNot(String quizId) throws Exception {
        Quiz quizFromDatabase = quizService.getQuiz(quizId);
        if (quizFromDatabase != null) {    // kurs vec postoji u bazi
            return true;
        } else {  // kurs ne postoji u bazi
            ScormCloud.setConfiguration(this.getScormCloudApiConfig());
            boolean Exists = ScormCloud.getCourseService().Exists(quizId);
            if (Exists) {  // kurs postoji na scorm-u
                downloadQuizAndPutItInDatabase(quizId);
                return true;
            } else {
                return false;
            }
        }
    }

    private void downloadQuizAndPutItInDatabase(String quizId) throws Exception {

        File oldQuiz = new File("quiz.zip"); // ako ne postoji nista ne uradi
        oldQuiz.delete();
        ScormCloud.getCourseService().GetAssets("quiz.zip", quizId); //pribavlja kurs u .zip formatu
        String zipFilePath = "quiz.zip";
        String destDir = "quizData";
        File oldDestQuiz = new File("quizData");
        FileUtils.deleteDirectory(oldDestQuiz);
        unzip(zipFilePath, destDir);   // unzipuje loadovan kurs
        int questionNumber = 1;
        quizService.addQuiz(new Quiz(quizId));
        while (true) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String contents = null;
                Path path = Paths.get("quizData/res/data/slide" + questionNumber + ".js");
                contents = Files.readString(path, StandardCharsets.ISO_8859_1); // Handle exception
                String htmlString1 = contents.substring(109);
                String htmlString2 = htmlString1.substring(0, htmlString1.length() - 8);
                Document parsedHtml = Jsoup.parse(htmlString2);

                String answerForQuestion = null;
                Elements elementsByTag = parsedHtml.getElementsByTag("span");
                Node parent = null;
                for (int i = 0; i < elementsByTag.size(); i++) {
                    if (parent == null) {
                        parent = elementsByTag.get(i).parentNode();
                    } else if (parent != elementsByTag.get(i).parentNode()) {
                        parent = elementsByTag.get(i).parentNode();
                        stringBuilder.append("\n");
                    }
                    boolean flagSpace = false;
                    Element childElement = elementsByTag.get(i);
                    String text = childElement.text();

                    if (text.charAt(text.length() - 1) == 'Â') {
                        flagSpace = true;
                    }
                    String replacedString = text.replaceAll("[^\\x20-\\x7e]", ""); // izbacuje sve non ascii karaktere iz stringa
                    if (replacedString.contains("answer->")) {
                        String[] splitedAnswer = replacedString.split("answer->");
                        answerForQuestion = splitedAnswer[1];
                    }
                    if (flagSpace == true) {
                        replacedString += " ";
                    }
                    // sve upisujemo osim ako je odgovor
                    if (!replacedString.contains(("answer->"))) {
                        stringBuilder.append(replacedString);
                    }

                }
                QuizPages qp = new QuizPages();
                qp.setPage(questionNumber);
                qp.setQuestion(stringBuilder.toString().trim().replaceAll("\\s{2,}", " ")); // izbacuje suvisne whitespace-ove
                qp.setAnswer(answerForQuestion);
                qp.setQuizid(quizId);
                quizPageService.addQuizPage(qp);
                questionNumber++;
            } catch (Exception e) {
                return;
            }
        }
    }

}
