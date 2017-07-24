package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.MutationDetails;

import java.util.Collection;

/**
 * Transforms a collection of {@link MutationDetails}
 * into a collection of {@link HigherOrderMutationDetails} by specific criteria
 */
public interface HigherOrderMutationStrategy {

    /**
     * Processes first order mutants to create a collection of higher order mutants
     * @param firstOrderMutants FOM collection or null
     * @return collection of {@link HigherOrderMutant} or null
     */
    Collection<HigherOrderMutationDetails> processMutants(Collection<MutationDetails> firstOrderMutants);

}
