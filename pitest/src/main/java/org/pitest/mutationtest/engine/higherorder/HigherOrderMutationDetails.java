package org.pitest.mutationtest.engine.higherorder;

import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface HigherOrderMutationDetails {

    List<MutationDetails> getDetailsList();

    int getSize();

    HigherOrderMutationIdentifier getId();

    Mutant createMutant(Mutater mutater);

    Collection<MethodName> getMethods();

}
