package org.example.util;

import org.hashids.Hashids;

public class CryptoTool {

    private static final int MIN_HASH_LEN = 10;

    private final Hashids hashids;

    public CryptoTool(String salt) {
        this.hashids = new Hashids(salt, MIN_HASH_LEN);
    }

    public String hashOf(Long value) {
        return this.hashids.encode(value);
    }

    public Long idOf(String hash) {
        long[] res = this.hashids.decode(hash);
        if (res.length != 0) {
            return res[0];
        }
        throw new RuntimeException("Can't decode. Invalid hash: " + hash);
    }
}
