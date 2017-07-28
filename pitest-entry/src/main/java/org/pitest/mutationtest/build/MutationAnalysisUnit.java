package org.pitest.mutationtest.build;

import org.pitest.mutationtest.MutationMetaData;
import org.pitest.mutationtest.engine.MutationDetails;

/**
 * A unit of first order mutation analysis
 */
public interface MutationAnalysisUnit extends AnalysisUnit<MutationDetails, MutationMetaData> {

}
