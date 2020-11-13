package com.example.datasources;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public final class DataSourceRemote implements DataSource {

    private final Flux<Item> fixtures;

    public DataSourceRemote(Flux<Item> fixtures) {
        this.fixtures = Flux.from(fixtures);
    }

    @Override
    public Mono<DataSourceResult> findItemById(long id) {
        return fixtures.filter(item -> item.getId() == id)
            .map(item -> new DataSourceResult(DataSource.Type.REMOTE, item))
            .next()
            .delayElement(Duration.ofMillis(1000));
    }
}
