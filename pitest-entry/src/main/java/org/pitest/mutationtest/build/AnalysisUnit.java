package org.pitest.mutationtest.build;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * A unit of mutation analysis
 */
public interface AnalysisUnit<T, V> extends Callable<V> {

  int priority();

  /**
   * returns the collection of mutations contained in this unit
   * @return collection of {@link T} or null
   */
  Collection<T> getMutations();

}
