package com.swjtu.api;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MD5
 * @Description TODO
 */
public class MD5 {

    // First initialize a character array to store each hexadecimal character
    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /**
     * Get the MD5 value of a string
     *
     * @param input Input string
     * @return MD5 value of the input string
     *
     */
    public static String md5(String input) {
        if (input == null)
            return null;

        try {
            // Get an MD5 converter (if you want the SHA1 parameter to "SHA1")
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // Input string is converted to byte array
            byte[] inputByteArray = input.getBytes("utf-8");
            // inputByteArray is the byte array obtained by input string conversion
            messageDigest.update(inputByteArray);
            // Converts and returns the result, which is also a byte array containing 16 elements
            byte[] resultByteArray = messageDigest.digest();
            // Convert character array to string
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Get the MD5 value of a file
     *
     * @param file
     * @return
     */
    public static String md5(File file) {
        try {
            if (!file.isFile()) {
                System.err.println("file" + file.getAbsolutePath() + "Does not exist or is not a file");
                return null;
            }

            FileInputStream in = new FileInputStream(file);

            String result = md5(in);

            in.close();

            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5(InputStream in) {

        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, read);
            }

            in.close();

            String result = byteArrayToHex(messagedigest.digest());

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new a character array, this is used to form the result string (
        //To explain: a byte is an 8-bit binary, which is 2 hexadecimal characters (the 8th power of 2 is equal to the 2nd power of 16)
        char[] resultCharArray = new char[byteArray.length * 2];
        // Traverse the byte array, and convert it into characters and put them into the character array by bit operation (bit operation is efficient)
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // Character array is returned as a string
        return new String(resultCharArray);

    }

}
