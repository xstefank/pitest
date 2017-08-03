package org.pitest.mutationtest.build.factory;

import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.execute.HigherOrderMutationTestProcessImpl;
import org.pitest.mutationtest.execute.MinionArguments;
import org.pitest.mutationtest.execute.MutationTestProcess;
import org.pitest.process.ProcessArgs;

import java.net.ServerSocket;

public class HigherOrderWorkerFactory extends AbstractWorkerFactory<MutationDetails> {

  public HigherOrderWorkerFactory(WorkerFactoryArgs args) {
    super(args.getBaseDir(),
            args.getPitConfig(),
            args.getConfig(),
            args.getTimeoutStrategy(),
            args.isVerbose(),
            args.getClassPath());
  }

  @Override
  public MutationTestProcess newWorker(ServerSocket socket, ProcessArgs args, MinionArguments fileArgs) {
    return new HigherOrderMutationTestProcessImpl(socket, args, fileArgs);
  }

}
