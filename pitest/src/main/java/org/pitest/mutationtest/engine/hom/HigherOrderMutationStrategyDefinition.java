package org.pitest.mutationtest.engine.hom;

/**
 * Definition of available higher order mutation strategies
 */
public enum HigherOrderMutationStrategyDefinition {

    LAST_TO_FIRST("last2first", new Last2FirstMutationStrategy());

    private String id;
    private HigherOrderMutationStrategy strategy;


    HigherOrderMutationStrategyDefinition(String id, HigherOrderMutationStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
    }

    public static HigherOrderMutationStrategyDefinition findById(String id) {
        for (HigherOrderMutationStrategyDefinition strategyDefinition : values()) {
            if (strategyDefinition.getId().equals(id)) {
                return strategyDefinition;
            }
        }

        return null;
    }

    public String getId() {
        return id;
    }

    public HigherOrderMutationStrategy getStrategy() {
        return strategy;
    }
}
