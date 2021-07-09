package org.gradle.example.testsuites;

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;
import org.gradle.example.testsuites.internal.DefaultJvmTestSuite;
import org.gradle.example.testsuites.internal.DefaultJvmTestSuiteTarget;

public class TestSuitePlugin  implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getPluginManager().apply("org.gradle.java-base");
        JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
        ExtensiblePolymorphicDomainObjectContainer<JvmTestSuite> testSuites =
                project.getObjects().polymorphicDomainObjectContainer(JvmTestSuite.class);
        testSuites.registerFactory(JvmTestSuite.class, name -> project.getObjects().newInstance(DefaultJvmTestSuite.class, name, java.getSourceSets(), project.getConfigurations()));
        project.getExtensions().add("testSuites", testSuites);

        testSuites.all(testSuite -> {
            testSuite.getTargets().whenElementKnown(DefaultJvmTestSuiteTarget.class, testSuiteTarget -> {

                TaskProvider<Test> testTask = project.getTasks().register(testSuite.getName() + testSuiteTarget.getName(), Test.class, task -> {
                    task.setDescription("Description set by plugin");
                    task.setGroup("verification");
                    task.setTestClassesDirs(testSuiteTarget.getTestClasses());
                    task.setClasspath(testSuiteTarget.getRuntimeClasspath());
                });
                testSuiteTarget.getRunTask().convention(testTask);
            });
        });

        project.afterEvaluate(p -> {
            testSuites.withType(DefaultJvmTestSuite.class).configureEach(testSuite -> {
                testSuite.addTestTarget("java8");
                testSuite.getTargets().realizeNow();
            });
        });
    }
}