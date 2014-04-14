package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.comparators.DateRangeComparators;
import info.seltenheim.homepage.services.projects.Project;
import info.seltenheim.homepage.services.projects.ProjectsService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {
    @Autowired
    private ProjectsService projectsService;

    public Result index(String langKey) throws IOException {
        final List<Project> allProjects = projectsService.findProjectsForStartPage();
        allProjects.addAll(projectsService.findProjectsForCurrent());

        Collections.sort(allProjects, DateRangeComparators::compareEndDateTimes);

        return ok(info.seltenheim.homepage.views.html.index.render(allProjects));
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
}