package it.codedump.camelosgi;

import org.apache.camel.builder.RouteBuilder;


public class MyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	
        from("timer:myTimer?period=2000")
            .routeId("printMessageRoute") // Optional route ID
            .setBody(constant("Hello, Camel!")) // Set the message body to "Hello, Camel!"
            .to("log:console"); // Log the message to the Karaf console
    }

	
}
