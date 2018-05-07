package org.wingtree.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.stereotype.Component;

/**
 * Provides driver for embedded neo4j connection
 */
@Component
public interface EmbeddedNeo4jDriverProvider
{
    GraphDatabaseService get();
}
