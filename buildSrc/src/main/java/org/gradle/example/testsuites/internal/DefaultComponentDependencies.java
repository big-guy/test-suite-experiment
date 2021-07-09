package org.gradle.example.testsuites.internal;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.example.testsuites.ComponentDependencies;

import javax.inject.Inject;

public class DefaultComponentDependencies implements ComponentDependencies {
    private final Configuration implementation;
    private final Configuration compileOnly;
    private final Configuration runtimeOnly;

    @Inject
    public DefaultComponentDependencies(Configuration implementation, Configuration compileOnly, Configuration runtimeOnly) {
        this.implementation = implementation;
        this.compileOnly = compileOnly;
        this.runtimeOnly = runtimeOnly;
    }

    @Inject
    protected DependencyHandler getDependencyHandler() {
        throw new UnsupportedOperationException();
    }

    private void addToConfiguration(Configuration bucket, Object dependencyNotation) {
        bucket.getDependencies().add(getDependencyHandler().create(dependencyNotation));
    }

    private void addToConfiguration(Configuration bucket, Object dependencyNotation, Action<? super ExternalModuleDependency> configuration) {
        ExternalModuleDependency dependency = (ExternalModuleDependency) getDependencyHandler().create(dependencyNotation);
        configuration.execute(dependency);
        bucket.getDependencies().add(dependency);
    }

    @Override
    public void implementation(Object dependencyNotation) {
        addToConfiguration(implementation, dependencyNotation);
    }

    @Override
    public void implementation(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration) {
        addToConfiguration(implementation, dependencyNotation, configuration);
    }

    @Override
    public void runtimeOnly(Object dependencyNotation) {
        addToConfiguration(runtimeOnly, dependencyNotation);
    }

    @Override
    public void runtimeOnly(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration) {
        addToConfiguration(runtimeOnly, dependencyNotation, configuration);
    }

    @Override
    public void compileOnly(Object dependencyNotation) {
        addToConfiguration(compileOnly, dependencyNotation);
    }

    @Override
    public void compileOnly(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration) {
        addToConfiguration(compileOnly, dependencyNotation, configuration);
    }
}
