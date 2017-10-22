package org.pitest.mutationtest.engine.hom.strategy;

import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategyProcessor;

/**
 * Definition of available higher order mutation strategies
 */
public enum MutationStrategy {

    LAST_2_FIRST("L2F", new Last2FirstMutationProcessor())

    ;

    private String id;
    private HigherOrderMutationStrategyProcessor processor;

    MutationStrategy(String id, HigherOrderMutationStrategyProcessor processor) {
        this.id = id;
        this.processor = processor;
    }

    public String getId() {
        return id;
    }

    public HigherOrderMutationStrategyProcessor getProcessor() {
        return processor;
    }
}
