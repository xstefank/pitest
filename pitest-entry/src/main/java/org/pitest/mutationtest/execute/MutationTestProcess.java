package org.pitest.mutationtest.execute;

import org.pitest.mutationtest.MutationStatusMap;
import org.pitest.util.ExitCode;

import java.io.IOException;

public interface MutationTestProcess {

  void start() throws IOException, InterruptedException;

  void results(final MutationStatusMap allmutations) throws IOException;

  ExitCode waitToDie();

}
