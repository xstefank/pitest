package org.pitest.mutationtest.build.factory;

import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.execute.MinionArguments;
import org.pitest.mutationtest.execute.MutationTestProcess;
import org.pitest.mutationtest.execute.MutationTestProcessImpl;
import org.pitest.process.ProcessArgs;

import java.net.ServerSocket;

public class FirstOrderWorkerFactory extends AbstractWorkerFactory<MutationDetails> {

  public FirstOrderWorkerFactory(WorkerFactoryArgs args) {
    super(args.getBaseDir(),
            args.getPitConfig(),
            args.getConfig(),
            args.getTimeoutStrategy(),
            args.isVerbose(),
            args.getClassPath());
  }

  @Override
  public MutationTestProcess newWorker(ServerSocket socket, ProcessArgs args, MinionArguments fileArgs) {
    return new MutationTestProcessImpl(socket, args, fileArgs);
  }

}
