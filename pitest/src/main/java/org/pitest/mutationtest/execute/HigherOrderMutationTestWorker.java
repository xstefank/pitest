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
package org.pitest.mutationtest.execute;

import org.pitest.classinfo.CachingByteArraySource;
import org.pitest.classinfo.ClassName;
import org.pitest.classpath.ClassPath;
import org.pitest.classpath.ClassloaderByteArraySource;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.F3;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationEngine;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationDetails;
import org.pitest.mutationtest.mocksupport.JavassistInterceptor;
import org.pitest.testapi.TestResult;
import org.pitest.testapi.TestUnit;
import org.pitest.testapi.execute.Container;
import org.pitest.testapi.execute.ExitingResultCollector;
import org.pitest.testapi.execute.MultipleTestGroup;
import org.pitest.testapi.execute.Pitest;
import org.pitest.testapi.execute.containers.ConcreteResultCollector;
import org.pitest.testapi.execute.containers.UnContainer;
import org.pitest.util.IsolationUtils;
import org.pitest.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.pitest.util.Unchecked.translateCheckedException;

public class HigherOrderMutationTestWorker {

  private static final Logger                               LOG   = Log
      .getLogger();

  // micro optimise debug logging
  private static final boolean                              DEBUG = LOG
      .isLoggable(Level.FINE);

  private Mutater mutater;
  private ClassLoader                                 loader;
  private final F3<ClassName, ClassLoader, byte[], Boolean> hotswap;
  private MutationEngine engine;

  public HigherOrderMutationTestWorker(
          final F3<ClassName, ClassLoader, byte[], Boolean> hotswap,
          final Mutater mutater, final ClassLoader loader,
          MutationEngine engine) {
    this.loader = loader;
    this.mutater = mutater;
    this.hotswap = hotswap;
    this.engine = engine;
  }

  protected void run(final Collection<HigherOrderMutationDetails> range, final Reporter r,
                     final TimeOutDecoratedTestSource testSource) throws IOException {

    for (final HigherOrderMutationDetails mutation : range) {
      if (DEBUG) {
        LOG.fine("Running higher order mutation " + mutation);
      }
      final long t0 = System.currentTimeMillis();
      processMutation(r, testSource, mutation);
      if (DEBUG) {
        LOG.fine("processed mutation in " + (System.currentTimeMillis() - t0)
            + " ms.");
      }
    }

  }

  private void processMutation(final Reporter r,
                               final TimeOutDecoratedTestSource testSource,
                               final HigherOrderMutationDetails higherOrderMutationDetails) throws IOException {
    Iterator<MutationDetails> detailsIterator = higherOrderMutationDetails.getDetailsList().iterator();
    List<TestInfo> tests = new ArrayList<TestInfo>();
    boolean isViable = true;

    while (detailsIterator.hasNext() && isViable) {
        MutationDetails mutationDetails = detailsIterator.next();
        isViable = processIndividualMutation(r, testSource, mutationDetails);
        addRelevantTests(tests, mutationDetails);
    }

    if (!isViable) {
      LOG.warning("Higher order mutation " + higherOrderMutationDetails + " is not viable");
      r.report(null, new MutationStatusTestPair(0, DetectionStatus.NON_VIABLE));
      return;
    }

    List<TestUnit> relevantTests = testSource.translateTests(tests);
    final MutationStatusTestPair statusTestPair = handleTests(higherOrderMutationDetails, relevantTests);

    //TODO collective report for higher order mutant

    if (DEBUG) {
      LOG.fine("Higher order mutation " + higherOrderMutationDetails.getId() + " detected = " + statusTestPair);
    }
  }

  private MutationStatusTestPair handleTests(HigherOrderMutationDetails higherOrderMutationDetails,
                                             List<TestUnit> relevantTests) {
    final MutationStatusTestPair mutationDetected;
    if ((relevantTests == null) || relevantTests.isEmpty()) {
      LOG.info("No test coverage for mutation  " + higherOrderMutationDetails.getId() + " in "
          + higherOrderMutationDetails.getMethods());
      mutationDetected = new MutationStatusTestPair(0,
          DetectionStatus.RUN_ERROR);
    } else {
      mutationDetected = handleTestExecution(higherOrderMutationDetails, relevantTests);
    }

    return mutationDetected;
  }

  private MutationStatusTestPair handleTestExecution(HigherOrderMutationDetails higherOrderMutationDetails, List<TestUnit> relevantTests) {
    if (DEBUG) {
      LOG.fine("" + relevantTests.size() + " relevant test for "
          + higherOrderMutationDetails.getMethods());
    }

    final Container c = createNewContainer(loader);
    return doTestsDetectMutation(c, relevantTests);
  }


  private void addRelevantTests(List<TestInfo> tests, MutationDetails mutationDetails) {
    tests.addAll(mutationDetails.getTestsInOrder());
  }

  private boolean processIndividualMutation(final Reporter r,
      final TimeOutDecoratedTestSource testSource,
      final MutationDetails mutationDetails) throws IOException {

    final MutationIdentifier mutationId = mutationDetails.getId();
    final Mutant mutatedClass = this.mutater.getMutation(mutationId);

    // For the benefit of mocking frameworks such as PowerMock
    // mess with the internals of Javassist so our mutated class
    // bytes are returned
    JavassistInterceptor.setMutant(mutatedClass);

    if (DEBUG) {
      LOG.fine("mutating method " + mutatedClass.getDetails().getMethod());
    }

    r.describe(mutationId);

    final boolean mutationSuccess = handleMutation(
        mutationDetails, mutatedClass);

    if (!mutationSuccess) {
        return false;
    }

//    r.report(mutationId, mutationDetected);
//    if (DEBUG) {
//      LOG.fine("Mutation " + mutationId + " detected = " + mutationDetected);
//    }

    return true;
  }

  private boolean handleMutation(
      final MutationDetails mutationId, final Mutant mutatedClass) {
    return handleCoveredMutation(mutationId, mutatedClass);
  }

  private boolean handleCoveredMutation(
      final MutationDetails mutationId, final Mutant mutatedClass) {

    //TODO is it using the right class loader?
    this.loader = pickClassLoaderForMutant(mutationId);
    final long t0 = System.currentTimeMillis();
    if (this.hotswap.apply(mutationId.getClassName(), loader,
        mutatedClass.getBytes())) {
      if (DEBUG) {
        LOG.fine("replaced class with mutant in "
            + (System.currentTimeMillis() - t0) + " ms");
      }

      this.mutater = engine.createMutator(new CachingByteArraySource(new ClassloaderByteArraySource(loader), 12));
      return true;
    } else {
      LOG.warning("Mutation " + mutationId + " was not viable ");
//      mutationDetected = new MutationStatusTestPair(0,
//          DetectionStatus.NON_VIABLE);
      return false;
    }

  }

  private static Container createNewContainer(final ClassLoader activeloader) {
    final Container c = new UnContainer() {
      @Override
      public List<TestResult> execute(final TestUnit group) {
        List<TestResult> results = new ArrayList<TestResult>();
        final ExitingResultCollector rc = new ExitingResultCollector(
            new ConcreteResultCollector(results));
        group.execute(activeloader, rc);
        return results;
      }
    };
    return c;
  }

  private ClassLoader pickClassLoaderForMutant(final MutationDetails mutant) {
    if (mutant.mayPoisonJVM()) {
      if (DEBUG) {
        LOG.fine("Creating new classloader for static initializer");
      }
      return new DefaultPITClassloader(new ClassPath(),
          IsolationUtils.bootClassLoader());
    } else {
      return this.loader;
    }
  }

  @Override
  public String toString() {
    return "MutationTestWorker [mutater=" + this.mutater + ", loader="
        + this.loader + ", hotswap=" + this.hotswap + "]";
  }

  private MutationStatusTestPair doTestsDetectMutation(final Container c,
      final List<TestUnit> tests) {
    try {
      final CheckTestHasFailedResultListener listener = new CheckTestHasFailedResultListener();

      final Pitest pit = new Pitest(Collections.singletonList(listener));
      pit.run(c, createEarlyExitTestGroup(tests));

      return createStatusTestPair(listener);
    } catch (final Exception ex) {
      throw translateCheckedException(ex);
    }

  }

  private MutationStatusTestPair createStatusTestPair(
      final CheckTestHasFailedResultListener listener) {
    if (listener.lastFailingTest().hasSome()) {
      return new MutationStatusTestPair(listener.getNumberOfTestsRun(),
          listener.status(), listener.lastFailingTest().value()
              .getQualifiedName());
    } else {
      return new MutationStatusTestPair(listener.getNumberOfTestsRun(),
          listener.status());
    }
  }

  private List<TestUnit> createEarlyExitTestGroup(final List<TestUnit> tests) {
    return Collections.<TestUnit> singletonList(new MultipleTestGroup(tests));
  }



}
