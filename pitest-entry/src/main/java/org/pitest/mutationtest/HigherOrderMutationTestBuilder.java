package org.pitest.mutationtest;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.build.MutationAnalysisUnit;
import org.pitest.mutationtest.build.MutationGrouper;
import org.pitest.mutationtest.build.MutationSource;
import org.pitest.mutationtest.build.MutationTestBuilder;
import org.pitest.mutationtest.build.NotGroupingGrouper;
import org.pitest.mutationtest.build.WorkerFactory;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategy;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategyDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class HigherOrderMutationTestBuilder extends MutationTestBuilder {

    private String strategyId;

    public HigherOrderMutationTestBuilder(WorkerFactory workerFactory, MutationAnalyser analyser, MutationSource mutationSource, MutationGrouper grouper, String strategyId) {
        super(workerFactory, analyser, mutationSource, new NotGroupingGrouper());
        this.strategyId = strategyId;
    }

    @Override
    public List<MutationAnalysisUnit> createMutationTestUnits(Collection<ClassName> codeClasses) {
        List<MutationAnalysisUnit> testUnits = super.createMutationTestUnits(codeClasses);
        List<MutationDetails> mutationDetails = new ArrayList<MutationDetails>();

        for (MutationAnalysisUnit testUnit : testUnits) {
            mutationDetails.addAll(testUnit.getMutations());
        }

        //find the desired higher order strategy
        HigherOrderMutationStrategy strategy = getStrategy(strategyId);

        return testUnits;
    }

    private HigherOrderMutationStrategy getStrategy(String strategyId) {
        HigherOrderMutationStrategyDefinition strategyDefinition = HigherOrderMutationStrategyDefinition.findById(strategyId);

        if (strategyDefinition == null) {
            //unknown strategy requested
            throw new UnsupportedOperationException(String.format("Unknown higher order strategy [ %s ]", strategyId));
        }

        return strategyDefinition.getStrategy();
    }
}
