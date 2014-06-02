package net.jma.clases;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Seguridad {

	static String seed = "xxxxxxxxxxxx";
    static String c_strKey = "----------------";
    static String c_strIV = "00000000";
	
	public static String encrypt(String cleartext) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}
	
	public static String decrypt(String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
	    kgen.init(128, sr); // 192 and 256 bits may not be available
	    SecretKey skey = kgen.generateKey();
	    byte[] raw = skey.getEncoded();
	    return raw;
	}

	
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}
	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}
	
	public static byte[] toByte(String hexString) {
		int len = hexString.length()/2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2*buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	}
	
	public static String Hash(String text) throws Exception {
	    MessageDigest md;
	    md = MessageDigest.getInstance("SHA-1");
	    byte[] sha1hash = new byte[40];
	    md.update(text.getBytes("UTF-8"), 0, text.length());
	    sha1hash = md.digest();
	    String converted = Seguridad.toHex(sha1hash);
	    return converted;
	}
	
    public static String damePalabra(String usuario, String pass) throws Exception
    {
        String ret = "";
        try
        {
            String c_ofusc = "0F;00;07;77;75;05;76;77;74;73;0A;01;03;7C;05;0E;0B;13;13;1D";
        	
            //'Declaración de variables
            byte[] arrClaveOfuscadoraLongBytes = null;
            byte[] arrPalabraSecOfuscBytes;
            byte[] arrPalabraSecretaBytes;
            String strSalida;
            int intIndice;

            //'Inicialización de variables            
            String claveOfuscadoraLong = usuario + pass.substring(0, 9) + "***";

            //'Convertir a array de bytes la clave ofuscadora (clave xor)
            arrClaveOfuscadoraLongBytes = claveOfuscadoraLong.getBytes("UTF-8"); //Seguridad.toByte(claveOfuscadoraLong);

            //'Convertir a array de bytes la clave secreta a la que se le realiza el xor
            arrPalabraSecOfuscBytes = Seguridad.hexStringToByteArray(c_ofusc, ";");

            //'Realizar el xor (llevando el resultado a un array de bytes)
            arrPalabraSecretaBytes = Seguridad.xorArrayBytes(arrClaveOfuscadoraLongBytes, arrPalabraSecOfuscBytes);

            //'Convertir a cadena el resultado (que estaba en forma de bytes)
            strSalida = "";
            for(intIndice = 0; intIndice <= arrPalabraSecretaBytes.length -1; intIndice++){
                strSalida = strSalida + (char)arrPalabraSecretaBytes[intIndice];
            }

            ret = strSalida;
        }
        catch (Exception ex)
        {
            throw ex;
        }
        return ret;
    }
    
    public static byte[] hexStringToByteArray(String lista, String separador){
    	lista = lista.replaceAll(separador, "");
    	int len = lista.length();
    	byte[] data = new byte[len / 2];
    	for (int i = 0; i < len; i += 2) {
    		data[i / 2] = (byte) ((Character.digit(lista.charAt(i), 16) << 4)
                             + Character.digit(lista.charAt(i+1), 16));
    	}
    	return data;
    }    
	
    public static byte[] xorArrayBytes(byte[] operador1, byte[] operador2) throws Exception{

        byte[] ret;

        //'Declaración de variables
        byte[] arrSalida = null;
        int intElementos;
        int intIndice;

        //'Validar tamaño de los arrays sobre los que se va a hacer el xor
        if (operador1.length > operador2.length){
            throw new Exception("El operador1 es de longitud mayor que el operador2");
        }

        //'Realizar el xor (a nivel de bytes) sobre los arrays argumento
        intElementos = operador1.length;
        arrSalida = new byte[intElementos];

        for (intIndice = 0; intIndice <= intElementos-1; intIndice++){
            arrSalida[intIndice] = (byte)(operador1[intIndice] ^ operador2[intIndice]);
        }

        //'Devolver como resultado de la función el nuevo array (de bytes) en el que se
        //'ha efectuado el xor de los argumentos
        ret = arrSalida;

        return ret;

    }    
    
    public static String Desencriptar(String cadena) throws Exception {
		try{
			byte[] keyB = new byte[24]; // a Triple DES key is a byte[24] array
			for (int i = 0; i < c_strKey.length() && i < keyB.length; i++) {
				keyB[i] = (byte) c_strKey.charAt(i);
			}
			byte[] ivB = new byte[8];
			for (int i = 0; i < c_strIV.length() && i < ivB.length; i++){
				ivB[i] = (byte) c_strIV.charAt(i);
			}
			// Make the Key
			SecretKey key = new SecretKeySpec(keyB, "DESede");			
			IvParameterSpec ivspec = new IvParameterSpec(ivB);
						
		    InputStream in = new ByteArrayInputStream(Seguridad.hexStringToByteArray(cadena, ""));
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
			// Create and initialize the decryption engine
    	    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    	    //cipher.init(Cipher.DECRYPT_MODE, key);
    	    cipher.init(Cipher.DECRYPT_MODE, key, ivspec);

    	    // Read bytes, decrypt, and write them out.
    	    byte[] buffer = new byte[2048];
    	    int bytesRead;
    	    while ((bytesRead = in.read(buffer)) != -1) {
    	      out.write(cipher.update(buffer, 0, bytesRead));
    	    }

    	    // Write out the final bunch of decrypted bytes
    	    out.write(cipher.doFinal());
    	    out.flush();
			
    	    String s = out.toString("UTF-8");
    	    return s;
    	    
		}catch (Exception ex){
			throw ex;
		}
    }
    
    
    public static String Encriptar(String cadena) throws Exception {
		byte[] keyB = new byte[24]; // a Triple DES key is a byte[24] array
		for (int i = 0; i < c_strKey.length() && i < keyB.length; i++) {
			keyB[i] = (byte) c_strKey.charAt(i);
		}
		byte[] ivB = new byte[8];
		for (int i = 0; i < c_strIV.length() && i < ivB.length; i++){
			ivB[i] = (byte) c_strIV.charAt(i);
		}
		// Make the Key
		SecretKey key = new SecretKeySpec(keyB, "DESede");		
		IvParameterSpec ivspec = new IvParameterSpec(ivB);
    	
//		DESede/CBC/NoPadding (168)
//		DESede/CBC/PKCS5Padding (168)
//		DESede/ECB/NoPadding (168)
//		DESede/ECB/PKCS5Padding (168)		
		
		InputStream in = new ByteArrayInputStream(cadena.getBytes("UTF-8"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    // Create and initialize the encryption engine
	    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	    //cipher.init(Cipher.ENCRYPT_MODE, key);
	    cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
	    

	    // Create a special output stream to do the work for us
	    CipherOutputStream cos = new CipherOutputStream(out, cipher);

	    // Read from the input and write to the encrypting output stream
	    byte[] buffer = new byte[2048];
	    int bytesRead;
	    while ((bytesRead = in.read(buffer)) != -1) {
	      cos.write(buffer, 0, bytesRead);
	    }
	    cos.close();

	    String s = Seguridad.toHex(out.toByteArray()); // out.toString("UTF-8");
	    return s;
	    
	  }
    
}

