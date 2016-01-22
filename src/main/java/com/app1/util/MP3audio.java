package com.app1.util;

import java.io.File;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;

public class MP3audio {
	public static int getMp3TrackLength(File mp3File) {
		try {
			MP3Info info = new MP3Info(mp3File);
			info.setCharset("GB2312");
			System.out.println(info.getSongName());
			System.out.println(info.getArtist());
			System.out.println(info.getAlbum());
			System.out.println(info.getYear());
			System.out.println(info.getComment());
			return 1;	
		} catch(Exception e) {
			return -1;
		}
	}
	public static void main(String[] args) {
		File [] dirs = new File[] {
//				new File("F:/myvoices"),
				new File("G:\\儿歌")
		};
		
		for(File dir : dirs) {
			File [] fs = dir.listFiles();
			for(File file : fs) {
				int length = MP3audio.getMp3TrackLength(file);
				if(length <= 0) {
					System.err.println("###出错" + file.getName() + "=" + MP3audio.getMp3TrackLength(file));	
				} else {
					System.out.println(file.getName() + "=" + MP3audio.getMp3TrackLength(file));
				}
				
			}
		}

	}

}
