package org.pitest.mutationtest.engine.higherorder;

import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Last2FirstMutationStrategy implements HigherOrderMutationStrategy {

    @Override
    public List<HigherOrderMutationDetails> processMutations(List<MutationDetails> mutationDetails, MutationEngine mutationEngine) {
        LinkedList<MutationDetails> firstOrderMutations = new LinkedList<MutationDetails>(mutationDetails);

        List<HigherOrderMutationDetails> secondOrderMutations = new ArrayList<HigherOrderMutationDetails>();

        while (firstOrderMutations.size() > 1) {
            MutationDetails fom1 = firstOrderMutations.getLast();
            firstOrderMutations.removeLast();
            MutationDetails fom2 = firstOrderMutations.getFirst();

            if (firstOrderMutations.size() != 2) {
                firstOrderMutations.removeFirst();
            }

            AbstractHigherOrderMutationDetails higherOrderMutation = new Last2FirstMutationDetails(
                    Arrays.asList(fom1, fom2), mutationEngine);
            secondOrderMutations.add(higherOrderMutation);
        }

        return secondOrderMutations;
    }
}
