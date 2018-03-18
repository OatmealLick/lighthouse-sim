package org.wingtree.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

@EnableAutoConfiguration
@Controller
public class HttpServer
{
    public void run(final String[] args)
    {
        //TODO add endpoints for returning results
        SpringApplication.run(HttpServer.class, args);
    }
}
