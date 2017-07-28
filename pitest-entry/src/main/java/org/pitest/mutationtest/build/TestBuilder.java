package org.pitest.mutationtest.build;


import org.pitest.classinfo.ClassName;

import java.util.Collection;
import java.util.List;

public interface TestBuilder<T extends AnalysisUnit> {

    List<T> createMutationTestUnits(final Collection<ClassName> codeClasses);

}
