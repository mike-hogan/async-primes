package com.softwareconfidence.primes;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.MediaType;
import com.googlecode.utterlyidle.Response;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.HttpHeaders.LOCATION;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.Request.post;
import static com.googlecode.utterlyidle.Status.OK;
import static com.softwareconfidence.primes.FunctionalTest.pollAndAssert;
import static com.softwareconfidence.primes.rest.PrimesApplication.setupApplication;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

// To be run as CD pipeline stage following running of FunctionalTest
public class SizingTest {

    // Timeout value will need to be parameterised to suit continuous delivery pipeline hardware performance
    // 8 seconds is fine on my machine, being generous with 30
    @Rule
    public Timeout globalTimeout = new Timeout(30000);

    private final Application app = setupApplication();

    @Test
    public void handlesReasonablyLargeValues() throws Exception {
        // Could stream the result as an option, or implement paging
        Response submission = app.handle(post("/primes?upto=2000000"));
        pollAndAssert(app, submission.header(LOCATION).get(), OK, response -> {
            assertThat(response.header(CONTENT_TYPE).getOrElse("Not provided"), containsString(APPLICATION_JSON));
            return null;
        });
    }
}
