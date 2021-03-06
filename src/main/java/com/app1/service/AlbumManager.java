package com.app1.service;

import java.util.Map;

import com.app1.model.Album;
import com.app1.util.Pager;

public interface AlbumManager {

	
	public Pager getAlbums(int pageNo, int pageSize, Map map);
	
	public Album saveAlbum(Album album);
	
	public Album getAlbum(long id);
	
	public void removeAlbum(long id);
}
