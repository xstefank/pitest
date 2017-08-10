package org.pitest.mutationtest.engine.higherorder;

import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationEngine;

import java.util.List;

/**
 * Transforms a list of {@link MutationDetails}
 * into a list of {@link AbstractHigherOrderMutationDetails} by specific criteria
 */
public interface HigherOrderMutationStrategy {

    /**
     * Processes first order mutants to create a list of higher order mutants
     * @param firstOrderMutants FOM list or null
     * @return list of {@link HigherOrderMutationDetails} or null
     */
    List<HigherOrderMutationDetails> processMutations(List<MutationDetails> firstOrderMutants, MutationEngine mutationEngine);

}
