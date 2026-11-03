package com.bytter.cloud.common.core.crypto.cert;
import java.math.BigInteger;
public interface CertSNAllocator {
    BigInteger nextSerialNumber() throws Exception;
}
