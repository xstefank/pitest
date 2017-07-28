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
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.Map.Entry;

public class MutationStatusMap extends AbstractMutationStatusMap<MutationDetails, MutationResult> {

  @Override
  protected F<Entry<MutationDetails, MutationStatusTestPair>, MutationResult> detailsToMutationResults() {
    return new F<Entry<MutationDetails, MutationStatusTestPair>, MutationResult>() {

      @Override
      public MutationResult apply(
          final Entry<MutationDetails, MutationStatusTestPair> a) {
        return new MutationResult(a.getKey(), a.getValue());
      }

    };
  }

  public void markUncoveredMutations() {
    setStatusForMutations(
        FCollection.filter(allMutations(), hasNoCoverage()),
        DetectionStatus.NO_COVERAGE);

  }

  private static F<MutationDetails, Boolean> hasNoCoverage() {
    return new F<MutationDetails, Boolean>() {

      @Override
      public Boolean apply(final MutationDetails a) {
        return a.getTestsInOrder().isEmpty();
      }

    };
  }

}
