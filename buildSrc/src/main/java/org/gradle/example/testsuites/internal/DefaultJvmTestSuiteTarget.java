package org.gradle.example.testsuites.internal;

import org.gradle.api.Action;
import org.gradle.api.Transformer;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.testing.Test;
import org.gradle.example.testsuites.JvmTestSuiteTarget;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.function.BiFunction;

public abstract class DefaultJvmTestSuiteTarget implements JvmTestSuiteTarget {
    private final String name;

    @Inject public DefaultJvmTestSuiteTarget(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public TaskProvider<Test> getTestTask() {
        return new TaskProvider<Test>() {
            @Override
            public void configure(Action<? super Test> action) {
                action.execute(getRunTask().get());
            }

            @Override
            public String getName() {
                return getRunTask().get().getName();
            }

            @Override
            public Test get() {
                return getRunTask().get();
            }

            @Nullable
            @Override
            public Test getOrNull() {
                return getRunTask().getOrNull();
            }

            @Override
            public Test getOrElse(Test defaultValue) {
                return getRunTask().getOrElse(defaultValue);
            }

            @Override
            public <S> Provider<S> map(Transformer<? extends S, ? super Test> transformer) {
                return getRunTask().map(transformer);
            }

            @Override
            public <S> Provider<S> flatMap(Transformer<? extends Provider<? extends S>, ? super Test> transformer) {
                return getRunTask().flatMap(transformer);
            }

            @Override
            public boolean isPresent() {
                return getRunTask().isPresent();
            }

            @Override
            public Provider<Test> orElse(Test value) {
                return getRunTask().orElse(value);
            }

            @Override
            public Provider<Test> orElse(Provider<? extends Test> provider) {
                return getRunTask().orElse(provider);
            }

            @Override
            public Provider<Test> forUseAtConfigurationTime() {
                return getRunTask().forUseAtConfigurationTime();
            }

            @Override
            public <B, R> Provider<R> zip(Provider<B> provider, BiFunction<Test, B, R> biFunction) {
                return getRunTask().zip(provider, biFunction);
            }
        };
    }
}
