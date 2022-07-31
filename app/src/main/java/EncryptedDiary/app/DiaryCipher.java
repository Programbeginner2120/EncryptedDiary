package EncryptedDiary.app;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



public class DiaryCipher {

    private static  final String UNICODE_FORMAT = "UTF-8";  // specifying unicode

    private SecretKey myKey;

    // Basic constructor
    public DiaryCipher(){
        SecretKey myKey = this.generateSecretKey("AES");
    }

    // get private instance variable myKey
    public SecretKey getKey() {
        return myKey;
    }

    // Generating secret key as specified by string encryptionType
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

    // Encrypting text based upon encrypting type
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

    // Decrypting bytes based upon encrypting type
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

//    public static void main (String [] args){
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//
//            DiaryCipher myDiaryCipher = new DiaryCipher();
//            SecretKey key = myDiaryCipher.getKey();
//
//            byte[] encryptedBytes = myDiaryCipher.encryptText("HELLO WORLD", key, cipher);
//
//            String encryptedString = new String(encryptedBytes);
//
//            System.out.println(encryptedString);
//
//            String decryptedString = myDiaryCipher.decryptString(encryptedBytes, key, cipher);
//
//            System.out.println(decryptedString);
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//    }

}
