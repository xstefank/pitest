package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface HigherOrderMutationDetails {

    List<MutationDetails> getDetailsList();

    int getSize();

    Collection<MutationIdentifier> getId();

    Mutant createMutant(Mutater mutater);

}
