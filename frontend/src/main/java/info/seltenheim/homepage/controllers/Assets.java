package info.seltenheim.homepage.controllers;

import javax.inject.Inject;

import controllers.AssetsBuilder;
import play.api.Logger;
import play.api.http.HttpErrorHandler;
import play.api.mvc.*;

public class Assets extends AssetsBuilder {
    
    @Inject
    public Assets(HttpErrorHandler errorHandler) {
        super(errorHandler);
    }

    @Override
    public Action<AnyContent> at(String path, String file, boolean aggressiveCaching) {
        play.Logger.info(path+file);
        return super.at("/public/" + path, file, aggressiveCaching);
    }

    public Action<AnyContent> at(String file) {
        return this.at("", file, false);
    }
}