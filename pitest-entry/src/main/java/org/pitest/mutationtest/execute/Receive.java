package org.pitest.mutationtest.execute;

import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.util.Id;
import org.pitest.util.Log;
import org.pitest.util.ReceiveStrategy;
import org.pitest.util.SafeDataInputStream;

import java.util.Map;
import java.util.logging.Logger;

/**
 *
 */
public class Receive<K> implements ReceiveStrategy {

    private static final Logger LOG = Log.getLogger();

    private final Map<K, MutationStatusTestPair> idMap;
    private final Class<K> keyType;

    Receive(final Map<K, MutationStatusTestPair> idMap, Class<K> keyType) {
        this.idMap = idMap;
        this.keyType = keyType;
    }

    @Override
    public void apply(final byte control, final SafeDataInputStream is) {
        switch (control) {
            case Id.DESCRIBE:
                handleDescribe(is);
                break;
            case Id.REPORT:
                handleReport(is);
                break;
        }
    }

    private void handleReport(final SafeDataInputStream is) {
        final K mutation = is.read(keyType);
        final MutationStatusTestPair value = is
                .read(MutationStatusTestPair.class);
        this.idMap.put(mutation, value);
        LOG.fine(mutation + " " + value);
    }

    private void handleDescribe(final SafeDataInputStream is) {
        final K mutation = is.read(keyType);
        this.idMap.put(mutation, new MutationStatusTestPair(1,
                DetectionStatus.STARTED));
    }

}
