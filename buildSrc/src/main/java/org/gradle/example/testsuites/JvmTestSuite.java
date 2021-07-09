package org.gradle.example.testsuites;

import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.language.BinaryCollection;

public interface JvmTestSuite extends Named, SoftwareComponent {
    ConfigurableFileCollection getSources();
    void sources(Action<? super ConfigurableFileCollection> configuration);

    ComponentDependencies getDependencies();
    void dependencies(Action<ComponentDependencies> configuration);

    BinaryCollection<? extends JvmTestSuiteTarget> getTargets();
}
