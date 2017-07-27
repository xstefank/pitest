package org.pitest.mutationtest.build;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.MutationAnalyser;
import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationDetails;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategy;
import org.pitest.mutationtest.engine.hom.HigherOrderMutationStrategyDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class HigherOrderMutationTestBuilder extends MutationTestBuilder {

    private String strategyId;
    private MutationConfig mutationConfig;

    public HigherOrderMutationTestBuilder(WorkerFactory workerFactory,
                                          MutationAnalyser analyser,
                                          MutationSource mutationSource,
                                          MutationGrouper grouper,
                                          MutationConfig mutationConfig,
                                          String strategyId) {
        super(workerFactory, analyser, mutationSource, new NotGroupingGrouper());
        this.mutationConfig = mutationConfig;
        this.strategyId = strategyId;
    }

    @Override
    public List<MutationAnalysisUnit> createMutationTestUnits(Collection<ClassName> codeClasses) {
        List<MutationAnalysisUnit> testUnits = super.createMutationTestUnits(codeClasses);
        List<MutationDetails> mutations = collectMutations(testUnits);

        HigherOrderMutationStrategy strategy = getStrategy(strategyId);
        List<HigherOrderMutationDetails> higherOrderMutations = strategy.processMutations(mutations, mutationConfig.getEngine());

        Collections.sort(higherOrderMutations, comparator());

        //TODO transfer to MutationAnalysisUnit

        return testUnits;
    }

    private List<MutationDetails> collectMutations(List<MutationAnalysisUnit> testUnits) {
        List<MutationDetails> mutationDetails = new ArrayList<MutationDetails>();

        for (MutationAnalysisUnit testUnit : testUnits) {
            mutationDetails.addAll(testUnit.getMutations());
        }
        return mutationDetails;
    }

    private HigherOrderMutationStrategy getStrategy(String strategyId) {
        HigherOrderMutationStrategyDefinition strategyDefinition = HigherOrderMutationStrategyDefinition.findById(strategyId);

        if (strategyDefinition == null) {
            throw new UnsupportedOperationException(String.format("Unknown higher order strategy [ %s ]", strategyId));
        }

        return strategyDefinition.getStrategy();
    }

    private Comparator<HigherOrderMutationDetails> comparator() {
        return new Comparator<HigherOrderMutationDetails>() {
            @Override
            public int compare(HigherOrderMutationDetails o1, HigherOrderMutationDetails o2) {
                return o1.getId().iterator().next().compareTo(o2.getId().iterator().next());
            }
        };
    }
}
