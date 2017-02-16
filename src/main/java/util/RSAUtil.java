package util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by z673413 on 2017/2/16.
 */
public class RSAUtil {
    public static final String PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCP4Jto0eLeFNSD/VScEEL4UdDag9cZNBjWfH3X" +
                    "5uRyU0p24hSOWSOVsjIbKcKCaUN58BrKEyOMEqHoaFRLTuo0/eKjyl9tGUhQWQykJg3UBtgxbD5Y" +
                    "N/GPmN4T7YqRDfLzd73j2ZVf9dTwhVnOPYl+DiyOzGV3wgnIEY6xXgxBcwIDAQAB";
    public static final String PRIVATE_KEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI/gm2jR4t4U1IP9VJwQQvhR0NqD" +
                    "1xk0GNZ8fdfm5HJTSnbiFI5ZI5WyMhspwoJpQ3nwGsoTI4wSoehoVEtO6jT94qPKX20ZSFBZDKQm" +
                    "DdQG2DFsPlg38Y+Y3hPtipEN8vN3vePZlV/11PCFWc49iX4OLI7MZXfCCcgRjrFeDEFzAgMBAAEC" +
                    "gYBLuy4SPmfr/Yre7rlabDTUADyuyDawTXvFakHTIvWcN7s6WEX5p3HTmbhGE/UOL9oUlQ2E0A4l" +
                    "7KjMbB3yhddAw00fSAaOrfoe+tPvV29uwSjJSq2ele/v0xggZX6HTRlvFH2iwFOQHJsbE0j5ZcL1" +
                    "I0LnhadVU4Bw2bkXmW+kAQJBAMP0Gk5lUTXvLClf2V6hXQMssoMqb/agegMWUTTy8hu5zmiFpSx+" +
                    "SpJcSxa5GHZGzi95etf+ZlYC0pA9QKaAD3kCQQC79031SMhhnt0Gc3wW/M4LkcVL74Ep760SolUG" +
                    "QVJkcbFODoOPPW7MHYRQ6cJBWcKVnaAApCMIma/3vsne6kFLAkBfdrS6LLH7zKF/JqsHZyWhPk1Z" +
                    "iXsyc8v9ZQn6+cDpsQPV+AYYPw6lb0FFuLcqKCSbDFvEjrPcg1OB4dV1SD5BAkEAotAY/9DWePCB" +
                    "ZPgdUVATLaRLcHX5vJh1osFnwm5MbuKoAQPDYwyEb1tV5DxG3hJg4PXSxRtnmSh+aqsg8uyYNwJB" +
                    "AJjKsXsH3Gqob+T1CCApQzQMpOOLrlV7w0pYt6UEZkC/bwFLlG0XBDl2VB+k5FljKgwc4yHb4uWP" +
                    "iiUkM+d1x1k=";
    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 密钥长度，用来初始化
     */
    private static final int KEY_SIZE = 1024;

    private static Map<String, String> generateKeyPair() throws Exception {
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        keyPairGenerator.initialize(KEY_SIZE);

        /** 生成密匙对 */
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = keyPair.getPublic();
        /** 得到私钥 */
        Key privateKey = keyPair.getPrivate();

        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("publicKey", encryptBASE64(publicKey.getEncoded()));
        returnMap.put("privateKey", encryptBASE64(privateKey.getEncoded()));

        return returnMap;
    }

    public static byte[] decryptBASE64(String key) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    public static String encryptBASE64(byte[] key) {
        return (new BASE64Encoder()).encodeBuffer(key);
    }


    public static String encrypt(String str, String key) throws Exception {
        PublicKey publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decryptBASE64(key)));

        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] src = str.getBytes();
        /** 执行加密操作 */
        byte[] dst = cipher.doFinal(src);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(dst);
    }

    public static String decrypt(String str, String key) throws Exception {
        PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decryptBASE64(key)));

        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] dst = decoder.decodeBuffer(str);

        /** 执行解密操作 */
        byte[] src = cipher.doFinal(dst);
        return new String(src);
    }
}