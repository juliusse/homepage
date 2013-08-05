package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.mvc.Controller;
import services.database.DatabaseService;

@Component
public class TrackingsController extends Controller {

    @Autowired
    private DatabaseService databaseService;
    
    
}
