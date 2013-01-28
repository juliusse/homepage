package controllers;

import java.util.ArrayList;

import models.Project;

import play.*;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public final static String SESSION_LANGKEY = "langkey";
    
  public static Result index(String langKey) {
    setSessionLang(langKey);
    return ok(index.render(new ArrayList<Project>(),new ArrayList<Project>()));
  }
  
  public static Result contact(String langKey) {
      setSessionLang(langKey);
      
      return ok(contact.render());  
  }
  
  public static Result autoSelectLanguage() {
      
      if(request().host().endsWith(".de"))
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
  
  public static String getCurrentRouteWithOtherLang(String langKey) {
      return  "/"+langKey+request().uri().substring(3);
  }
  
  public static String messages(String key) {
      return Messages.get(Lang.forCode(getSessionLang()), key);
  }
  
  public static String toLower(String string) {
      return string.toLowerCase();
  }
  
  public static String getCurrentLangKey() {
      return Lang.defaultLang().code();
  }
  
}