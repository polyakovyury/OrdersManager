package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Category;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 *
 */
public class CategoryRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public CategoryRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<List<Category>> options() {
        return supplyAsync(() -> {
            return ebeanServer.find(Category.class).orderBy("name").findList();
        }, executionContext);
    }

    public CompletionStage<Long> insert(Category category) {
        return supplyAsync(() -> {
             category.id = System.currentTimeMillis(); // not ideal, but it works
             ebeanServer.insert(category);
             return category.id;
        }, executionContext);
    }
}