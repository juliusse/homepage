import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {

    @Override
    public void onStart(Application arg0) {
        super.onStart(arg0);
        
        
        if (User.findAll().size() == 0) { //server restarted
            populateDatabase();
        }
    }
    
    private void populateDatabase() { 
        new User("Julius", "mail@julius-seltenheim.com", "secret").save();
    }
    
}
