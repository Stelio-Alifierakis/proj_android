package be.heh.campus_technique.proj_android_s_alifierakis;

import java.security.MessageDigest;

/**
 * Created by steli on 07-01-17.
 */

public class Hash {

    public Hash(){
    }

    public String hashage(String message, String algotihme){
        String hash="";

        if(algotihme=="MD-5" || algotihme=="SHA-1"){

            try{
                MessageDigest md = MessageDigest.getInstance(algotihme);
                md.update(message.getBytes());
                byte[] hashByte=md.digest();

                char[] hexArray = "0123456789ABCDEF".toCharArray();

                StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < hashByte.length; j++) {
                    buffer.append(hexArray[(hashByte[j] >> 4) & 0x0f]);
                    buffer.append(hexArray[hashByte[j] & 0x0f]);
                }

                hash=buffer.toString();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        return hash;
    }
}
