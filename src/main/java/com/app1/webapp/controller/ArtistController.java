package com.app1.webapp.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.app1.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.app1.model.Artist;
import com.app1.service.ArtistManager;
import com.app1.util.Pager;

@Controller
public class ArtistController extends BaseFormController {

	@Autowired
	ArtistManager artistManager;

	@RequestMapping("/artistManage*")
	public String showPage(ModelMap model, HttpServletRequest request) {
		return "ArtistManage";
	}

	@ResponseBody
	@RequestMapping("/artists*")
	public Pager execute2(Artist artist, HttpServletRequest request, @RequestParam("page") int nowpage, @RequestParam("rows") int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		long albumId = artist.getAlbumId();
		map.put("albumId", albumId);

		Pager p = artistManager.getArtists(nowpage, rows, map);
		return p;
	}

	@ResponseBody
	@RequestMapping("/artistForm*")
	public Map filesUpload(Artist artist, HttpServletRequest request) {
		Calendar cal = Calendar.getInstance();
		String savePath = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.DAY_OF_YEAR);
		Map<String, String> map = new HashMap();
		String msg = null;

		try {

			if (request.getParameter("delete") != null) {
				artistManager.removeArtist(artist.getArtistId());
				saveMessage(request, getText("user.deleted", artist.getArtistName(), request.getLocale()));
				map.put("success", "1");
			} else {
				String fileName = "";
				FileInfo fileInfo = null;
				MultipartFile[] files = artist.getFiles();
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						MultipartFile file = files[i];
						if ("".equals(fileName)) {
							fileInfo = saveMP3File(file, savePath);
							fileName = fileInfo.getEncodeFileName();
						} else {
							fileInfo = saveMP3File(file, savePath);
							fileName = fileName + ";" + fileInfo.getEncodeFileName();
						}
					}
				}
				if (artist.getArtistId() > 0) {
					Artist old = artistManager.getArtist(artist.getArtistId());
					if(fileName != null){
						old.setArtistPath(fileName);
						old.setArtistTraceLength(fileInfo.getDuringTime());
					}
					old.setArtistName(artist.getArtistName());
					artistManager.saveArtist(old);
				} else {
					if(fileName != null) {
						artist.setArtistPath(fileName);
						artist.setArtistTraceLength(fileInfo.getDuringTime());
					}
//					System.out.println(fileInfo.getDuringTime()+ "=========");
					artistManager.saveArtist(artist);
				}
			}

		} catch (Exception e) {
			msg = e.getMessage();
		}
		map.put("errorMsg", msg);
		return map;
	}

}
