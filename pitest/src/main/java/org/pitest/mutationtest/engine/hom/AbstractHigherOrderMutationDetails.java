package org.pitest.mutationtest.engine.hom;

import org.pitest.functional.F2;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public abstract class AbstractHigherOrderMutationDetails implements HigherOrderMutationDetails {

    private List<MutationDetails> detailsList;
    private int size;

    public AbstractHigherOrderMutationDetails(List<MutationDetails> detailsList, int size) {
        assert detailsList.size() == size;
        this.size = size;
        this.detailsList = new ArrayList<MutationDetails>(detailsList);
    }

    public List<MutationDetails> getDetailsList() {
        return detailsList;
    }

    public int getSize() {
        return size;
    }

    public Collection<MutationIdentifier> getId() {
        return FCollection.fold(detailsToId(), new ArrayList<MutationIdentifier>(), detailsList);
    }

    private static F2<Collection<MutationIdentifier>, MutationDetails, Collection<MutationIdentifier>> detailsToId() {

        return new F2<Collection<MutationIdentifier>, MutationDetails, Collection<MutationIdentifier>>() {
            @Override
            public Collection<MutationIdentifier> apply(Collection<MutationIdentifier> mutationIdentifiers, MutationDetails details) {
                mutationIdentifiers.add(details.getId());
                return mutationIdentifiers;
            }
        };

    }
}
