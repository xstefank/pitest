package org.pitest.mutationtest.build.higherorder;

import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.HigherOrderMutationStatusMap;
import org.pitest.mutationtest.MutationMetaData;
import org.pitest.mutationtest.build.WorkerFactory;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationDetails;
import org.pitest.util.Log;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public class HigherOrderMutationTestUnit implements HigherOrderMutationAnalysisUnit {

    private List<HigherOrderMutationDetails> mutationDetails;

    private WorkerFactory workerFactory;

    public HigherOrderMutationTestUnit(List<HigherOrderMutationDetails> higherOrderMutations,
                                       WorkerFactory workerFactory) {
        this.mutationDetails = higherOrderMutations;
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
        while (mutations.hasUnrunMutations()) {
            runTestInSeperateProcessForMutationRange(mutations);
        }
    }

    private void runTestInSeperateProcessForMutationRange(
            final HigherOrderMutationStatusMap mutations) throws IOException,
            InterruptedException {

        final Collection<HigherOrderMutationDetails> remainingMutations = mutations
                .getUnrunMutations();
//        final MutationTestProcess worker = this.workerFactory.createWorker(
//                remainingMutations, this.testClasses);
//        worker.start();
//
//        setFirstMutationToStatusOfStartedInCaseMinionFailsAtBoot(mutations,
//                remainingMutations);
//
//        final ExitCode exitCode = waitForMinionToDie(worker);
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
}
