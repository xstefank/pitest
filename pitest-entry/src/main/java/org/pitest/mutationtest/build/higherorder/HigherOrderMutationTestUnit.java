package org.pitest.mutationtest.build.higherorder;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.HigherOrderMutationStatusMap;
import org.pitest.mutationtest.MutationMetaData;
import org.pitest.mutationtest.build.factory.WorkerFactory;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationDetails;
import org.pitest.mutationtest.execute.MutationTestProcess;
import org.pitest.util.ExitCode;
import org.pitest.util.Log;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public class HigherOrderMutationTestUnit implements HigherOrderMutationAnalysisUnit {

    private List<HigherOrderMutationDetails> mutationDetails;

    private WorkerFactory workerFactory;
    private Collection<ClassName> testClasses;

    public HigherOrderMutationTestUnit(List<HigherOrderMutationDetails> higherOrderMutations,
                                       final Collection<ClassName> testClasses,
                                       WorkerFactory workerFactory) {
        this.mutationDetails = higherOrderMutations;
        this.testClasses = testClasses;
        this.workerFactory = workerFactory;
    }

    @Override
    public MutationMetaData call() throws Exception {
        Log.getLogger().info("HigherOrderMutationTestUnit.call()");

        final HigherOrderMutationStatusMap mutationsMap = new HigherOrderMutationStatusMap();

        mutationsMap.setStatusForMutations(this.mutationDetails,
                DetectionStatus.NOT_STARTED);

        runTestsInSeperateProcess(mutationsMap);


        return null;
    }


    private void runTestsInSeperateProcess(final HigherOrderMutationStatusMap mutations)
            throws IOException, InterruptedException {
        //TODO uncomment once reporting is done
//        while (mutations.hasUnrunMutations()) {
            runTestInSeperateProcessForMutationRange(mutations);
//        }
    }

    @SuppressWarnings("unchecked")
    private void runTestInSeperateProcessForMutationRange(
            final HigherOrderMutationStatusMap mutations) throws IOException,
            InterruptedException {

        final Collection<HigherOrderMutationDetails> remainingMutations = mutations
                .getUnrunMutations();
        final MutationTestProcess worker = this.workerFactory.createWorker(
                remainingMutations, this.testClasses);
        worker.start();
//
//        setFirstMutationToStatusOfStartedInCaseMinionFailsAtBoot(mutations,
//                remainingMutations);
//
        final ExitCode exitCode = waitForMinionToDie(worker);
//        worker.results(mutations);
//
//        correctResultForProcessExitCode(mutations, exitCode);
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Collection<HigherOrderMutationDetails> getMutations() {
        return null;
    }

    private static ExitCode waitForMinionToDie(final MutationTestProcess worker) {
        final ExitCode exitCode = worker.waitToDie();
        Log.getLogger().fine("Exit code was - " + exitCode);
        return exitCode;
    }
}
