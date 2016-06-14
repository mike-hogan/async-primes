package com.softwareconfidence.primes;

import com.googlecode.utterlyidle.httpserver.RestServer;

import static com.googlecode.utterlyidle.ServerConfiguration.defaultConfiguration;
import static com.softwareconfidence.primes.rest.PrimesApplication.setupApplication;
import static java.lang.String.format;

public class Main {

    public static void main(String[] args) throws Exception {
        RestServer server = new RestServer(setupApplication(), defaultConfiguration().port(8080));

        System.out.println(format("\nVisit %sprimes?upto=10", server.uri()));
    }
}
