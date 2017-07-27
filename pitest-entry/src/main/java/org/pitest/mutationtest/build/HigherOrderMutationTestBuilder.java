package org.pitest.mutationtest;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.build.MutationAnalysisUnit;
import org.pitest.mutationtest.build.MutationGrouper;
import org.pitest.mutationtest.build.MutationSource;
import org.pitest.mutationtest.build.MutationTestBuilder;
import org.pitest.mutationtest.build.NotGroupingGrouper;
import org.pitest.mutationtest.build.WorkerFactory;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.hom.AbstractHigherOrderMutationDetails;
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
        List<AbstractHigherOrderMutationDetails> higherOrderMutations = strategy.processMutations(mutations, mutationConfig);

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

    private Comparator<AbstractHigherOrderMutationDetails> comparator() {
        return new Comparator<AbstractHigherOrderMutationDetails>() {
            @Override
            public int compare(AbstractHigherOrderMutationDetails o1, AbstractHigherOrderMutationDetails o2) {
                return 0;
            }
        };
    }
}
