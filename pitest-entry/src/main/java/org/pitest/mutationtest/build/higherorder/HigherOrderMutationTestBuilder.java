package org.pitest.mutationtest.build.higherorder;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.MutationAnalyser;
import org.pitest.mutationtest.MutationConfig;
import org.pitest.mutationtest.build.MutationAnalysisUnit;
import org.pitest.mutationtest.build.MutationGrouper;
import org.pitest.mutationtest.build.MutationSource;
import org.pitest.mutationtest.build.MutationTestBuilder;
import org.pitest.mutationtest.build.NotGroupingGrouper;
import org.pitest.mutationtest.build.TestBuilder;
import org.pitest.mutationtest.build.factory.WorkerFactory;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationDetails;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationStrategy;
import org.pitest.mutationtest.engine.higherorder.HigherOrderMutationStrategyDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class HigherOrderMutationTestBuilder implements TestBuilder<HigherOrderMutationAnalysisUnit> {

    private String strategyId;
    private MutationConfig mutationConfig;
    private TestBuilder<MutationAnalysisUnit> firstOrderBuilder;

    private WorkerFactory workerFactory;

    public HigherOrderMutationTestBuilder(WorkerFactory workerFactory,
                                          MutationAnalyser analyser,
                                          MutationSource mutationSource,
                                          MutationGrouper grouper,
                                          MutationConfig mutationConfig,
                                          String strategyId) {
        this.mutationConfig = mutationConfig;
        this.strategyId = strategyId;
        this.firstOrderBuilder = new MutationTestBuilder(workerFactory, analyser, mutationSource, new NotGroupingGrouper());

        this.workerFactory = workerFactory;
    }

    @Override
    public List<HigherOrderMutationAnalysisUnit> createMutationTestUnits(Collection<ClassName> codeClasses) {
        List<MutationAnalysisUnit> testUnits = firstOrderBuilder.createMutationTestUnits(codeClasses);
        List<MutationDetails> mutations = collectMutations(testUnits);

        HigherOrderMutationStrategy strategy = getStrategy(strategyId);
        List<HigherOrderMutationDetails> higherOrderMutations = strategy.processMutations(mutations, mutationConfig.getEngine());

        Collections.sort(higherOrderMutations, comparator());

        List<HigherOrderMutationAnalysisUnit> analysisUnits = new ArrayList<HigherOrderMutationAnalysisUnit>();
        analysisUnits.add(new HigherOrderMutationTestUnit(higherOrderMutations, new HashSet<ClassName>(), workerFactory));

        return analysisUnits;
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

    //TODO some better algorithm?
    private Comparator<HigherOrderMutationDetails> comparator() {
        return new Comparator<HigherOrderMutationDetails>() {
            @Override
            public int compare(HigherOrderMutationDetails o1, HigherOrderMutationDetails o2) {
                return o1.getId().getIdentifiers().iterator().next()
                        .compareTo(o2.getId().getIdentifiers().iterator().next());
            }
        };
    }
}
