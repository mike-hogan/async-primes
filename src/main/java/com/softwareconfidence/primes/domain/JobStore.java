package com.softwareconfidence.primes.domain;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.collections.PersistentMap;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.googlecode.totallylazy.collections.PersistentMap.constructors.map;
import static java.util.UUID.randomUUID;

public class JobStore {
    private PersistentMap<UUID, Future<List<Number>>> jobs = map();

    public UUID put(Future<List<Number>> future) {
        UUID id = randomUUID();
        jobs = jobs.insert(id, future);
        return id;
    }

    public Option<List<Number>> get(UUID id) {
        return jobs.lookup(id).filter(Future::isDone).map(Future::get);
    }
}
