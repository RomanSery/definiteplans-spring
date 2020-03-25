package com.definiteplans.service;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;


@Service
public class CipherService {
    private AesCipherService cipher = new AesCipherService();

    private byte[] cipherKeyBytes;


    public String encryptAndBase64Encode(String secretString) {
        byte[] secretBytes = CodecSupport.toBytes(secretString);
        ByteSource encryptedBytes = this.cipher.encrypt(secretBytes, this.cipherKeyBytes);
        return Base64.encodeToString(encryptedBytes.getBytes());
    }


    public String base64DecodeAndDecrypt(String base64EncodedEncryptedString) throws Exception {
        byte[] encryptedEncodedBytes = CodecSupport.toBytes(base64EncodedEncryptedString);
        byte[] encryptedBytes = Base64.decode(encryptedEncodedBytes);
        ByteSource decrypted = this.cipher.decrypt(encryptedBytes, this.cipherKeyBytes);
        return CodecSupport.toString(decrypted.getBytes());
    }


    public byte[] encryptBytes(byte[] plainByteArray) {
        ByteSource encryptedBytes = this.cipher.encrypt(plainByteArray, this.cipherKeyBytes);
        return encryptedBytes.getBytes();
    }

    public byte[] decryptBytes(byte[] encByteArray) {
        ByteSource plainBytes = this.cipher.decrypt(encByteArray, this.cipherKeyBytes);
        return plainBytes.getBytes();
    }

    public void setCipherKey(byte[] cipherKeyBytes) {
        this.cipherKeyBytes = cipherKeyBytes;
    }
}
