package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import models.Education;
import models.Employment;
import models.Position;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.api.templates.Html;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import services.database.DatabaseService;
import services.database.Project;


@Component
public class Application extends Controller {
    public final static String SESSION_LANGKEY = "langkey";
    
    @Autowired
    private DatabaseService databaseService;
    
    public Result index(String langKey) throws IOException {
        setSessionLang(langKey);
        List<Project> spProjects = databaseService.findProjectsForStartPage();
        Collections.sort(spProjects, new NewestProjectsFirstComparator());
        
        //get current positions
        final List<Position> positions = new ArrayList<Position>();
        if(langKey.equals("de")) {
            //positions.add(new Employment(new DateTime(2013,8,1,1,1), new DateTime(2013,10,1,1,1), "Rohde & Schwarz SIT", "Praktikant", "", new ArrayList<String>()));
            positions.add(new Education(new DateTime(2012,4,1,1,1), new DateTime(2014,3,31,1,1), "HTW Berlin", "http://www.htw-berlin.de/", "---", "", "Student", "(M.Sc., Int. Medieninformatik)", "Spezialisierung: Visual Computing"));
        } else {
            positions.add(new Education(new DateTime(2012,4,1,1,1), new DateTime(2014,3,31,1,1), "HTW Berlin, University of Applied Science", "http://www-en.htw-berlin.de/", "---", "", "Student","(M.Sc., Int. Media and Computing)",
                    "Specialisation: Visual Computing"));
        }
        return ok(views.html.index.render(databaseService.findProjectsForCurrent(), spProjects, positions));
    }

    public Result contact(String langKey) {
        setSessionLang(langKey);

        return ok(views.html.contact.render());
    }

    public Result profile(String langKey) {
        setSessionLang(langKey);

        List<Employment> empl = new ArrayList<Employment>();

        if (langKey.equals("de")) {
            empl.add(new Employment(new DateTime(2012, 4, 1, 1, 1), null, "julius-seltenheim.com", "Freiberufler", "http://www.julius-seltenheim.com", new String[] { "Webentwicklung", "Softwareentwicklung",
                    "Datenbankentwicklung", "Beratung" }));
            empl.add(new Employment(
                    new DateTime(2012,10, 1, 1, 1), 
                    new DateTime(2013,07, 20, 1, 1),
                    "Docear", 
                    "Teamleiter & Javaentwickler", 
                    "http://docear.org", 
                    new String[] { "Uniprojekt, Entwicklung einer Real-Time Collaborative Mindmapping Tool","Kommunikation mit Auftraggeber", "Backendentwicklung (Java, OSGI, Play Framework, Akka,...)"}));
            empl.add(new Employment(
                    new DateTime(2013, 2, 1, 1, 1), 
                    new DateTime(2013, 6, 1, 1, 1),
                    "first fox in space",
                    "Freiberufler",  
                    "http://firstfox.com", 
                    new String[] { "Planung einer Applikation", "Implementation","IT-Infrastruktur (Jenkins CI, Linux-Server)"}));
            empl.add(new Employment(
                    new DateTime(2012, 4, 1, 1, 1), 
                    new DateTime(2012,11, 6, 1, 1),
                    "Best Ants", 
                    "Freiberufler", 
                    "", 
                    new String[] { "Planung einer Applikation", "Entwicklung eines Prototyps"}));
            empl.add(new Employment(new DateTime(2011, 5, 1, 1, 1), new DateTime(2011, 12, 31, 1, 1), "HTW Berlin", "Studentische Hilfskraft", "http://www.htw-berlin.de", new String[] {
                    "Entwicklung von Prototypen", "Unterstützende Arbeiten" }));
            empl.add(new Employment(new DateTime(2010, 10, 1, 1, 1), new DateTime(2011, 2, 25, 1, 1), "Canary Data Solutions Ltd", "Praktikant im Bereich \".Net Softwareentwicklung\"",
                    "http://canary.co.nz", new String[] { "Softwareentwicklung", "Kundensupport", "Administration", "Refactoring" }));
            empl.add(new Employment(new DateTime(2010, 3, 1, 1, 1), new DateTime(2010, 9, 30, 1, 1), "HTW Berlin", "Tutor für Mathematik", "http://www.htw-berlin.de", new String[] {
                    "Vorbereitung und Durchführung von 2 Tutorien pro Woche", "Beantwortung von Fragen per E-Mail" }));
            empl.add(new Employment(
                    new DateTime(2009,4,1,1,1),
                    new DateTime(2009,12,1,1,1),
                    "Café Anna Blume", "Team-Mitarbeiter", "http://www.cafe-anna-blume.de",
                    new String[] {"vorbereiten",
                    "servieren"}));
            empl.add(new Employment(new DateTime(2008, 7, 1, 1, 1), new DateTime(2009, 3, 31, 1, 1), "Caritas", "Zivildienst", "http://www.invia-center-berlin.de", new String[] { "Kochen", "Catering",
                    "Für Gäste sorgen", "Vorbereitung", "Servieren", "Praktikanten einweisen", "Lieferungen" }));
        } else {
            empl.add(new Employment(new DateTime(2012, 4, 1, 1, 1), null, "julius-seltenheim.com", "Freelancer", "http://www.julius-seltenheim.com", new String[] { "Webdevelopment", "Softwaredevelopment",
                    "Databasedevelopment", "Consulting" }));
            empl.add(new Employment(
                    new DateTime(2012,10, 1, 1, 1), 
                    new DateTime(2013,07, 20, 1, 1),
                    "Docear", 
                    "Team Leader & Java Developer", 
                    "http://docear.org", 
                    new String[] { "University project about developping a real time mind map collaboration tool","Communication with product owner", "Backend development (Java, OSGI, Play Framework, Akka,...)"}));
            empl.add(new Employment(
                    new DateTime(2013, 2, 1, 1, 1), 
                    new DateTime(2013, 6, 1, 1, 1),
                    "first fox in space", 
                    "Freelancer", 
                    "http://firstfox.com", 
                    new String[] { "Design an application based on Play Framework (Java) and MySQL", "Implementation","Set up Jenkins Continuous Integration and Linux servers"}));
            empl.add(new Employment(
                    new DateTime(2012, 4, 1, 1, 1), 
                    new DateTime(2012,11, 6, 1, 1),
                    "Best Ants", 
                    "Freelancer", 
                    "", 
                    new String[] { "Design an application based on PHP, the Zend Framework and MySQL", "Start development of a prototype"}));
            empl.add(new Employment(new DateTime(2011, 5, 1, 1, 1), new DateTime(2011, 12, 31, 1, 1), "HTW Berlin", "Student Research Assistant", "http://www-en.htw-berlin.de", new String[] {
                    "Prototyping", "General Tasks" }));
            empl.add(new Employment(new DateTime(2010, 10, 1, 1, 1), new DateTime(2011, 2, 25, 1, 1), "Canary Data Solutions Ltd", ".Net Developer Intern", "http://canary.co.nz", new String[] {
                    "Software Development", "Customer Support", "Maintenance", "Refactoring" }));
            empl.add(new Employment(new DateTime(2010, 3, 1, 1, 1), new DateTime(2010, 9, 30, 1, 1), "HTW Berlin", "Tutor for Math", "http://www-en.htw-berlin.de", new String[] {
                    "Prepare and perform 2 tutorials per week", "E-mail support" }));
            empl.add(new Employment(
                    new DateTime(2009,4,1,1,1),
                    new DateTime(2009,12,1,1,1),
                    "Café Anna Blume", "Staff", "http://www.cafe-anna-blume.de",
                    new String[] {"Preparing",
                    "Serving"}));
            empl.add(new Employment(new DateTime(2008, 7, 1, 1, 1), new DateTime(2009, 3, 31, 1, 1), "Caritas", "Compulsory Community Service", "http://www.invia-center-berlin.de", new String[] {
                    "Cooking", "Catering", "Care for guests", "Preparing", "Serving", "Briefing Interns", "Delivery" }));
        }

        List<Education> edus = new ArrayList<Education>();
        if (langKey.equals("de")) {
            edus.add(new Education(new DateTime(2012,4,1,1,1), new DateTime(2014,3,31,1,1), "HTW Berlin", "http://www.htw-berlin.de/", "---", "","Student", "(M.Sc. in Internationaler Medieninformatik)", "Spezialisierung: Visual Computing"));
            edus.add(new Education(new DateTime(2009,4,1,1,1), new DateTime(2012,3,31,1,1), "HTW Berlin", "http://www.htw-berlin.de/", "1.3, sehr gut (A)", "", "Student", "B.Sc. in Internationaler Medieninformatik", ""));
            edus.add(new Education(new DateTime(2001,8,1,1,1), new DateTime(2008,6,30,1,1), "Hans und Hilde Coppi Gymnasium, Berlin", "http://www.coppi-gym.de/", "2.1", "","Schüler", "Abitur", ""));
        } else {
            edus.add(new Education(new DateTime(2012,4,1,1,1), new DateTime(2014,3,31,1,1), "HTW Berlin, University of Applied Science", "http://www-en.htw-berlin.de/", "---", "","Student",  "(M.Sc. in International Media and Computing)",
                    "Specialisation: Visual Computing"));
            edus.add(new Education(new DateTime(2009,4,1,1,1), new DateTime(2012,3,31,1,1), "HTW Berlin, University of Applied Science", "http://www-en.htw-berlin.de/", "1.3, very good (A)", "","Student",  "B.Sc. in International Media and Computing",
                    ""));
            edus.add(new Education(new DateTime(2001,8,1,1,1), new DateTime(2008,6,30,1,1), "Hans und Hilde Coppi Gymnasium, Berlin", "http://www.coppi-gym.de/", "2.1", "", "Student", "Abitur", ""));
        }
        
        Collections.sort(empl, new Comparator<Employment>() {
            @Override
            public int compare(Employment o1, Employment o2) {
                final DateTime end1 = o1.getToDate();
                final DateTime end2 = o2.getToDate();
                if(end1 == null && end2 == null) {
                    return 0;
                } else if(end1 == null) {
                    return -1;
                } else if(end2 == null) {
                    return 1;
                } else {
                    return -end1.compareTo(end2);
                }
            }
        });

        return ok(views.html.profile.render(empl, edus));
    }

    

    public Result autoSelectLanguage() {

        final List<Lang> langs = request().acceptLanguages();
        boolean acceptGerman = false;
        boolean acceptEnglish = false;
        for(Lang lang : langs) {
            if(lang.language().equals("de")) {
                acceptGerman = true;
            } else if(lang.language().equals("en")) {
                acceptEnglish = true;
            }    
        }
        if(acceptGerman) {
            return redirect(routes.Application.index("de"));
        } else if(acceptEnglish) {
            return redirect(routes.Application.index("en"));
        } else if (request().host().endsWith(".de"))
            return redirect(routes.Application.index("de"));
        else
            return redirect(routes.Application.index("en"));
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
            if(end1 == null && end2 == null) {
                return 0;
            } else if(end1 == null) {
                return 1;
            } else if(end2 == null) {
                return -1;
            } else {
                return -end1.compareTo(end2);
            }
        }
    }
}