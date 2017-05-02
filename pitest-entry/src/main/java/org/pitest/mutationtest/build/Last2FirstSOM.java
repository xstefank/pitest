package org.pitest.mutationtest.build;

import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:xstefank122@gmail.com">Martin Stefanko</a>
 */
public class Last2FirstSOM {

    private final MutationConfig mutationConfig;
    private final ClassByteArraySource bytes;
    private List<MutationDetails> mutations;

    public Last2FirstSOM(List<MutationDetails> mutations, MutationConfig mutationConfig, ClassByteArraySource bytes) {
        this.mutations = mutations;
        this.mutationConfig = mutationConfig;
        this.bytes = bytes;
    }

    public List<MutationDetails> createSecondOrderMutations() {
        LinkedList<MutationDetails> firstOrderMutations = new LinkedList<MutationDetails>(mutations);
        List<MutationDetails> secondOrderMutations = new ArrayList<MutationDetails>();

        while (firstOrderMutations.size() > 1) {
            MutationDetails fom1 = firstOrderMutations.getLast();
            firstOrderMutations.removeLast();

            MutationDetails fom2 = firstOrderMutations.getFirst();
            if (firstOrderMutations.size() != 2) {
                firstOrderMutations.removeFirst();
            }

            MutationIdentifier operator = fom2.getId();
//            MutationDetails newMutation = mutationConfig.createMutator(bytes)
//                    .findMutation(operator, bytes.getBytes(fom1.getClassName().asJavaName()).getOrElse(null));


        }

        return mutations;
    }

}
