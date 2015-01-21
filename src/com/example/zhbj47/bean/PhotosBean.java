package com.example.zhbj47.bean;

import java.util.List;

public class PhotosBean {

	public String retcode;
	public Photos data;
	
	public class Photos {

		public String countcommenturl;
		public String more;
		
		public List<PhotosItem> news;
		
		public String title;
		public List topic;
	}
	
	public class PhotosItem {

		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		public String largeimage;
		//图片链接地址
		public String listimage;
		public String pubdate;
		public String smallimage;
		//图片的配套文字
		public String title;
		public String type;
		public String url;
		
	}
}
