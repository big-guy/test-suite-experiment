package org.gradle.example.testsuites;

import org.gradle.api.Action;
import org.gradle.api.artifacts.ExternalModuleDependency;

public interface ComponentDependencies {
    void implementation(Object dependencyNotation);
    void implementation(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration);

    void runtimeOnly(Object dependencyNotation);
    void runtimeOnly(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration);
    
    void compileOnly(Object dependencyNotation);
    void compileOnly(Object dependencyNotation, Action<? super ExternalModuleDependency> configuration);
}
