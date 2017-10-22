package org.pitest.mutationtest.engine.hom.strategy;

import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.hom.HigherOrderMutant;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategyProcessor;

import java.util.Collection;

public class Last2FirstMutationProcessor implements HigherOrderMutationStrategyProcessor {


    @Override
    public Collection<HigherOrderMutant> processMutants(Collection<Mutant> firstOrderMutants) {
        //TODO return null for now
        return null;
    }
}
