package info.seltenheim.homepage.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import info.seltenheim.homepage.services.filesystem.FileSystemService;
import info.seltenheim.homepage.services.positions.Employment;
import info.seltenheim.homepage.services.positions.PositionsService;
import info.seltenheim.homepage.services.projects.Project;
import info.seltenheim.homepage.services.projects.Project.ProjectType;
import info.seltenheim.homepage.services.projects.ProjectsService;
import info.seltenheim.homepage.services.users.User;
import info.seltenheim.homepage.services.users.UserService;
import play.Application;
import play.Logger;
import play.Plugin;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

/**
 * This plugin loads sample project that are listed at
 * <strong>application.projects.fixtures</strong>.<br>
 * It also adds user that are given as key-value pairs at
 * <strong>application.users.fixtures</strong>.
 */
public final class FixturePlugin extends Module {

    @Inject
    private PositionsService positionsService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectsService projectsService;

    @Inject
    private FileSystemService fileSystemService;

    @Override
    public Seq<Binding<?>> bindings(Environment env, Configuration conf) {
        return seq();
    }

    // public void onStart() {
    // Logger.debug("Setting up Project fixtures...");
    // final List<String> fixtureFilePaths =
    // conf.getStringList("application.projects.fixtures", Lists.<String>
    // newArrayList());
    // for (final String path : fixtureFilePaths) {
    // try {
    // addProject(path);
    // } catch (IOException e) {
    // Logger.error("can't setup fixtures", e);
    // }
    // }
    //
    // // add user
    // final List<String> fixtureUsers =
    // conf.getStringList("application.users.fixtures", Lists.<String>
    // newArrayList());
    // for (String userAndPw : fixtureUsers) {
    // final String[] parts = userAndPw.split(":");
    // try {
    // addUser(parts[0], parts[1]);
    // } catch (IOException e) {
    // Logger.error("can't setup fixtures", e);
    // }
    // }
    //
    // // add an employment
    // final Map<String, String> map = new HashMap<String, String>();
    // map.put("de", "de");
    // map.put("en", "en");
    // final List<String> list = new ArrayList<String>();
    // list.add("play");
    //
    // final Map<String, List<String>> map2 = new HashMap<String,
    // List<String>>();
    // map2.put("en", list);
    // map2.put("de", list);
    // try {
    // positionsService.upsertPosition(new
    // Employment(DateTime.parse("2013-10-01"), DateTime.parse("2014-02-01"),
    // "R&S", map, "http://r-s.com", map2, list));
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    private void addUser(String username, String password) throws IOException {

        userService.upsertUser(new User(username, password));
    }

    private void addProject(String path) throws IOException {
        try {

            final File projectFolder = new File(path);
            final JsonNode projectDefinition = new ObjectMapper().readTree(new File(projectFolder, "attributes.json"));
            final Map<String, String> titleMap = new HashMap<String, String>();
            titleMap.put("de", projectDefinition.get("titleDe").textValue());
            titleMap.put("en", projectDefinition.get("titleEn").textValue());
            final Map<String, String> descriptionMap = new HashMap<String, String>();
            descriptionMap.put("de", projectDefinition.get("descrDe").textValue());
            descriptionMap.put("en", projectDefinition.get("descrEn").textValue());

            final boolean displayOnStartPage = projectDefinition.get("displayOnStartPage").asBoolean();
            final List<String> technologyList = Arrays.asList(projectDefinition.get("techs").textValue().split(","));
            final DateTime developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime(projectDefinition.get("devStart").textValue());
            final DateTime developmentEnd = Project.PROJECT_DATETIME_FORMAT.parseDateTime(projectDefinition.get("devEnd").textValue());

            final String filePath = null;
            byte[] imageBytes = FileUtils.readFileToByteArray(new File(projectFolder, "image.jpg"));
            final String mainImagePath = fileSystemService.saveImage(imageBytes, "jpg");

            final List<ProjectType> typeOf = new ArrayList<ProjectType>();
            for (String type : projectDefinition.get("ofType").textValue().split(",")) {
                typeOf.add(ProjectType.valueOf(type));
            }

            final Project project = new Project(titleMap, descriptionMap, displayOnStartPage, technologyList, developmentStart, developmentEnd, mainImagePath, filePath, typeOf);
            projectsService.upsertProject(project);
        } catch (Exception e) {
            Logger.error("error!", e);
        }

    }

}
