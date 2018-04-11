package org.wingtree.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.stereotype.Component;

@Component
public interface EmbeddedNeo4jDriverProvider
{
    GraphDatabaseService get();
}
