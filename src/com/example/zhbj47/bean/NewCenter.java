package com.example.zhbj47.bean;

import java.util.List;

public class NewCenter {
	public List<NewCenterItem> data;
	public List<String> extend;
	public String retcode;
	
	public class NewCenterItem{
		public List<Children> children;
		public String id;
		public String title;
		public String type;
		public String url;
		public String url1;
		public String dayurl;
		public String excurl;
		public String weekurl;
	}
	public class Children{
		public String id;
		public String title;
		public String type;
		public String url;
	}
}
