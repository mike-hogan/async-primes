package com.softwareconfidence.primes.rest;

import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.*;
import com.softwareconfidence.primes.domain.JobStore;
import com.softwareconfidence.primes.domain.PrimesCalculator;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.json.Json.json;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.HttpHeaders.LOCATION;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.Response.*;
import static com.googlecode.utterlyidle.Status.NOT_FOUND;

public class PrimesResource {

    private final Redirector redirector;
    private final PrimesCalculator primesCalculator;
    private final JobStore jobStore;

    public PrimesResource(Redirector redirector, PrimesCalculator primesCalculator, JobStore jobStore) {
        this.redirector = redirector;
        this.primesCalculator = primesCalculator;
        this.jobStore = jobStore;
    }

    @POST
    @Path("/primes")
    public Response getPrimes(@QueryParam("upto") Long upto) {
        //Could/should be a distributed work queue and off-process persistence
        Future<List<Number>> job = primesCalculator.submit(upto);
        final UUID jobId = jobStore.put(job);
        return accepted().header(LOCATION,redirector.uriOf(method(on(PrimesResource.class).getResult(jobId))));
    }

    @GET
    @Path("/result/{id}")
    @Produces(APPLICATION_JSON)
    public Response getResult(@PathParam("id") UUID id) {
        return jobStore.get(id).map(primes -> ok().entity(json(primes))).getOrElse(response(NOT_FOUND));
    }
}
