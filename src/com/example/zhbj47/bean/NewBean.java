package com.example.zhbj47.bean;

import java.util.List;

public class NewBean {
	public String retcode;
	public NewBeanItem data;

	public class NewBeanItem {
		public String countcommenturl;
		public String more;
		public List<News> news;
		public String title;
		public List<Topic> topic;
		public List<TopNews> topnews;

	}

	//底部listview显示新闻的类
	public class News {
		public String comment;
		public String commentlist;
		public String commenturl;
		//新闻唯一标识，记录在本地，判断是否已读的新闻
		public String id;
		//图片url
		public String listimage;
		//时间
		public String pubdate;
		//新闻缩略内容
		public String title;
		public String type;
		//url关联想详情页请求地址 html--WebView（加载html网页）
		public String url;
		
		//标识当前条目新闻是否已读
		public boolean isRead;
	}

	public class Topic {
		public String description;
		public String id;
		public String listimage;
		public String sort;
		public String title;
		public String url;
	}

	public class TopNews {
		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		//图片
		public String topimage;
		public String pubdate;
		//标题
		public String title;
		public String type;
		//详情页url地址
		public String url;

	}
}
