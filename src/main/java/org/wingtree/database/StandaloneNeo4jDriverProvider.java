package org.wingtree.database;

import org.neo4j.driver.v1.Driver;
import org.springframework.stereotype.Component;

@Component
public interface StandaloneNeo4jDriverProvider
{
    Driver get();
}
