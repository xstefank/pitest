package org.pitest.mutationtest.engine.hom;

import org.pitest.classinfo.ByteArraySource;
import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationEngine;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.List;

/**
 *
 */
public class Last2FirstMutationDetails extends AbstractHigherOrderMutationDetails {

    private static final int SIZE = 2;

    private MutationEngine mutationEngine;

    public Last2FirstMutationDetails(List<MutationDetails> detailsList, MutationEngine mutationEngine) {
        super(detailsList, SIZE);
        this.mutationEngine = mutationEngine;
    }

    @Override
    public Mutant createMutant(Mutater mutater) {

        MutationIdentifier identifier = getFirst().getId();
        Mutant mutant1 = mutater.getMutation(identifier);

        Mutater mutant1Mutater = mutationEngine.createMutator(new ByteArraySource(mutant1.getBytes()));
        Mutant newMutant = mutant1Mutater.getMutation(getSecond().getId());

        return newMutant;
    }

    private MutationDetails getFirst() {
        return getDetailsList().get(0);
    }

    private MutationDetails getSecond() {
        return getDetailsList().get(1);
    }
}
