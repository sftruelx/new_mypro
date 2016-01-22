package com.app1.model;

public class FileInfo {

	private String realFileName;
	
	private String newFileName;
	
	private String encodeFileName;
	
	private String realFilePath;
	
	private String prefix;
	
	private int duringTime;
	
	private String title;

	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	public String getRealFilePath() {
		return realFilePath;
	}

	public void setRealFilePath(String realFilePath) {
		this.realFilePath = realFilePath;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(int duringTime) {
		this.duringTime = duringTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEncodeFileName() {
		return encodeFileName;
	}

	public void setEncodeFileName(String encodeFileName) {
		this.encodeFileName = encodeFileName;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	
	
	
}
