package com.app1.util;



import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

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
	public static void main(String[] args) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
		File [] dirs = new File[] {
//				new File("F:/myvoices"),
				new File("E:/美国口音/chapter 2/")
		};
		
		for(File dir : dirs) {
			File [] fs = dir.listFiles();
			for(File file : fs) {
				MP3File f      = (MP3File) AudioFileIO.read(file);
				AudioHeader audioHeader = f.getAudioHeader();
				int length = audioHeader.getTrackLength();
				audioHeader.getSampleRateAsNumber();
				audioHeader.getChannels();
				audioHeader.isVariableBitRate();
				if(length <= 0) {
					System.err.println("###出错" + file.getName() + "=" + MP3audio.getMp3TrackLength(file));	
				} else {
					System.out.println(file.getName() + "=" + length + " " );
				}
				
			}
		}

	}

}
