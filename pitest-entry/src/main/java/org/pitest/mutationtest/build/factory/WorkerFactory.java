package org.pitest.mutationtest.build.factory;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.execute.MutationTestProcess;

import java.util.Collection;

public interface WorkerFactory<T> {

  MutationTestProcess createWorker(final Collection<T> remainingMutations,
      final Collection<ClassName> testClasses);

}
