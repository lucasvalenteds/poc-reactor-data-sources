package com.example.datasources;

public final class DataSourceResult {

    private final DataSource.Type source;
    private final Item item;

    public DataSourceResult(final DataSource.Type source, final Item item) {
        this.source = source;
        this.item = item;
    }

    public DataSource.Type getSource() {
        return source;
    }

    public Item getItem() {
        return item;
    }
}
