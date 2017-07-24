package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.MutationDetails;

import java.util.Collection;

/**
 *
 */
public class Last2FirstMutationStrategy implements HigherOrderMutationStrategy {

    @Override
    public Collection<HigherOrderMutationDetails> processMutants(Collection<MutationDetails> firstOrderMutants) {
        //TODO return null for now
        return null;
    }
}
