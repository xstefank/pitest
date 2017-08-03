package org.pitest.mutationtest.engine.higherorder;

import org.pitest.mutationtest.engine.Mutant;

import java.util.Collection;

/**
 * A collection of {@link org.pitest.mutationtest.engine.Mutant} of fixed size
 */
public class HigherOrderMutant {

    private final Collection<Mutant> details;
    private final byte[] bytes;
    private final int size;

    public HigherOrderMutant(Collection<Mutant> details, byte[] bytes, int size) {
        assert details.size() == size;

        this.details = details;
        this.bytes = bytes;
        this.size = size;
    }

    public Collection<Mutant> getDetails() {
        return details;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getSize() {
        return size;
    }
}
