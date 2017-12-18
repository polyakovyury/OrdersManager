package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Improvement;
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
public class ImprovementRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ImprovementRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Map<String, String>> options() {
        return supplyAsync(() -> ebeanServer.find(Improvement.class).orderBy("name").findList(), executionContext)
                .thenApply(list -> {
                    HashMap<String, String> options = new LinkedHashMap<String, String>();
                    for (Improvement c : list) {
                        options.put(c.id.toString(), c.name);
                    }
                    return options;
                });
    }

    public CompletionStage<Long> insert(Improvement improvement) {
        return supplyAsync(() -> {
            improvement.id = System.currentTimeMillis(); //TODO: not ideal, but it works
            ebeanServer.insert(improvement);
            return improvement.id;
        }, executionContext);
    }
}