package com.bytter.cloud.common.core.crypto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;
public class GMBaseUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
}
