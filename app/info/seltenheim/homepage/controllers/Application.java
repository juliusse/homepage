package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.services.positions.Education;
import info.seltenheim.homepage.services.positions.Employment;
import info.seltenheim.homepage.services.positions.Position;
import info.seltenheim.homepage.services.positions.PositionsService;
import info.seltenheim.homepage.services.projects.Project;
import info.seltenheim.homepage.services.projects.ProjectsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {
    @Autowired
    private PositionsService positionsService;

    @Autowired
    private ProjectsService projectsService;

    public Result index(String langKey) throws IOException {
        final List<Project> spProjects = projectsService.findProjectsForStartPage();
        Collections.sort(spProjects, new NewestProjectsFirstComparator());

        final List<Position> currentPositions = positionsService.findCurrentPositions();
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

        return ok(info.seltenheim.homepage.views.html.index.render(projectsService.findProjectsForCurrent(), spProjects, currentPositions));
    }

    public Result contact(String langKey) {
        return ok(info.seltenheim.homepage.views.html.contact.render());
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

    public Result getSiteMap() {

        response().setContentType("application/xml");
        final StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version='1.0' encoding='UTF-8'?>");
        xmlBuilder.append(" <urlset xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        xmlBuilder.append(" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\"");
        xmlBuilder.append(" xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // add main page
        xmlBuilder.append("");
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>http://julius-seltenheim.com</loc>");
        xmlBuilder.append("<changefreq>monthly</changefreq>");
        xmlBuilder.append("<priority>0.5</priority>");
        xmlBuilder.append("</url>");

        // add profile page
        xmlBuilder.append("");
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>http://julius-seltenheim.com/en/profile</loc>");
        xmlBuilder.append("<changefreq>monthly</changefreq>");
        xmlBuilder.append("<priority>0.8</priority>");
        xmlBuilder.append("</url>");

        // add skills page
        xmlBuilder.append("");
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>http://julius-seltenheim.com/en/skills</loc>");
        xmlBuilder.append("<changefreq>monthly</changefreq>");
        xmlBuilder.append("<priority>0.8</priority>");
        xmlBuilder.append("</url>");

        // add projects page
        xmlBuilder.append("");
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>http://julius-seltenheim.com/en/projects</loc>");
        xmlBuilder.append("<changefreq>monthly</changefreq>");
        xmlBuilder.append("<priority>0.8</priority>");
        xmlBuilder.append("</url>");

        // add contact page
        xmlBuilder.append("");
        xmlBuilder.append("<url>");
        xmlBuilder.append("<loc>http://julius-seltenheim.com/en/contact</loc>");
        xmlBuilder.append("<changefreq>monthly</changefreq>");
        xmlBuilder.append("<priority>0.3</priority>");
        xmlBuilder.append("</url>");

        xmlBuilder.append("</urlset>");

        return ok(xmlBuilder.toString());
    }

    public static String getCurrentRouteWithOtherLang(String langKey) {
        // index page
        if (request().uri().length() < 3) { // no language key present
            return routes.Application.index(langKey).toString();
        }
        return "/" + langKey + request().uri().substring(3);
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