package com.example.datasources;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public final class DataSourceCache implements DataSource {

    private final Flux<Item> fixtures;

    public DataSourceCache(Flux<Item> fixtures) {
        this.fixtures = Flux.from(fixtures);
    }

    @Override
    public Mono<DataSourceResult> findItemById(long id) {
        return fixtures.filter(item -> item.getId() == id)
            .map(item -> new DataSourceResult(DataSource.Type.CACHE, item))
            .next()
            .delayElement(Duration.ofMillis(250));
    }
}
