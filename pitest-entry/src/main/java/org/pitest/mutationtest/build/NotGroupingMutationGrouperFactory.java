package org.pitest.mutationtest.build;

import org.pitest.classpath.CodeSource;

import java.util.Properties;

/**
 *
 */
public class NotGroupingMutationGrouperFactory implements MutationGrouperFactory {
    @Override
    public String description() {
        return "Not grouping mutation factory";
    }

    @Override
    public MutationGrouper makeFactory(Properties props, CodeSource codeSource, int numberOfThreads, int unitSize) {
        return new NotGroupingGrouper();
    }
}
