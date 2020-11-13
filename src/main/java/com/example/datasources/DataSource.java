package com.example.datasources;

import reactor.core.publisher.Mono;

public interface DataSource {

    enum Type {
        CACHE, LOCAL, REMOTE
    }

    Mono<DataSourceResult> findItemById(long id);
}
