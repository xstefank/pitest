package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.MutationDetails;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HigherOrderMutationDetails {

    private List<MutationDetails> detailsList;
    private int size;

    public HigherOrderMutationDetails(List<MutationDetails> detailsList, int size) {
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
}
