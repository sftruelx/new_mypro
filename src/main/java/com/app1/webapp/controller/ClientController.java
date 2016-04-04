package com.app1.webapp.controller;

import com.app1.model.Album;
import com.app1.model.Artist;
import com.app1.model.FileInfo;
import com.app1.service.AlbumManager;
import com.app1.service.ArtistManager;
import com.app1.service.ClassifyManager;
import com.app1.util.AES;
import com.app1.util.Pager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import com.app1.util.AESUtils;

@Controller
public class ClientController extends BaseFormController {
    private final Log log = LogFactory.getLog(ClientController.class);
    @Autowired
    ClassifyManager manager;

    @ResponseBody
    @RequestMapping("/client/test*")
    public ReturnData execute(HttpServletRequest request) {
        String parentId = request.getParameter("parentID");
        Long id = null;
        if (parentId != null) {
            id = Long.valueOf(parentId);
        } else {
            id = new Long(1);
        }
/*
        Enumeration names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			System.out.println("<b>" + name + ":</b>" + request.getHeader(name) + "<br />");
			if (name.equals("user-agent")) {
				System.out.println("<a href='#'>" + name + ":" + request.getHeader(name) + "</a><br />");
			}
			System.err.println(name + ":" + request.getHeader(name) + "");

		}*/
        ReturnData rd = new ReturnData();
        rd.setData(manager.getParent(id));
        rd.setCode(1);
        rd.setTxt("OK");
        return rd;
    }

    @Autowired
    AlbumManager albumManager;

    @ResponseBody
    @RequestMapping("/client/album*")
    public Pager getAlbum(HttpServletRequest request, @RequestParam("page") int nowpage, @RequestParam("rows") int rows) {
        String type = request.getParameter("type");
        Map<String, Object> map = new HashMap<String, Object>();
        return albumManager.getAlbums(nowpage, rows, map);

    }

    @Autowired
    ArtistManager artistManager;

    @ResponseBody
    @RequestMapping("/client/artist*")
    public Pager getArtist(Artist artist, @RequestParam("page") int nowpage, @RequestParam("rows") int rows) {
        Map<String, Object> map = new HashMap<String, Object>();
        long albumId = artist.getAlbumId();
        map.put("albumId", albumId);
        Pager p = artistManager.getArtists(nowpage, rows, map);
        return p;
    }


    @Value("#{configProperties['albumroot']}")
    String rootPath;


    @ResponseBody
    @RequestMapping("/client/albumForm*")
    public long filesUpload(Album album, HttpServletRequest request) {
        long result = 0L;
        Calendar cal = Calendar.getInstance();
        String savePath = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.DAY_OF_YEAR);
        Map<String, String> map = new HashMap();
        String msg = null;

        try {

            if (request.getParameter("delete") != null) {
                albumManager.removeAlbum(album.getId());
                saveMessage(request, getText("user.deleted", album.getAlbumName(), request.getLocale()));
                map.put("success", "1");
            } else {
                String fileName = "";
                MultipartFile[] files = album.getFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];
                        if ("".equals(fileName)) {
                            fileName = saveFile(file, savePath);
                        } else {
                            fileName = fileName + ";" + saveFile(file, savePath);
                        }
                    }
                }
                if (album.getId() > 0) {
                    Album old = null;
                    old = albumManager.getAlbum(album.getId());
                    if (fileName != null) {
                        old.setImgPath(fileName);
                    }
                    old.setAlbumName(album.getAlbumName());
                    old.setAuthor(album.getAuthor());
                    old.setDescripe(album.getDescripe());
                    old.setPublishDate(new Date());
                    old.setCreateTime(new Date());
                    albumManager.saveAlbum(old);
                } else {

                    album.setImgPath(fileName);
                    album.setCreateTime(new Date());
                    album.setPublishDate(new Date());
                    Album a = albumManager.saveAlbum(album);
                    result = a.getId();
                }
            }

        } catch (Exception e) {
            msg = e.getMessage();
        }
        map.put("errorMsg", msg);
        return result;
    }

    @ResponseBody
    @RequestMapping("/client/artistForm*")
    public String filesUpload(Artist artist, HttpServletRequest request) {
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
        return "OK";
    }

    @RequestMapping("client/img")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) {

        String fileName = request.getParameter("url");

        try {
            fileName = AES.decrypt2Str(fileName, AES.password);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        log.info("dowload img" + fileName);
        response.setContentType("multipart/form-data");
        String prefix = fileName.substring(fileName.lastIndexOf("/") + 1);
        response.setHeader("Content-Disposition", "attachment;fileName=" + prefix);
        ServletOutputStream out;
        File file = new File(rootPath + fileName);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            inputStream.close();
            out.close();
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ReturnData {
        private int code;
        private String txt;
        private Object data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }


    }
}
