package org.pitest.mutationtest.engine.hom;

/**
 * Definition of available second order mutation strategies
 */
public enum SecondOrderMutationStrategies {

    LAST_TO_FIRST("last2first", new Last2FirstMutationStrategy());

    private String id;
    private HigherOrderMutationStrategy strategy;


    SecondOrderMutationStrategies(String id, HigherOrderMutationStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
    }

    public String getId() {
        return id;
    }

    public HigherOrderMutationStrategy getStrategy() {
        return strategy;
    }
}
