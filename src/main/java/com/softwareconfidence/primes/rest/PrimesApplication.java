package com.softwareconfidence.primes.rest;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.RestApplication;
import com.softwareconfidence.primes.domain.DomainModule;

import static com.googlecode.utterlyidle.ApplicationBuilder.application;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class PrimesApplication extends RestApplication {

    public PrimesApplication(BasePath basePath) {
        super(basePath);
    }

    public static Application setupApplication() {
        return application(PrimesApplication.class)
                .add(annotatedClass(PrimesResource.class))
                .add(new DomainModule())
                .build();
    }
}
