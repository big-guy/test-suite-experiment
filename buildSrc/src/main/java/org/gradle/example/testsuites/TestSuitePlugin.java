package org.gradle.example.testsuites;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;
import org.gradle.example.testsuites.internal.DefaultJvmTestSuite;

public class TestSuitePlugin  implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("org.gradle.java-base");
        ExtensiblePolymorphicDomainObjectContainer<JvmTestSuite> testSuites =
                project.getObjects().polymorphicDomainObjectContainer(JvmTestSuite.class);
        testSuites.registerBinding(JvmTestSuite.class, DefaultJvmTestSuite.class);
        project.getExtensions().add("testSuites", testSuites);

        testSuites.configureEach(testSuite -> {
            testSuite.getTargets().whenElementKnown(JvmTestSuiteTarget.class, testSuiteTarget -> {
                TaskProvider<Test> testTask = project.getTasks().register(testSuiteTarget.getName(), Test.class, task -> {
                    task.setDescription("Description set by plugin");
                    task.setGroup("verification");
                    task.setTestClassesDirs(testSuiteTarget.getTestClasses());
                    task.setClasspath(testSuiteTarget.getRuntimeClasspath());
                });
                testSuiteTarget.getTestTask().convention(testTask);
            });
        });

        project.afterEvaluate(p -> {
            testSuites.withType(DefaultJvmTestSuite.class).configureEach(testSuite -> {
                testSuite.addTestTarget();
                testSuite.getTargets().realizeNow();
            });
        });
    }
}