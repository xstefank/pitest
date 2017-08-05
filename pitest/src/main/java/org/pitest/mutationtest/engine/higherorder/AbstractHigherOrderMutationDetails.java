package org.pitest.mutationtest.engine.higherorder;

import org.pitest.functional.F;
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

    protected List<MutationDetails> detailsList;
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

    public HigherOrderMutationIdentifier getId() {
        return new HigherOrderMutationIdentifier(FCollection.map(detailsList, detailsToId()));
    }

    private F<MutationDetails, MutationIdentifier> detailsToId() {

        return new F<MutationDetails, MutationIdentifier>() {
            @Override
            public MutationIdentifier apply(MutationDetails details) {
                return details.getId();
            }
        };

    }


}
