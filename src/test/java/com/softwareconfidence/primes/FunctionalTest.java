package com.softwareconfidence.primes;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.HttpHeaders.LOCATION;
import static com.googlecode.utterlyidle.MediaType.APPLICATION_JSON;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.Request.get;
import static com.googlecode.utterlyidle.Request.post;
import static com.googlecode.utterlyidle.Status.ACCEPTED;
import static com.googlecode.utterlyidle.Status.BAD_REQUEST;
import static com.googlecode.utterlyidle.Status.OK;
import static com.softwareconfidence.primes.rest.PrimesApplication.setupApplication;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class FunctionalTest {

    @Rule
    public Timeout globalTimeout = new Timeout(1000);

    private final Application app = setupApplication();

    @Test
    public void returnsPrimesAsJsonArray() throws Exception {
        Response submission = app.handle(post("/primes?upto=20"));

        assertThat(submission.entity().toString(), submission.status(), is(ACCEPTED));
        assertThat(submission.header(LOCATION).getOrNull(), notNullValue());

        pollAndAssert(app, submission.header(LOCATION).get(), OK, assertReturns(APPLICATION_JSON, is("[2,3,5,7,11,13,17,19]")));
    }

    @Test
    public void returnsPrimesInclusiveOfUpToValue() throws Exception {
        Response submission = app.handle(post("/primes?upto=13"));

        pollAndAssert(app, submission.header(LOCATION).get(), OK, assertReturns(APPLICATION_JSON, is("[2,3,5,7,11,13]")));
    }

    @Test
    public void handlesZeroAndNegativeUpToValues() throws Exception {
        Response submission = app.handle(post("/primes?upto=0"));
        pollAndAssert(app, submission.header(LOCATION).get(), OK, assertReturns(APPLICATION_JSON, is("[]")));

        submission = app.handle(post("/primes?upto=-1"));
        pollAndAssert(app, submission.header(LOCATION).get(), OK, assertReturns(APPLICATION_JSON, is("[]")));
    }

    @Test
    public void handlesMissingUpToValues() throws Exception {
        Response submission = app.handle(post("/primes"));
        assertThat(submission.status(),is(BAD_REQUEST));
        assertReturns(TEXT_HTML, containsString("Unsatisfiable Parameters")).call(submission);
    }

    static void pollAndAssert(Application app, String location, Status expectedStatus, Function1<Response, Void> assertion) throws Exception {
        boolean waiting = true;
        while(waiting) {
            Response primes = app.handle(get(location));
            if(primes.status() == expectedStatus) {
                waiting = false;
                assertion.call(primes);
            } else {
                sleep(50);
            }
        }
    }

    private static Function1<Response, Void> assertReturns(String contentType, Matcher<String> entityMatcher) {
        return response -> {
            assertThat(response.header(CONTENT_TYPE).getOrElse("Not provided"), containsString(contentType));
            assertThat(response.entity().toString(), entityMatcher);
            return null;
        };
    }

}
