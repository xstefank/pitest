package org.pitest.mutationtest.engine.hom;

import org.pitest.mutationtest.engine.Mutant;

import java.util.Collection;

/**
 * A colllection of {@link org.pitest.mutationtest.engine.Mutant} of fixed size
 */
public class HigherOrderMutant {

    private final Collection<Mutant> fomMutants;
    private final byte[] bytes;
    private final int size;

    public HigherOrderMutant(Collection<Mutant> fomMutants, byte[] bytes, int size) {
        this.fomMutants = fomMutants;
        this.bytes = bytes;
        this.size = size;
    }

    public Collection<Mutant> getFomMutants() {
        return fomMutants;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getSize() {
        return size;
    }
}
