package org.gradle.example.testsuites;

import org.gradle.api.Named;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.testing.Test;

public interface JvmTestSuiteTarget extends Named, SoftwareComponent {
    ConfigurableFileCollection getTestClasses();
    ConfigurableFileCollection getRuntimeClasspath();

    Property<Test> getTestTask();
}
