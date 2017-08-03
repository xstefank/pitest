package org.pitest.mutationtest.build.factory;

import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.TimeoutLengthStrategy;
import org.pitest.testapi.Configuration;

import java.io.File;

public class WorkerFactoryArgs {

    private final String classPath;
    private final File baseDir;
    private final Configuration pitConfig;
    private final TimeoutLengthStrategy timeoutStrategy;
    private final boolean verbose;
    private final MutationConfig config;

    public WorkerFactoryArgs(String classPath,
                             File baseDir,
                             Configuration pitConfig,
                             TimeoutLengthStrategy timeoutStrategy,
                             boolean verbose,
                             MutationConfig config) {
        this.classPath = classPath;
        this.baseDir = baseDir;
        this.pitConfig = pitConfig;
        this.timeoutStrategy = timeoutStrategy;
        this.verbose = verbose;
        this.config = config;
    }

    public String getClassPath() {
        return classPath;
    }

    public File getBaseDir() {
        return baseDir;
    }

    public Configuration getPitConfig() {
        return pitConfig;
    }

    public TimeoutLengthStrategy getTimeoutStrategy() {
        return timeoutStrategy;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public MutationConfig getConfig() {
        return config;
    }

    public static class WorkerFactoryArgsBuilder {

        String classPath;
        File baseDir;
        Configuration pitConfig;
        TimeoutLengthStrategy timeoutStrategy;
        boolean verbose;
        MutationConfig config;

        public WorkerFactoryArgsBuilder classPath(String classPath) {
            this.classPath = classPath;
            return this;
        }

        public WorkerFactoryArgsBuilder baseDir(File baseDir) {
            this.baseDir = baseDir;
            return this;
        }

        public WorkerFactoryArgsBuilder pitConfig(Configuration pitConfig) {
            this.pitConfig = pitConfig;
            return this;
        }

        public WorkerFactoryArgsBuilder timeoutStrategy(TimeoutLengthStrategy timeoutStrategy) {
            this.timeoutStrategy = timeoutStrategy;
            return this;
        }

        public WorkerFactoryArgsBuilder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public WorkerFactoryArgsBuilder config(MutationConfig config) {
            this.config = config;
            return this;
        }

        public WorkerFactoryArgs build() {
            return new WorkerFactoryArgs(
                    classPath,
                    baseDir,
                    pitConfig,
                    timeoutStrategy,
                    verbose,
                    config
            );
        }

    }

}
