/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rusticisoftware.hostedengine.client.Configuration;
import com.rusticisoftware.hostedengine.client.ScormCloud;
import com.rusticisoftware.hostedengine.client.ScormEngineService;
import com.rusticisoftware.hostedengine.client.ServiceRequest;
import com.rusticisoftware.hostedengine.client.datatypes.CourseData;
import com.rusticisoftware.hostedengine.client.datatypes.DispatchDestination;
import com.rusticisoftware.tincan.Activity;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.RemoteLRS;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;
import diplomski.xapi.databaseServices.interfaces.CourseService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.springframework.web.servlet.function.RequestPredicates.path;

/**
 *
 * @author Dusan
 */
@Component
public class Service {

    @Autowired
    StatefulRedisConnection<String, String> redisConnection;

    public Service() throws Exception {
        //ScormTry();
        //testDatabase();
    }

    public void saveInRedis() throws MalformedURLException, Exception {
        String key = "test2";
        RedisClient redisClient = RedisClient.create("redis://dusan123@redis-17972.c15.us-east-1-2.ec2.cloud.redislabs.com:17972");

        RedisAsyncCommands<String, String> async = redisClient.connect().async();
        async.multi();
        Map<String, String> map = new HashMap<String, String>();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enableDefaultTyping();
        map.put("state", "testState2");
        async.hset(key, map);
        async.expire(key, TimeUnit.MINUTES.toSeconds(30));
        RedisFuture<TransactionResult> execResult = async.exec();
    }

    public void ScormTry() throws Exception {
        Configuration cfg = new Configuration(
                "https://cloud.scorm.com/EngineWebServices",
                "MVWK6547FO",
                "4pi1EEmrWPxfWnnt6y4hBATPAmAYNxO5kPNr9mEe",
                "your origin description string");
        ScormCloud.setConfiguration(cfg);
        boolean Exists = ScormCloud.getCourseService().Exists("random123");
        if (Exists) {
            ScormCloud.getCourseService().GetAssets("proba.zip", "random123"); //pribavlja kurs u .zip formatu
            String zipFilePath = "proba.zip";
            String destDir = "courseData";
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

                    stringBuilder.append("PAGE:" + pageNumber + "\n");
                    for (int i = 0; i < elementsByTag.size(); i++) {
                        Element childElement = elementsByTag.get(i);
                        String text = childElement.text();
                        stringBuilder.append(text + "\n");
                    }
                    pageNumber++;
                } catch (Exception e) {
                    System.out.println(stringBuilder);
                    break;
                }
            }
        }

    }

    public void XApiTry() throws MalformedURLException, Exception {
        RemoteLRS lrs;
        lrs = new RemoteLRS();
        lrs.setEndpoint("https://cloud.scorm.com/lrs/MVWK6547FO/");
        lrs.setVersion(TCAPIVersion.V095);
        lrs.setUsername("1GimU18456ndrLgC3zo");
        lrs.setPassword("uOtW0EYCphLHsSXls1M");

        Agent agent = new Agent();
        agent.setMbox("mailto:info@tincanapi.com");

        Verb verb = new Verb("http://adlnet.gov/expapi/verbs/attempted");

        Activity activity = new Activity("http://rusticisoftware.github.com/TinCanJava");

        Statement st = new Statement();
        st.setActor(agent);
        st.setVerb(verb);
        st.setObject(activity);

        lrs.saveStatement(st);
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

}
