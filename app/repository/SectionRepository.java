package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Section;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 *
 */
public class SectionRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public SectionRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Map<String, String>> options() {
        return supplyAsync(() -> ebeanServer.find(Section.class).orderBy("name").findList(), executionContext)
                .thenApply(list -> {
                    HashMap<String, String> options = new LinkedHashMap<String, String>();
                    for (Section c : list) {
                        options.put(c.id.toString(), c.name);
                    }
                    return options;
                });
    }

    public CompletionStage<Long> insert(Section section) {
        return supplyAsync(() -> {
             section.id = System.currentTimeMillis(); // not ideal, but it works
             ebeanServer.insert(section);
             return section.id;
        }, executionContext);
    }
}