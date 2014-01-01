package info.seltenheim.homepage.plugins;

import info.seltenheim.homepage.configuration.SpringConfiguration;
import info.seltenheim.homepage.services.filesystem.FileSystemService;
import info.seltenheim.homepage.services.projects.Project;
import info.seltenheim.homepage.services.projects.Project.ProjectType;
import info.seltenheim.homepage.services.projects.ProjectsService;
import info.seltenheim.homepage.services.users.User;
import info.seltenheim.homepage.services.users.UserService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

import com.google.common.collect.Lists;

/**
 * This plugin loads sample project that are listed at
 * <strong>application.projects.fixtures</strong>.<br>
 * It also adds user that are given as key-value pairs at
 * <strong>application.users.fixtures</strong>.
 */
public final class FixturePlugin extends Plugin {
    private final Application application;

    public FixturePlugin(Application application) {
        this.application = application;
    }

    public boolean enabled() {
        return application.configuration().getBoolean("embed.mongo.enabled");
    }

    public void onStart() {
        final Configuration conf = application.configuration();
        Logger.debug("Setting up Project fixtures...");
        final List<String> fixtureFilePaths = conf.getStringList("application.projects.fixtures", Lists.<String> newArrayList());
        for (final String path : fixtureFilePaths) {
            try {
                addProject(path);
            } catch (IOException e) {
                Logger.error("can't setup fixtures", e);
            }
        }

        // add user
        final List<String> fixtureUsers = conf.getStringList("application.users.fixtures", Lists.<String> newArrayList());
        for (String userAndPw : fixtureUsers) {
            final String[] parts = userAndPw.split(":");
            try {
                addUser(parts[0], parts[1]);
            } catch (IOException e) {
                Logger.error("can't setup fixtures", e);
            }
        }
    }

    private void addUser(String username, String password) throws IOException {
        final UserService service = SpringConfiguration.getBean(UserService.class);
        service.upsertUser(new User(username, password));
    }

    private void addProject(String path) throws IOException {
        try {
            final ProjectsService service = SpringConfiguration.getBean(ProjectsService.class);
            final FileSystemService fsService = SpringConfiguration.getBean(FileSystemService.class);
            Logger.debug(service.getClass().getName() + "; " + fsService.getClass().getName());
            final File projectFolder = new File(path);
            final JsonNode projectDefinition = new ObjectMapper().readTree(new File(projectFolder, "attributes.json"));
            final Map<String, String> titleMap = new HashMap<String, String>();
            titleMap.put("de", projectDefinition.get("titleDe").getTextValue());
            titleMap.put("en", projectDefinition.get("titleEn").getTextValue());
            final Map<String, String> descriptionMap = new HashMap<String, String>();
            descriptionMap.put("de", projectDefinition.get("descrDe").getTextValue());
            descriptionMap.put("en", projectDefinition.get("descrEn").getTextValue());

            final boolean displayOnStartPage = projectDefinition.get("displayOnStartPage").asBoolean();
            final List<String> technologyList = Arrays.asList(projectDefinition.get("techs").getTextValue().split(","));
            final DateTime developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime(projectDefinition.get("devStart").getTextValue());
            final DateTime developmentEnd = Project.PROJECT_DATETIME_FORMAT.parseDateTime(projectDefinition.get("devEnd").getTextValue());

            final String filePath = null;
            byte[] imageBytes = FileUtils.readFileToByteArray(new File(projectFolder, "image.jpg"));
            final String mainImagePath = fsService.saveImage(imageBytes, "jpg");

            final List<ProjectType> typeOf = new ArrayList<ProjectType>();
            for (String type : projectDefinition.get("ofType").getTextValue().split(",")) {
                typeOf.add(ProjectType.valueOf(type));
            }

            final Project project = new Project(titleMap, descriptionMap, displayOnStartPage, technologyList, developmentStart, developmentEnd, mainImagePath, filePath, typeOf);
            service.upsertProject(project);
        } catch (Exception e) {
            Logger.error("error!", e);
        }

    }
}
