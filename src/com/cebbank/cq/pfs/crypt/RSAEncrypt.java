package com.cebbank.cq.pfs.crypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAEncrypt {
	public static final String KEY_ALGORITHM = "RSA";   
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	/**
	 * 私钥
	 */
	private RSAPrivateKey privateKey;

	/**
	 * 公钥
	 */
	private RSAPublicKey publicKey;
	
	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	

	/**
	 * 获取私钥
	 * @return 当前的私钥对象
	 */
	private RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * @return 当前的公钥对象
	 */
	private RSAPublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * 随机生成密钥对
	 */
	private void genKeyPair(){
		KeyPairGenerator keyPairGen= null;
		try {
			keyPairGen= KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair= keyPairGen.generateKeyPair();
		this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
		this.publicKey= (RSAPublicKey) keyPair.getPublic();
	}

	/**
	 * 从文件中输入流中加载公钥
	 * @param in 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	private void loadPublicKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}


	/**
	 * 从字符串中加载公钥
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	private void loadPublicKey(String publicKeyStr) throws Exception{
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
			this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
			
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * @param keyFileName 私钥文件名
	 * @return 是否成功
	 * @throws Exception 
	 */
	private void loadPrivateKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	private void loadPrivateKey(String privateKeyStr) throws Exception{
		try {
			BASE64Decoder base64Decoder= new BASE64Decoder();
			byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}
	private String getStringPublicKey(){
		byte [] bts = this.publicKey.getEncoded();
		BASE64Encoder ecode = new BASE64Encoder();
		return ecode.encodeBuffer(bts);
	}
	private String getStringPrivateKey(){
		byte [] bts = this.privateKey.getEncoded();
		BASE64Encoder ecode = new BASE64Encoder();
		return ecode.encodeBuffer(bts);
	}

	/**
	 * 公钥加密过程
	 * @param publicKey 公钥
	 * @param plainTextData 明文数据
	 * @return
	 * @throws Exception 加密过程中的异常信息
	 */
	private String encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
		if(publicKey== null){
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			//超过117字节报错，分段加密
			byte[] enbyte=null;
			StringBuilder sb=new StringBuilder();
			for(int i=0;i<plainTextData.length;i+=64){
				byte[] output= cipher.doFinal(ArrayUtils.subarray(plainTextData,i,i+64));				
				enbyte=ArrayUtils.addAll(enbyte, output);
			}
				
			BASE64Encoder encode = new BASE64Encoder();
			return encode.encodeBuffer(enbyte);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥解密过程
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	private byte[]  decrypt(RSAPrivateKey privateKey, String codeCipherData) throws Exception{
		if (privateKey== null){
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			BASE64Decoder decode = new BASE64Decoder();
			//分段解密
			byte[] debyte=null;
			byte[] data=decode.decodeBuffer(codeCipherData);
			for(int i=0;i<data.length;i+=128){
				byte[] output= cipher.doFinal(ArrayUtils.subarray(data,i,i+128));
				debyte=ArrayUtils.addAll(debyte, output);
			}
			
			return debyte;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	private static String byteArrayToCode(byte[] data){
		BASE64Encoder encode = new BASE64Encoder();
		return encode.encodeBuffer(data);
	}
	private static byte[] codeToByteArray(String data) throws IOException{
		BASE64Decoder decode = new BASE64Decoder();
		return decode.decodeBuffer(data);
	}
	/**
	 * 字节数据转十六进制字符串
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	private static String byteArrayToString(byte[] data){
		StringBuilder stringBuilder= new StringBuilder();
		for (int i=0; i<data.length; i++){
			//取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
			//取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i<data.length-1){
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}
	private static String enCode(String str,String pubKey){
		RSAEncrypt rsa = new RSAEncrypt();
		try {
			rsa.loadPublicKey(pubKey);
			return rsa.encrypt(rsa.getPublicKey(),str.getBytes(Constants.XML_CHARSET));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据加密失败");
		}		
	}

	public static String enCode(String str){
		return enCode(str,pubKey);
	}

	private static String deCode(String str,String prvKey){
		
		RSAEncrypt rsa = new RSAEncrypt();
		try {
			rsa.loadPrivateKey(prvKey);
			return new String(rsa.decrypt(rsa.getPrivateKey(),str),Constants.XML_CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据加密失败");
		}
	}

	public static String deCode(String str){
		return deCode(str,prvKey);
	}

	/**
	 * 签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	 private static String sign(String data, String privateKey) {
		 try {
			 byte[] keyBytes =decode(privateKey);
		        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);		        
		        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);		        
		        signature.initSign(privateK);
		        signature.update(data.getBytes(Constants.XML_CHARSET));
//System.out.println("data="+data);
//System.out.println("decode data="+decode(data));
		        BASE64Encoder encode = new BASE64Encoder();
		        return encode.encodeBuffer(signature.sign());
//		        return encode(signature.sign());
		 } catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("签名失败");
		 }
	 }

	 public static String sign(String data){
	 	return sign(data,prvKey);
	 }

	 /**
	  * 验证
	  * @param data
	  * @param publicKey
	  * @param sign
	  * @return
	  * @throws Exception
	  */
	 private static boolean verify(String data, String publicKey, String sign) {
	  	 try {
	    		 byte[] keyBytes = decode(publicKey);
	    	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
	    	        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    	        PublicKey publicK = keyFactory.generatePublic(keySpec);
			        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);			        
	    	        signature.initVerify(publicK);
	    	        signature.update(data.getBytes(Constants.XML_CHARSET));
	    	        return signature.verify(decode(sign));
	    	 } catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("验证签名失败");
			 }
	    }

	private static byte[] decode(String base64) throws Exception {
	        return Base64.decodeBase64(base64.getBytes());
	    }

	private static String encode(byte[] bytes) throws Exception {
	        return new String(Base64.encodeBase64(bytes));
	    }

	private static void writeToFile(String filepath,String key){
	    	try {
	    	FileWriter fw=new FileWriter(filepath);
	    	BufferedWriter bw=new BufferedWriter(fw);
	    	bw.write(key);
	    	bw.flush();
	    	bw.close();
	    	fw.close();
	    	} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("写入文件出错！");
		    }
	    	
	    }

	private static RSAEncrypt rsaEncrypt;
	private static String pubKey;
	private static String prvKey;
	static {
		rsaEncrypt = new RSAEncrypt();
		try {
			rsaEncrypt.loadPrivateKey(new FileInputStream(new File("key/pkcs8_rsa_private_key.pem")));
			rsaEncrypt.loadPublicKey(new FileInputStream(new File("key/rsa_public_key.pem")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		pubKey = rsaEncrypt.getStringPublicKey();
		prvKey = rsaEncrypt.getStringPrivateKey();
	}

	public static void main(String[] args) throws Exception {
		/**
		RSAEncrypt rsaEncrypt = new RSAEncrypt();
		
		try {
			rsaEncrypt.loadPrivateKey(new FileInputStream(new File("key/pkcs8_rsa_private_key.pem")));
			rsaEncrypt.loadPublicKey(new FileInputStream(new File("key/rsa_public_key.pem")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String pubKey = rsaEncrypt.getStringPublicKey();		
		String prvKey=rsaEncrypt.getStringPrivateKey();
		 **/

		String xmlbody="<?xml version=\"1.0\" encoding=\"UTF-8\"?>< response><TranCode>2001</TranCode><body><SubscribeNo>认购业务编码</SubscribeNo><ReadyPayNo>预售许可证</ReadyPayNo><HouseId>房屋编号</HouseId><SubscribeAmt>认购金额</SubscribeAmt><ResAmount>剩余待缴金额</ResAmount><NoticePaymentDt>缴款通知书生成时间</NoticePaymentDt><BankNo>银行代码</BankNo><BankName>银行名称</BankName><SuperviseAcct>监管总账号</SuperviseAcct><SuperviseSubacct>监管子账号</SuperviseSubacct><HouseDesc>楼盘房屋描述</HouseDesc><BuyerName>购房者姓名</BuyerName><HouseTyp>房屋类型</HouseTyp><SubscribeStat>认购状态1-正常2-解除</ SubscribeStat ><LockStat>是否锁定0-正常 1-锁定</LockStat><PayInfoStat>缴款状态</PayInfoStat><MsgCode>交易结果代码</MsgCode><MsgDesc>结果说明</MsgDesc></body></ response>";

		String entext=RSAEncrypt.enCode(xmlbody, pubKey);//公钥加密
		System.out.println("密文："+entext);
		//System.out.println("私钥："+prvKey);

		String sign = RSAEncrypt.sign(xmlbody, prvKey);//私钥签名
		System.err.println("签名:" + sign);

		boolean status = RSAEncrypt.verify(xmlbody, pubKey, sign);//公钥验证签名
		System.err.println("验证:" + status);
		

		String detext=RSAEncrypt.deCode(entext, prvKey);//私钥解密
		System.out.println("原文："+detext);
	}
}
