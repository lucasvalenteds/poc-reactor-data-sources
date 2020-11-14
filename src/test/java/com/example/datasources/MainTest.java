package com.example.datasources;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.RepeatedTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MainTest {

    private final Item piano = new Item(1L, "Piano");
    private final Item table = new Item(2L, "Table");
    private final Item chair = new Item(3L, "Chair");
    private final Flux<Item> fixtures = Flux.fromIterable(List.of(piano, table, chair));

    @RepeatedTest(value = 10, name = "Cache is preferred over local and remote ({currentRepetition})")
    void testCache() {
        DataSource api = new DataSourceRemote(fixtures);
        DataSource database = new DataSourceLocal(fixtures);
        DataSource cache = new DataSourceCache(fixtures);

        Flux<DataSourceResult> source = Flux.concat(
            cache.findItemById(chair.getId()),
            database.findItemById(chair.getId()),
            api.findItemById(chair.getId())
        );

        StepVerifier.create(source.next())
            .assertNext(result -> {
                assertEquals(DataSource.Type.CACHE, result.getSource());
                assertEquals(chair, result.getItem());
            })
            .expectComplete()
            .verifyThenAssertThat()
            .tookLessThan(Duration.ofMillis(750));
    }

    @RepeatedTest(value = 10, name = "Local is preferred over remote ({currentRepetition})")
    void testLocal() {
        DataSource api = new DataSourceRemote(fixtures);
        DataSource database = new DataSourceLocal(fixtures);
        DataSource cache = new DataSourceCache(Flux.empty());

        Flux<DataSourceResult> source = Flux.concat(
            cache.findItemById(table.getId()),
            database.findItemById(table.getId()),
            api.findItemById(table.getId())
        );

        StepVerifier.create(source.next())
            .assertNext(result -> {
                assertEquals(DataSource.Type.LOCAL, result.getSource());
                assertEquals(table, result.getItem());
            })
            .expectComplete()
            .verifyThenAssertThat()
            .tookLessThan(Duration.ofMillis(1000));
    }

    @RepeatedTest(value = 10, name = "Remote returns when data is not cached and available locally ({currentRepetition})")
    void testRemote() {
        DataSource api = new DataSourceRemote(fixtures);
        DataSource database = new DataSourceLocal(Flux.just(chair, table));
        DataSource cache = new DataSourceCache(Flux.just(table));

        Flux<DataSourceResult> source = Flux.concat(
            cache.findItemById(piano.getId()),
            database.findItemById(piano.getId()),
            api.findItemById(piano.getId())
        );

        StepVerifier.create(source.next())
            .assertNext(result -> {
                assertEquals(DataSource.Type.REMOTE, result.getSource());
                assertEquals(piano, result.getItem());
            })
            .expectComplete()
            .verifyThenAssertThat()
            .tookMoreThan(Duration.ofMillis(1000));
    }
}
