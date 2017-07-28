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

import org.pitest.functional.Option;

public abstract class AbstractMutationResult<T> {

  private final T                      details;
  private final MutationStatusTestPair status;

  public AbstractMutationResult(final T md,
                                final MutationStatusTestPair status) {
    this.details = md;
    this.status = status;
  }

  public T getDetails() {
    return this.details;
  }

  public Option<String> getKillingTest() {
    return this.status.getKillingTest();
  }

  public DetectionStatus getStatus() {
    return this.status.getStatus();
  }

  public int getNumberOfTestsRun() {
    return this.status.getNumberOfTestsRun();
  }

  public MutationStatusTestPair getStatusTestPair() {
    return this.status;
  }

  public String getStatusDescription() {
    return getStatus().name();
  }

  public String getKillingTestDescription() {
    return getKillingTest().getOrElse("none");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractMutationResult)) return false;

    AbstractMutationResult<?> that = (AbstractMutationResult<?>) o;

    if (!getDetails().equals(that.getDetails())) return false;
    return getStatus().equals(that.getStatus());
  }

  @Override
  public int hashCode() {
    int result = getDetails().hashCode();
    result = 31 * result + getStatus().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "MutationResult [details=" + this.getDetails() + ", status="
            + this.getStatus() + "]";
  }


}