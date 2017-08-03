package org.pitest.mutationtest.execute;

import org.pitest.mutationtest.MutationStatusMap;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationIdentifier;
import org.pitest.process.ProcessArgs;
import org.pitest.process.WrappingProcess;
import org.pitest.util.ExitCode;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class HigherOrderMutationTestProcessImpl implements MutationTestProcess {

  private final WrappingProcess                 process;
  private final MutationTestCommunicationThread thread;

  public HigherOrderMutationTestProcessImpl(final ServerSocket socket,
                                            final ProcessArgs processArgs, final MinionArguments arguments) {
    this.process = new WrappingProcess(socket.getLocalPort(), processArgs,
        HigherOrderMutationTestMinion.class);
    this.thread = new MutationTestCommunicationThread<HigherOrderMutationIdentifier>(socket, arguments,
        new HashMap<HigherOrderMutationIdentifier, MutationStatusTestPair>(), HigherOrderMutationIdentifier.class);

  }

  @Override
  public void start() throws IOException, InterruptedException {
    this.thread.start();
    this.process.start();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void results(final MutationStatusMap allmutations) throws IOException {

    for (final MutationDetails each : allmutations.allMutations()) {
      final MutationStatusTestPair status = this.thread.getStatus(each.getId());
      if (status != null) {
        allmutations.setStatusForMutation(each, status);
      }
    }

  }

  @Override
  public ExitCode waitToDie() {
    try {
      return this.thread.waitToFinish();
    } finally {
      this.process.destroy();
    }

  }

}
