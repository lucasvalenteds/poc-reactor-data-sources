# POC: Reactor Data Sources

It demonstrates how to handle multiple data sources using a reactive approach.

The goal is to use the data from the source that returns first in a `Flux<T>` from three main data sources: cache, local source and remote source. One example of each data source type may be Redis, Postgres and a REST API respectively.

Assuming the data sources mentioned before, when querying an object by it's ID we want a flow that follow the pattern: First try to find it in cache, then try on local database if empty and make a remote API call when neither returns the object.

## How to run

| Description | Command |
| :--- | :--- |
| Run tests | `./gradlew test` |
