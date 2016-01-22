package com.app1.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;


/**
 * »ñµÃMP3ÎÄ¼þµÄÐÅÏ¢
 * 
 */
public class MP3Info {
	
	public static void main(String[] args) {
		//TODO ÑÝÊ¾
		File MP3FILE = new File("test.mp3");
		try {
			MP3Info info = new MP3Info(MP3FILE);
			info.setCharset("UTF-8");
			System.out.println(info.getSongName());
			System.out.println(info.getArtist());
			System.out.println(info.getAlbum());
			System.out.println(info.getYear());
			System.out.println(info.getComment());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private String charset = "utf-8";//½âÎöMP3ÐÅÏ¢Ê±ÓÃµÄ×Ö·û±àÂë
	
	private byte[] buf;//MP3µÄ±êÇ©ÐÅÏ¢µÄbyteÊý×é
	
	/**
	 * ÊµÀý»¯Ò»¸ö»ñµÃMP3ÎÄ¼þµÄÐÅÏ¢µÄÀà
	 * @param mp3 MP3ÎÄ¼þ
	 * @throws IOException ¶ÁÈ¡MP3³ö´í»òÔòMP3ÎÄ¼þ²»´æÔÚ
	 */
	public MP3Info(File mp3) throws IOException{
		
		buf = new byte[128];//³õÊ¼»¯±êÇ©ÐÅÏ¢µÄbyteÊý×é
		
		RandomAccessFile raf = new RandomAccessFile(mp3, "r");//Ëæ»ú¶ÁÐ´·½Ê½´ò¿ªMP3ÎÄ¼þ
		raf.seek(raf.length() - 128);//ÒÆ¶¯µ½ÎÄ¼þMP3Ä©Î²
		raf.read(buf);//¶ÁÈ¡±êÇ©ÐÅÏ¢
		
		raf.close();//¹Ø±ÕÎÄ¼þ
		
		if(buf.length != 128){//Êý¾ÝÊÇ·ñºÏ·¨
			throw new IOException("MP3±êÇ©ÐÅÏ¢Êý¾Ý³¤¶È²»ºÏ·¨!");
		}
		
		if(!"TAG".equalsIgnoreCase(new String(buf,0,3))){//ÐÅÏ¢¸ñÊ½ÊÇ·ñÕýÈ·
			throw new IOException("MP3±êÇ©ÐÅÏ¢Êý¾Ý¸ñÊ½²»ÕýÈ·!");
		}
		
	}

	/**
	 * »ñµÃÄ¿Ç°½âÎöÊ±ÓÃµÄ×Ö·û±àÂë
	 * @return Ä¿Ç°½âÎöÊ±ÓÃµÄ×Ö·û±àÂë
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * ÉèÖÃ½âÎöÊ±ÓÃµÄ×Ö·û±àÂë
	 * @param charset ½âÎöÊ±ÓÃµÄ×Ö·û±àÂë
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getSongName(){
		try {
			return new String(buf,3,30,charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf,3,30).trim();
		}
	}
	
	public String getArtist(){
		try {
			return new String(buf,33,30,charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf,33,30).trim();
		}
	}
	
	public String getAlbum(){
		try {
			return new String(buf,63,30,charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf,63,30).trim();
		}
	}
	
	public String getYear(){
		try {
			return new String(buf,93,4,charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf,93,4).trim();
		}
	}
	
	public String getComment(){
		try {
			return new String(buf,97,28,charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf,97,28).trim();
		}
	}
	
	
}

