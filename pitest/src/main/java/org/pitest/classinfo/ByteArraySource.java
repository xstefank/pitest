package org.pitest.classinfo;

import org.pitest.functional.Option;

public class ByteArraySource implements ClassByteArraySource {

    private byte[] bytes;

    public ByteArraySource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public Option<byte[]> getBytes(String clazz) {
        return Option.some(bytes);
    }

}
