package org.pitest.mutationtest.build;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Group which creates only one group
 */
public class NotGroupingGrouper implements MutationGrouper {

    @Override
    public List<List<MutationDetails>> groupMutations(Collection<ClassName> codeClasses, Collection<MutationDetails> mutations) {
        List<MutationDetails> singleton = new ArrayList<MutationDetails>(mutations);
        List<List<MutationDetails>> result = new ArrayList<List<MutationDetails>>(1);
        result.add(singleton);

        return result;
    }
}
