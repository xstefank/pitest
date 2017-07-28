/*
 * Copyright 2011 Henry Coles
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
package org.pitest.mutationtest;

import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.predicate.Predicate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.pitest.functional.prelude.Prelude.putToMap;

public abstract class AbstractMutationStatusMap<T> {

  private final Map<T, MutationStatusTestPair> mutationMap = new HashMap<T, MutationStatusTestPair>();

  public void setStatusForMutation(final T mutation,
      final DetectionStatus status) {
    this.setStatusForMutations(Collections.singleton(mutation), status);
  }

  public void setStatusForMutation(final T mutation,
      final MutationStatusTestPair status) {
    this.mutationMap.put(mutation, status);
  }

  public void setStatusForMutations(
      final Collection<T> mutations, final DetectionStatus status) {
    FCollection.forEach(mutations,
        putToMap(this.mutationMap, new MutationStatusTestPair(0, status)));
  }

  public List<MutationResult> createMutationResults() {
    return FCollection.map(this.mutationMap.entrySet(),
        detailsToMutationResults());

  }

  public boolean hasUnrunMutations() {
    return !getUnrunMutations().isEmpty();
  }

  public Collection<T> getUnrunMutations() {
    return FCollection.filter(this.mutationMap.entrySet(),
        hasStatus(DetectionStatus.NOT_STARTED)).map(toMutationDetails());
  }

  public Collection<T> getUnfinishedRuns() {
    return FCollection.filter(this.mutationMap.entrySet(),
        hasStatus(DetectionStatus.STARTED)).map(toMutationDetails());
  }

  public Set<T> allMutations() {
    return this.mutationMap.keySet();
  }

  protected abstract F<Entry<T, MutationStatusTestPair>, MutationResult> detailsToMutationResults();

  private F<Entry<T, MutationStatusTestPair>, T> toMutationDetails() {
    return new F<Entry<T, MutationStatusTestPair>, T>() {

      @Override
      public T apply(
          final Entry<T, MutationStatusTestPair> a) {
        return a.getKey();
      }

    };
  }

  private Predicate<Entry<T, MutationStatusTestPair>> hasStatus(
      final DetectionStatus status) {
    return new Predicate<Entry<T, MutationStatusTestPair>>() {

      @Override
      public Boolean apply(
          final Entry<T, MutationStatusTestPair> a) {
        return a.getValue().getStatus().equals(status);
      }

    };
  }

}
