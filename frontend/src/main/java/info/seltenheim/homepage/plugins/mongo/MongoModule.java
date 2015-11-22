package info.seltenheim.homepage.plugins.mongo;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;

import scala.collection.Seq;

public class MongoModule extends Module {
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(bind(MongoService.class).to(MongoServiceImpl.class));
    }
}
