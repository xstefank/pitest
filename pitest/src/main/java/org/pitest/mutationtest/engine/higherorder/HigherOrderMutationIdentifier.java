/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.engine.higherorder;

import org.pitest.classinfo.ClassName;
import org.pitest.functional.F2;
import org.pitest.functional.FCollection;
import org.pitest.functional.SideEffect1;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Uniquely identifies a mutation
 */
public final class HigherOrderMutationIdentifier implements Comparable<HigherOrderMutationIdentifier> {

    private final List<MutationIdentifier> identifiers;
    private final List<Location> locations;
    private final List<Integer> indexes;
    private final String mutators;

    public HigherOrderMutationIdentifier(final List<MutationIdentifier> identifiers) {
        this.identifiers = identifiers;
        this.locations = computeLocations();
        this.indexes = computeIndexes();
        this.mutators = computeMutators();
    }

    /**
     * Returns the locations of the mutations
     *
     * @return the locations of the mutation
     */
    public List<Location> getLocations() {
        return this.locations;
    }

    /**
     * Returns the name of the mutator that created this mutation
     *
     * @return the mutator name
     */
    public String getMutators() {
        return this.mutators;
    }

    public List<Integer> getIndexes() {
        return this.indexes;
    }

    /**
     * Returns the index to the first instruction on which this mutation occurs.
     * This index is specific to how ASM represents the bytecode.
     *
     * @return the zero based index to the instruction
     */
    public int getFirstIndex() {
        return this.indexes.iterator().next();
    }

    @Override
    public String toString() {
        return "MutationIdentifier [locations=" + this.locations + ", indexes="
                + this.indexes + ", mutators=" + this.indexes + "]";
    }

    /**
     * Returns true if this mutation has a matching identifier
     *
     * @param id the MutationIdentifier to match
     * @return true if the MutationIdentifier matches otherwise false
     */
    public boolean matches(final HigherOrderMutationIdentifier id) {
        return this.locations.equals(id.locations) && this.mutators.equals(id.mutators)
                && this.indexes.contains(id.getFirstIndex());
    }

    /**
     * Returns the class in which this mutation is located
     *
     * @return class in which mutation is located
     */
    public List<ClassName> getClassName() {
        return FCollection.fold(locationToClassName(), new ArrayList<ClassName>(), this.locations);
    }

    private F2<List<ClassName>, Location, List<ClassName>> locationToClassName() {
        return new F2<List<ClassName>, Location, List<ClassName>>() {

            @Override
            public List<ClassName> apply(List<ClassName> classNames, Location location) {
                classNames.add(location.getClassName());
                return classNames;
            }

        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HigherOrderMutationIdentifier)) {
            return false;
        }

        HigherOrderMutationIdentifier that = (HigherOrderMutationIdentifier) o;

        return identifiers != null ? identifiers.equals(that.identifiers) : that.identifiers == null;
    }

    @Override
    public int hashCode() {
        return identifiers != null ? identifiers.hashCode() : 0;
    }

    @Override
    public int compareTo(HigherOrderMutationIdentifier that) {
        List<MutationIdentifier> thisIdentifiers = new ArrayList<MutationIdentifier>(this.identifiers);
        List<MutationIdentifier> thatIdentifiers = new ArrayList<MutationIdentifier>(that.identifiers);

        if (thisIdentifiers.size() != thatIdentifiers.size()) {
            return thisIdentifiers.size() - thatIdentifiers.size();
        }

        Collections.sort(thisIdentifiers);
        Collections.sort(thatIdentifiers);

        for (int i = 0; i < thisIdentifiers.size(); i++) {
            int compare = thisIdentifiers.get(i).compareTo(thatIdentifiers.get(i));
            if (compare != 0) {
                return compare;
            }
        }

        return 0;
    }

    private List<Location> computeLocations() {
        return FCollection.fold(identifierToLocation(), new ArrayList<Location>(), identifiers);
    }

    private F2<List<Location>, MutationIdentifier, List<Location>> identifierToLocation() {

        return new F2<List<Location>, MutationIdentifier, List<Location>>() {

            @Override
            public List<Location> apply(List<Location> locations, MutationIdentifier mutationIdentifier) {
                locations.add(mutationIdentifier.getLocation());
                return locations;
            }

        };
    }

    private List<Integer> computeIndexes() {
        return FCollection.fold(identifierToIndexes(), new ArrayList<Integer>(), identifiers);
    }

    private F2<List<Integer>, MutationIdentifier, List<Integer>> identifierToIndexes() {
        return new F2<List<Integer>, MutationIdentifier, List<Integer>>() {

            @Override
            public List<Integer> apply(List<Integer> indexes, MutationIdentifier mutationIdentifier) {
                indexes.addAll(mutationIdentifier.getIndexes());
                return indexes;
            }

        };
    }

    private String computeMutators() {
        final StringBuffer mutatorsSb = new StringBuffer("[ ");
        FCollection.forEach(identifiers, new SideEffect1<MutationIdentifier>() {
            @Override
            public void apply(MutationIdentifier mutationIdentifier) {
                mutatorsSb.append(String.format("%s, ", mutationIdentifier.getMutator()));
            }
        });

        mutatorsSb.setLength(mutatorsSb.length() - 2);
        mutatorsSb.append(" ]");
        return mutatorsSb.toString();
    }
}
