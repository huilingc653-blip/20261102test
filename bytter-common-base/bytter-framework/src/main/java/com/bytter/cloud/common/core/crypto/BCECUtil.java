package com.bytter.cloud.common.core.crypto;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * 这个工具类的方法，也适用于其他基于BC库的ECC算法
 */
public class BCECUtil {
    private static final String ALGO_NAME_EC = "EC";
    private static final String PEM_STRING_PUBLIC = "PUBLIC KEY";
    private static final String PEM_STRING_ECPRIVATEKEY = "EC PRIVATE KEY";
    /**
     * 生成ECC密钥对
     *
     * @param domainParameters domainParameters
     * @param random           random
     * @return ECC密钥对
     */
    public static AsymmetricCipherKeyPair generateKeyPairParameter(
            ECDomainParameters domainParameters, SecureRandom random) {
        ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(domainParameters,
                random);
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(keyGenerationParams);
        return keyGen.generateKeyPair();
    }
    public static KeyPair generateKeyPair(ECDomainParameters domainParameters, SecureRandom random)
            throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO_NAME_EC, BouncyCastleProvider.PROVIDER_NAME);
        ECParameterSpec parameterSpec = new ECParameterSpec(domainParameters.getCurve(), domainParameters.getG(),
                domainParameters.getN(), domainParameters.getH());
        kpg.initialize(parameterSpec, random);
        return kpg.generateKeyPair();
    }
    public static int getCurveLength(ECKeyParameters ecKey) {
        return getCurveLength(ecKey.getParameters());
    }
    public static int getCurveLength(ECDomainParameters domainParams) {
        return (domainParams.getCurve().getFieldSize() + 7) / 8;
    }
    public static byte[] fixToCurveLengthBytes(int curveLength, byte[] src) {
        if (src.length == curveLength) {
            return src;
        }
        byte[] result = new byte[curveLength];
        if (src.length > curveLength) {
            System.arraycopy(src, src.length - result.length, result, 0, result.length);
        } else {
            System.arraycopy(src, 0, result, result.length - src.length, src.length);
        }
        return result;
    }
    /**
     * @param dHex             十六进制字符串形式的私钥d值，如果是SM2算法，Hex字符串长度应该是64（即32字节）
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPrivateKeyParameters
     */
    public static ECPrivateKeyParameters createECPrivateKeyParameters(
            String dHex, ECDomainParameters domainParameters) {
        return createECPrivateKeyParameters(ByteUtils.fromHexString(dHex), domainParameters);
    }
    /**
     * @param dBytes           字节数组形式的私钥d值，如果是SM2算法，应该是32字节
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPrivateKeyParameters
     */
    public static ECPrivateKeyParameters createECPrivateKeyParameters(
            byte[] dBytes, ECDomainParameters domainParameters) {
        return createECPrivateKeyParameters(new BigInteger(1, dBytes), domainParameters);
    }
    /**
     * @param d                大数形式的私钥d值
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPrivateKeyParameters
     */
    public static ECPrivateKeyParameters createECPrivateKeyParameters(
            BigInteger d, ECDomainParameters domainParameters) {
        return new ECPrivateKeyParameters(d, domainParameters);
    }
    /**
     * 根据EC私钥构造EC公钥
     *
     * @param priKey ECC私钥参数对象
     * @return ECPublicKeyParameters
     */
    public static ECPublicKeyParameters buildECPublicKeyByPrivateKey(ECPrivateKeyParameters priKey) {
        ECDomainParameters domainParameters = priKey.getParameters();
        ECPoint q = new FixedPointCombMultiplier().multiply(domainParameters.getG(), priKey.getD());
        return new ECPublicKeyParameters(q, domainParameters);
    }
    /**
     * @param x                大数形式的公钥x分量
     * @param y                大数形式的公钥y分量
     * @param curve            EC曲线参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#CURVE}
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPublicKeyParameters
     */
    public static ECPublicKeyParameters createECPublicKeyParameters(
            BigInteger x, BigInteger y, ECCurve curve, ECDomainParameters domainParameters) {
        return createECPublicKeyParameters(x.toByteArray(), y.toByteArray(), curve, domainParameters);
    }
    /**
     * @param xHex             十六进制形式的公钥x分量，如果是SM2算法，Hex字符串长度应该是64（即32字节）
     * @param yHex             十六进制形式的公钥y分量，如果是SM2算法，Hex字符串长度应该是64（即32字节）
     * @param curve            EC曲线参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#CURVE}
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPublicKeyParameters
     */
    public static ECPublicKeyParameters createECPublicKeyParameters(
            String xHex, String yHex, ECCurve curve, ECDomainParameters domainParameters) {
        return createECPublicKeyParameters(ByteUtils.fromHexString(xHex), ByteUtils.fromHexString(yHex),
                curve, domainParameters);
    }
    /**
     * @param xBytes           十六进制形式的公钥x分量，如果是SM2算法，应该是32字节
     * @param yBytes           十六进制形式的公钥y分量，如果是SM2算法，应该是32字节
     * @param curve            EC曲线参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#CURVE}
     * @param domainParameters EC Domain参数，一般是固定的，如果是SM2算法的可参考{@link SM2Util#DOMAIN_PARAMS}
     * @return ECPublicKeyParameters
     */
    public static ECPublicKeyParameters createECPublicKeyParameters(
            byte[] xBytes, byte[] yBytes, ECCurve curve, ECDomainParameters domainParameters) {
        final byte uncompressedFlag = 0x04;
        int curveLength = getCurveLength(domainParameters);
        xBytes = fixToCurveLengthBytes(curveLength, xBytes);
        yBytes = fixToCurveLengthBytes(curveLength, yBytes);
        byte[] encodedPubKey = new byte[1 + xBytes.length + yBytes.length];
        encodedPubKey[0] = uncompressedFlag;
        System.arraycopy(xBytes, 0, encodedPubKey, 1, xBytes.length);
        System.arraycopy(yBytes, 0, encodedPubKey, 1 + xBytes.length, yBytes.length);
        return new ECPublicKeyParameters(curve.decodePoint(encodedPubKey), domainParameters);
    }
    public static ECPrivateKeyParameters convertPrivateKeyToParameters(BCECPrivateKey ecPriKey) {
        ECParameterSpec parameterSpec = ecPriKey.getParameters();
        ECDomainParameters domainParameters = new ECDomainParameters(parameterSpec.getCurve(), parameterSpec.getG(),
                parameterSpec.getN(), parameterSpec.getH());
        return new ECPrivateKeyParameters(ecPriKey.getD(), domainParameters);
    }
    public static ECPublicKeyParameters convertPublicKeyToParameters(BCECPublicKey ecPubKey) {
        ECParameterSpec parameterSpec = ecPubKey.getParameters();
        ECDomainParameters domainParameters = new ECDomainParameters(parameterSpec.getCurve(), parameterSpec.getG(),
                parameterSpec.getN(), parameterSpec.getH());
        return new ECPublicKeyParameters(ecPubKey.getQ(), domainParameters);
    }
    public static BCECPublicKey createPublicKeyFromSubjectPublicKeyInfo(SubjectPublicKeyInfo subPubInfo)
            throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return BCECUtil.convertX509ToECPublicKey(subPubInfo.toASN1Primitive().getEncoded(ASN1Encoding.DER));
    }
    /**
     * 将ECC私钥转换为PKCS8标准的字节流
     *
     * @param priKey 私钥
     * @param pubKey 可以为空，但是如果为空的话得到的结果OpenSSL可能解析不了
     * @return 数组
     */
    public static byte[] convertECPrivateKeyToPKCS8(
            ECPrivateKeyParameters priKey, ECPublicKeyParameters pubKey) {
        ECDomainParameters domainParams = priKey.getParameters();
        ECParameterSpec spec = new ECParameterSpec(domainParams.getCurve(), domainParams.getG(),
                domainParams.getN(), domainParams.getH());
        BCECPublicKey publicKey = null;
        if (pubKey != null) {
            publicKey = new BCECPublicKey(ALGO_NAME_EC, pubKey, spec,
                    BouncyCastleProvider.CONFIGURATION);
        }
        BCECPrivateKey privateKey = new BCECPrivateKey(ALGO_NAME_EC, priKey, publicKey,
                spec, BouncyCastleProvider.CONFIGURATION);
        return privateKey.getEncoded();
    }
    /**
     * 将PKCS8标准的私钥字节流转换为私钥对象
     *
     * @param pkcs8Key pkcs8Key
     * @return BCECPrivateKey
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException  异常
     * @throws InvalidKeySpecException  异常
     */
    public static BCECPrivateKey convertPKCS8ToECPrivateKey(byte[] pkcs8Key)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(pkcs8Key);
        KeyFactory kf = KeyFactory.getInstance(ALGO_NAME_EC, BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPrivateKey) kf.generatePrivate(peks);
    }
    /**
     * 将PKCS8标准的私钥字节流转换为PEM
     *
     * @param encodedKey 加密秘钥
     * @return 字符串
     * @throws IOException IO异常
     */
    public static String convertECPrivateKeyPKCS8ToPEM(byte[] encodedKey) throws IOException {
        return convertEncodedDataToPEM(PEM_STRING_ECPRIVATEKEY, encodedKey);
    }
    /**
     * 将PEM格式的私钥转换为PKCS8标准字节流
     *
     * @param pemString pem文件
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] convertECPrivateKeyPEMToPKCS8(String pemString) throws IOException {
        return convertPEMToEncodedData(pemString);
    }
    /**
     * 将ECC私钥转换为SEC1标准的字节流
     * openssl d2i_ECPrivateKey函数要求的DER编码的私钥也是SEC1标准的，
     * 这个工具函数的主要目的就是为了能生成一个openssl可以直接“识别”的ECC私钥.
     * 相对RSA私钥的PKCS1标准，ECC私钥的标准为SEC1
     *
     * @param priKey 私钥
     * @param pubKey 公钥
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] convertECPrivateKeyToSEC1(
            ECPrivateKeyParameters priKey, ECPublicKeyParameters pubKey) throws IOException {
        byte[] pkcs8Bytes = convertECPrivateKeyToPKCS8(priKey, pubKey);
        PrivateKeyInfo pki = PrivateKeyInfo.getInstance(pkcs8Bytes);
        ASN1Encodable encodable = pki.parsePrivateKey();
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] sec1Bytes = primitive.getEncoded();
        return sec1Bytes;
    }
    /**
     * 将SEC1标准的私钥字节流恢复为PKCS8标准的字节流
     *
     * @param sec1Key sec1Key
     * @return 字节数组
     * @throws IOException IO异常
     */
    public static byte[] convertECPrivateKeySEC1ToPKCS8(byte[] sec1Key) throws IOException {
        /*
         * 参考org.bouncycastle.asn1.pkcs.PrivateKeyInfo和
         * org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey，逆向拼装
         */
        X962Parameters params = getDomainParametersFromName(SM2Util.JDK_EC_SPEC, false);
        ASN1OctetString privKey = new DEROctetString(sec1Key);
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(0)); //版本号
        v.add(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, params)); //算法标识
        v.add(privKey);
        DERSequence ds = new DERSequence(v);
        return ds.getEncoded(ASN1Encoding.DER);
    }
    /**
     * 将SEC1标准的私钥字节流转为BCECPrivateKey对象
     *
     * @param sec1Key sec1Key
     * @return BCECPrivateKey
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchProviderException  NoSuchProviderException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws IOException              IOException
     */
    public static BCECPrivateKey convertSEC1ToBCECPrivateKey(byte[] sec1Key)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(convertECPrivateKeySEC1ToPKCS8(sec1Key));
        KeyFactory kf = KeyFactory.getInstance(ALGO_NAME_EC, BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPrivateKey) kf.generatePrivate(peks);
    }
    /**
     * 将SEC1标准的私钥字节流转为ECPrivateKeyParameters对象
     * openssl i2d_ECPrivateKey函数生成的DER编码的ecc私钥是：SEC1标准的、带有EC_GROUP、带有公钥的，
     * 这个工具函数的主要目的就是为了使Java程序能够“识别”openssl生成的ECC私钥
     *
     * @param sec1Key sec1Key
     * @return ECPrivateKeyParameters
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchProviderException  NoSuchProviderException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws IOException  IOException
     */
    public static ECPrivateKeyParameters convertSEC1ToECPrivateKey(byte[] sec1Key)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
        BCECPrivateKey privateKey = convertSEC1ToBCECPrivateKey(sec1Key);
        return convertPrivateKeyToParameters(privateKey);
    }
    /**
     * 将ECC公钥对象转换为X509标准的字节流
     *
     * @param pubKey pubKey
     * @return 字节数组
     */
    public static byte[] convertECPublicKeyToX509(ECPublicKeyParameters pubKey) {
        ECDomainParameters domainParams = pubKey.getParameters();
        ECParameterSpec spec = new ECParameterSpec(domainParams.getCurve(), domainParams.getG(),
                domainParams.getN(), domainParams.getH());
        BCECPublicKey publicKey = new BCECPublicKey(ALGO_NAME_EC, pubKey, spec,
                BouncyCastleProvider.CONFIGURATION);
        return publicKey.getEncoded();
    }
    /**
     * 将X509标准的公钥字节流转为公钥对象
     *
     * @param x509Bytes x509Bytes
     * @return BCECPublicKey
     * @throws NoSuchProviderException  NoSuchProviderException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    public static BCECPublicKey convertX509ToECPublicKey(byte[] x509Bytes) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec eks = new X509EncodedKeySpec(x509Bytes);
        KeyFactory kf = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        return (BCECPublicKey) kf.generatePublic(eks);
    }
    /**
     * 将X509标准的公钥字节流转为PEM
     *
     * @param encodedKey 加密秘钥
     * @return PEM
     * @throws IOException IOException
     */
    public static String convertECPublicKeyX509ToPEM(byte[] encodedKey) throws IOException {
        return convertEncodedDataToPEM(PEM_STRING_PUBLIC, encodedKey);
    }
    /**
     * 将PEM格式的公钥转为X509标准的字节流
     *
     * @param pemString pemString
     * @return 字节流
     * @throws IOException IOException
     */
    public static byte[] convertECPublicKeyPEMToX509(String pemString) throws IOException {
        return convertPEMToEncodedData(pemString);
    }
    /**
     * copy from BC
     *
     * @param genSpec genSpec
     * @return X9ECParameters
     */
    public static X9ECParameters getDomainParametersFromGenSpec(ECGenParameterSpec genSpec) {
        return getDomainParametersFromName(genSpec.getName());
    }
    /**
     * copy from BC
     *
     * @param curveName curveName
     * @return X9ECParameters
     */
    public static X9ECParameters getDomainParametersFromName(String curveName) {
        X9ECParameters domainParameters;
        try {
            if (curveName.charAt(0) >= '0' && curveName.charAt(0) <= '2') {
                ASN1ObjectIdentifier oidID = new ASN1ObjectIdentifier(curveName);
                domainParameters = ECUtil.getNamedCurveByOid(oidID);
            } else {
                if (curveName.indexOf(' ') > 0) {
                    curveName = curveName.substring(curveName.indexOf(' ') + 1);
                    domainParameters = ECUtil.getNamedCurveByName(curveName);
                } else {
                    domainParameters = ECUtil.getNamedCurveByName(curveName);
                }
            }
        } catch (IllegalArgumentException ex) {
            domainParameters = ECUtil.getNamedCurveByName(curveName);
        }
        return domainParameters;
    }
    /**
     * copy from BC
     *
     * @param ecSpec          ecSpec
     * @param withCompression withCompression
     * @return X962Parameters
     */
    public static X962Parameters getDomainParametersFromName(
            java.security.spec.ECParameterSpec ecSpec, boolean withCompression) {
        X962Parameters params;
        if (ecSpec instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier curveOid = ECUtil.getNamedCurveOid(((ECNamedCurveSpec) ecSpec).getName());
            if (curveOid == null) {
                curveOid = new ASN1ObjectIdentifier(((ECNamedCurveSpec) ecSpec).getName());
            }
            params = new X962Parameters(curveOid);
        } else if (ecSpec == null) {
            params = new X962Parameters(DERNull.INSTANCE);
        } else {
            ECCurve curve = EC5Util.convertCurve(ecSpec.getCurve());
            X9ECParameters ecP = new X9ECParameters(
                    curve,
                    new X9ECPoint(EC5Util.convertPoint(curve, ecSpec.getGenerator()), withCompression),
                    ecSpec.getOrder(),
                    BigInteger.valueOf(ecSpec.getCofactor()),
                    ecSpec.getCurve().getSeed());
            //// 如果是1.62或更低版本的bcprov-jdk15on应该使用以下这段代码，因为高版本的EC5Util.convertPoint没有向下兼容
            /*
            X9ECParameters ecP = new X9ECParameters(
                curve,
                EC5Util.convertPoint(curve, ecSpec.getGenerator(), withCompression),
                ecSpec.getOrder(),
                BigInteger.valueOf(ecSpec.getCofactor()),
                ecSpec.getCurve().getSeed());
            */
            params = new X962Parameters(ecP);
        }
        return params;
    }
    private static String convertEncodedDataToPEM(String type, byte[] encodedData) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try (PemWriter pWrt = new PemWriter(new OutputStreamWriter(bOut, StandardCharsets.UTF_8))) {
            PemObject pemObj = new PemObject(type, encodedData);
            pWrt.writeObject(pemObj);
        }
        return bOut.toString(StandardCharsets.UTF_8.name());
    }
    private static byte[] convertPEMToEncodedData(String pemString) throws IOException {
        ByteArrayInputStream bIn = new ByteArrayInputStream(pemString.getBytes(StandardCharsets.UTF_8));
        try (PemReader pRdr = new PemReader(new InputStreamReader(bIn, StandardCharsets.UTF_8))) {
            PemObject pemObject = pRdr.readPemObject();
            return pemObject.getContent();
        }
    }
}
