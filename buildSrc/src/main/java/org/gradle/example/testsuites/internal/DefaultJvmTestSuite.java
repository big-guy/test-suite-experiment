package org.gradle.example.testsuites.internal;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.example.testsuites.ComponentDependencies;
import org.gradle.example.testsuites.JvmTestSuite;
import org.gradle.example.testsuites.JvmTestSuiteTarget;
import org.gradle.internal.Cast;
import org.gradle.language.internal.DefaultBinaryCollection;

import javax.inject.Inject;

public abstract class DefaultJvmTestSuite implements JvmTestSuite {
    private final ComponentDependencies dependencies;
    private final DefaultBinaryCollection<JvmTestSuiteTarget> targets;
    private final SourceSet sourceSet;

    @Inject
    public DefaultJvmTestSuite(ObjectFactory objectFactory, SourceSetContainer sourceSets, ConfigurationContainer configurations) {
        this.sourceSet = sourceSets.create(getName());
        getSources().from(sourceSet.getAllJava());
        Configuration compileOnly = configurations.getByName(sourceSet.getCompileOnlyConfigurationName());
        Configuration implementation = configurations.getByName(sourceSet.getImplementationConfigurationName());
        Configuration runtimeOnly = configurations.getByName(sourceSet.getRuntimeOnlyConfigurationName());
        this.dependencies = objectFactory.newInstance(DefaultComponentDependencies.class, implementation, compileOnly, runtimeOnly);
        this.targets = Cast.uncheckedCast(objectFactory.newInstance(DefaultBinaryCollection.class, JvmTestSuiteTarget.class));
    }

    @Inject
    abstract ObjectFactory getObjectFactory();

    public void sources(Action<? super ConfigurableFileCollection> configuration) {
        configuration.execute(getSources());
    }

    public ComponentDependencies getDependencies() {
        return dependencies;
    }
    public void dependencies(Action<ComponentDependencies> configuration) {
        configuration.execute(dependencies);
    }

    public DefaultBinaryCollection<JvmTestSuiteTarget> getTargets() {
        return targets;
    }

    public void addTestTarget() {
        DefaultJvmTestSuiteTarget defaultJvmTestSuiteTarget = getObjectFactory().newInstance(DefaultJvmTestSuiteTarget.class);
        defaultJvmTestSuiteTarget.getTestClasses().from(sourceSet.getOutput().getClassesDirs());
        defaultJvmTestSuiteTarget.getRuntimeClasspath().from(sourceSet.getRuntimeClasspath());
        targets.add(defaultJvmTestSuiteTarget);
    }
}
