package org.pitest.mutationtest.build.factory;

import org.pitest.classinfo.ClassName;
import org.pitest.functional.SideEffect1;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.TimeoutLengthStrategy;
import org.pitest.mutationtest.execute.MinionArguments;
import org.pitest.mutationtest.execute.MutationTestProcess;
import org.pitest.process.ProcessArgs;
import org.pitest.testapi.Configuration;
import org.pitest.util.Log;
import org.pitest.util.SocketFinder;

import java.io.File;
import java.net.ServerSocket;
import java.util.Collection;

import static org.pitest.functional.prelude.Prelude.printWith;

public abstract class AbstractWorkerFactory<T> implements WorkerFactory<T> {

  private final String                classPath;
  private final File                  baseDir;
  private final Configuration         pitConfig;
  private final TimeoutLengthStrategy timeoutStrategy;
  private final boolean               verbose;
  private final MutationConfig        config;

  public AbstractWorkerFactory(final File baseDir, final Configuration pitConfig,
                               final MutationConfig mutationConfig,
                               final TimeoutLengthStrategy timeoutStrategy, final boolean verbose,
                               final String classPath) {
    this.pitConfig = pitConfig;
    this.timeoutStrategy = timeoutStrategy;
    this.verbose = verbose;
    this.classPath = classPath;
    this.baseDir = baseDir;
    this.config = mutationConfig;
  }

  @Override
  public MutationTestProcess createWorker(
      final Collection<T> remainingMutations,
      final Collection<ClassName> testClasses) {
    final MinionArguments fileArgs = new MinionArguments<T>(remainingMutations,
        testClasses, this.config.getEngine(), this.timeoutStrategy,
        Log.isVerbose(), this.pitConfig);

    final ProcessArgs args = ProcessArgs.withClassPath(this.classPath)
        .andLaunchOptions(this.config.getLaunchOptions())
        .andBaseDir(this.baseDir).andStdout(captureStdOutIfVerbose())
        .andStderr(printWith("stderr "));

    final SocketFinder sf = new SocketFinder();
    return newWorker(sf.getNextAvailableServerSocket(), args, fileArgs);
  }

  public abstract MutationTestProcess newWorker(ServerSocket socket, ProcessArgs args, MinionArguments fileArgs);

  private SideEffect1<String> captureStdOutIfVerbose() {
    if (this.verbose) {
      return Prelude.printWith("stdout ");
    } else {
      return Prelude.noSideEffect(String.class);
    }

  }

}
