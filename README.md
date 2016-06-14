# What is this?
This is a submission to a coding challenge that required a REST service written in Java that returns prime numbers.  There was a stipulation to use Maven.

# Running
*mvn compile test exec:java*

*curl http://localhost:8080/primes?upto=10*

# Commentary

I chose to make the service super simple.  It returns a JSON array synchronously, which can tie up the server when it's delivering a large list of primes.

An option I considered was to submit the calculation of the prime array to an ExecutorService and store a Future against a UUID, then return HTTP 202 with a
Location header that contained a URL to query the progress of that job.  In the absence of explicit stipulation either way, I went with the simplest approach - synchronous.

Apart from Maven as the build tool, I was free to choose frameworks.
I went with [Utterlyidle](https://github.com/bodar/utterlyidle) and [Totallylazy](https://github.com/bodar/totallylazy)


