package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.Mutant;

import java.util.Collection;

/**
 *
 */
public class Last2FirstMutationStrategy implements HigherOrderMutationStrategy {

    @Override
    public Collection<HigherOrderMutant> processMutants(Collection<Mutant> firstOrderMutants) {
        //TODO return null for now
        return null;
    }
}
