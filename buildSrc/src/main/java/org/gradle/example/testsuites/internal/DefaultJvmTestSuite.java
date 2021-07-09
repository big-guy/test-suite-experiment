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
import org.gradle.language.BinaryCollection;
import org.gradle.language.internal.DefaultBinaryCollection;

import javax.inject.Inject;

public abstract class DefaultJvmTestSuite implements JvmTestSuite {
    private final ComponentDependencies dependencies;
    private final DefaultBinaryCollection<JvmTestSuiteTarget> targets;
    private final SourceSet sourceSet;
    private final String name;

    @Inject
    public DefaultJvmTestSuite(String name, SourceSetContainer sourceSets, ConfigurationContainer configurations) {
        this.name = name;
        this.sourceSet = sourceSets.create(getName());
        Configuration compileOnly = configurations.getByName(sourceSet.getCompileOnlyConfigurationName());
        Configuration implementation = configurations.getByName(sourceSet.getImplementationConfigurationName());
        Configuration runtimeOnly = configurations.getByName(sourceSet.getRuntimeOnlyConfigurationName());
        this.dependencies = getObjectFactory().newInstance(DefaultComponentDependencies.class, implementation, compileOnly, runtimeOnly);
        this.targets = Cast.uncheckedCast(getObjectFactory().newInstance(DefaultBinaryCollection.class, JvmTestSuiteTarget.class));
    }

    @Override
    public String getName() {
        return name;
    }

    @Inject
    public abstract ObjectFactory getObjectFactory();

    public SourceSet getSources() {
        return sourceSet;
    }
    public void sources(Action<? super SourceSet> configuration) {
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
    public void targets(Action<BinaryCollection<? extends JvmTestSuiteTarget>> configuration) {
        configuration.execute(targets);
    }

    public void addTestTarget(String target) {
        DefaultJvmTestSuiteTarget defaultJvmTestSuiteTarget = getObjectFactory().newInstance(DefaultJvmTestSuiteTarget.class, target);
        defaultJvmTestSuiteTarget.getTestClasses().from(sourceSet.getOutput().getClassesDirs());
        defaultJvmTestSuiteTarget.getRuntimeClasspath().from(sourceSet.getRuntimeClasspath());
        targets.add(defaultJvmTestSuiteTarget);
    }
}
