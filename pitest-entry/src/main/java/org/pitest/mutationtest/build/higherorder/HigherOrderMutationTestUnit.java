package org.pitest.mutationtest.build.higherorder;

import org.pitest.mutationtest.MutationMetaData;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationDetails;
import org.pitest.util.Log;

import java.util.Collection;
import java.util.List;


public class HigherOrderMutationTestUnit implements HigherOrderMutationAnalysisUnit {

    private List<HigherOrderMutationDetails> mutationDetails;

    public HigherOrderMutationTestUnit(List<HigherOrderMutationDetails> higherOrderMutations) {
        this.mutationDetails = higherOrderMutations;
    }

    @Override
    public MutationMetaData call() throws Exception {
        Log.getLogger().info("HigherOrderMutationTestUnit.call()");
        return null;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Collection<HigherOrderMutationDetails> getMutations() {
        return null;
    }
}
