package org.wingtree.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@EnableAutoConfiguration
@Controller
public class HttpServer
{
    public void run(final String[] args)
    {
        //TODO add endpoints for returning results
        SpringApplication.run(HttpServer.class, args);
    }

    @RequestMapping(path = "/lantern/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public void getReadingsFromDevicesOnLanternWithId(@RequestParam String id)
    {

    }
}
