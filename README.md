# What is this?
This is a submission to a coding challenge that required a REST service written in Java that returns prime numbers.  There was a stipulation to use Maven.

# Running
*mvn compile test exec:java*

*curl http://localhost:8080/primes?upto=10*

# Commentary
This is an alternative implementation to [this simpler version](https://github.com/mike-hogan/idle-primes)

This version submit the calculation of the prime array to an ExecutorService and stores a Future against a UUID, then return HTTP 202 with a
Location header that contained a URL to query the progress of that job.

Apart from Maven as the build tool, I was free to choose frameworks.
I went with [Utterlyidle](https://github.com/bodar/utterlyidle) and [Totallylazy](https://github.com/bodar/totallylazy)


