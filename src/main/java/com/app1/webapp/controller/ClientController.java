package com.app1.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app1.model.FileInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app1.model.Artist;
import com.app1.service.AlbumManager;
import com.app1.service.ArtistManager;
import com.app1.service.ClassifyManager;
import com.app1.util.AESUtils;
import com.app1.util.Pager;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping("/client/artistForm*")
    public Map filesUpload(Artist artist, HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        String savePath = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.DAY_OF_YEAR);
        Map<String, String> map = new HashMap();
        String msg = "this is 来个中文 test!";


        map.put("errorMsg", msg);
        return map;
    }


    @RequestMapping("client/img")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) {

        String fileName = request.getParameter("url");

        try {
            fileName = AESUtils.decrypt(fileName);
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
