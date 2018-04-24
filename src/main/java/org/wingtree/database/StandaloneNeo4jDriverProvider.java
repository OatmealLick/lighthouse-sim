package org.wingtree.database;

import org.neo4j.driver.v1.Driver;
import org.springframework.stereotype.Component;

/**
 * Provides driver for standalone neo4j connection.
 */
@Component
public interface StandaloneNeo4jDriverProvider
{
    Driver get();
}
