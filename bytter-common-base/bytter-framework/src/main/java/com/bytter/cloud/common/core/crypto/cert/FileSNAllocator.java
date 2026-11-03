package com.bytter.cloud.common.core.crypto.cert;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
public class FileSNAllocator implements CertSNAllocator {
    private static final String SN_FILENAME = "sn.dat";
    private static final String snFilePath;
    static {
        ClassLoader loader = FileSNAllocator.class.getClassLoader();
        snFilePath = loader.getResource(SN_FILENAME).getPath();
    }
    public synchronized BigInteger nextSerialNumber() throws Exception {
        BigInteger sn = readSN();
        writeSN(sn.add(BigInteger.ONE));
        return sn;
    }
    private BigInteger readSN() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(snFilePath, "r")) {
            byte[] data = new byte[(int) raf.length()];
            int read = raf.read(data);
            String snStr = new String(data, StandardCharsets.UTF_8);
            return new BigInteger(snStr);
        }
    }
    private void writeSN(BigInteger sn) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(snFilePath, "rw")) {
            raf.writeBytes(sn.toString(10));
        }
    }
}
