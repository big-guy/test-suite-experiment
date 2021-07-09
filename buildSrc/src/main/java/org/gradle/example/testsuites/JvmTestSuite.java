package org.gradle.example.testsuites;

import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.component.SoftwareComponent;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.SourceSet;
import org.gradle.language.BinaryCollection;

public interface JvmTestSuite extends Named, SoftwareComponent {
    SourceSet getSources();
    void sources(Action<? super SourceSet> configuration);

    ComponentDependencies getDependencies();
    void dependencies(Action<ComponentDependencies> configuration);

    BinaryCollection<? extends JvmTestSuiteTarget> getTargets();
    void targets(Action<BinaryCollection<? extends JvmTestSuiteTarget>> configuration);
}
