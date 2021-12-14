package EncryptedDiary.app;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;



public class DiaryCipher {

    private final String UNICODE_FORMAT = "UTF-8";

    private SecretKey myKey;


    public DiaryCipher(){
        SecretKey myKey = this.generateSecretKey("AES");
    }

    public SecretKey getKey() {
        return myKey;
    }

    private SecretKey generateSecretKey(String encryptionType) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(encryptionType);
            this.myKey = keyGen.generateKey();
            return this.myKey;
        } catch (Exception ex) {
            System.out.println("ERROR IN GENERATING KEY");
            return null;
        }
    }

    public byte [] encryptText(String dataToEncrypt, SecretKey myKey, Cipher cipher){

        try {
            byte [] text = dataToEncrypt.getBytes(UNICODE_FORMAT);
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            byte [] encryptedText = cipher.doFinal(text);
            return encryptedText;
        }

        catch (Exception ex){
            System.out.println("ERROR IN ENCRYPTING MESSAGE");
            return null;
        }

    }

    public String decryptString(byte [] dataToDecrypt, SecretKey myKey, Cipher cipher){
        try{
            cipher.init(Cipher.DECRYPT_MODE, myKey);
            byte [] decryptedText = cipher.doFinal(dataToDecrypt);
            String result = new String(decryptedText);
            return result;
        }

        catch (Exception ex){
            System.out.println("ERROR IN DECRYPTING MESSAGE");
            return null;
        }
    }

}
