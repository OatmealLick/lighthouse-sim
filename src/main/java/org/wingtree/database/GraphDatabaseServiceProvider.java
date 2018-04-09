package org.wingtree.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.stereotype.Component;

@Component
public interface GraphDatabaseServiceProvider {

    GraphDatabaseService get();
}
