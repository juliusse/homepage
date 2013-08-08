package controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.api.templates.Html;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import services.database.DatabaseService;
import services.database.Education;
import services.database.Employment;
import services.database.Position;
import services.database.Project;

@Component
public class Application extends Controller {
    public final static String SESSION_LANGKEY = "langkey";

    @Autowired
    private DatabaseService databaseService;

    public Result index(String langKey) throws IOException {
        final List<Project> spProjects = databaseService.findProjectsForStartPage();
        Collections.sort(spProjects, new NewestProjectsFirstComparator());

        final List<Position> currentPositions = databaseService.findCurrentPositions();
        Collections.sort(currentPositions, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                if (o1 instanceof Employment && o2 instanceof Education) {
                    return -1;
                } else if (o1 instanceof Education && o2 instanceof Employment) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return ok(views.html.index.render(databaseService.findProjectsForCurrent(), spProjects, currentPositions));
    }

    public Result contact(String langKey) {
        return ok(views.html.contact.render());
    }

    public Result indexWithLanguageAutoSelect() throws IOException {
        // english is default lang
        // currently only check needed if German is accepted
        String langKey = "en";

        final List<Lang> langs = request().acceptLanguages();
        boolean acceptGerman = false;
        boolean acceptEnglish = false;
        for (Lang lang : langs) {
            if (lang.language().equals("de")) {
                acceptGerman = true;
            } else if (lang.language().equals("en")) {
                acceptEnglish = true;
            }
        }
        if (acceptGerman) {
            langKey = "de";
        } else if (!acceptEnglish && request().host().endsWith(".de")) {
            langKey = "de";
        }

        return index(langKey);
    }

    public static void setSessionLang(String langKey) {
        session(SESSION_LANGKEY, langKey);
    }

    public static String getSessionLang() {
        return session(SESSION_LANGKEY);
    }

    public static Locale getCurrentLocale() {
        return Lang.forCode(getSessionLang()).toLocale();
    }

    public static String getCurrentRouteWithOtherLang(String langKey) {
        // index page
        if(request().uri().length() < 3) { //no language key present
            return routes.Application.index(langKey).toString();
        }
        return "/" + langKey + request().uri().substring(3);
    }

    public static Html messages(String key) {
        return Html.apply(Messages.get(Lang.forCode(getSessionLang()), key));
    }

    public static String toLower(String string) {
        return string.toLowerCase();
    }

    private static final class NewestProjectsFirstComparator implements Comparator<Project> {
        @Override
        public int compare(Project o1, Project o2) {
            final DateTime end1 = o1.getDevelopmentEnd();
            final DateTime end2 = o2.getDevelopmentEnd();
            if (end1 == null && end2 == null) {
                return 0;
            } else if (end1 == null) {
                return 1;
            } else if (end2 == null) {
                return -1;
            } else {
                return -end1.compareTo(end2);
            }
        }
    }
}