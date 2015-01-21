package com.example.zhbj47.pager;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj47.R;
import com.example.zhbj47.adapter.HMAdapter;
import com.example.zhbj47.bean.NewCenter.NewCenterItem;
import com.example.zhbj47.bean.PhotosBean;
import com.example.zhbj47.bean.PhotosBean.PhotosItem;
import com.example.zhbj47.utils.GsonUtils;
import com.example.zhbj47.utils.HMApi;
import com.example.zhbj47.utils.ImageCacheUtils;
import com.example.zhbj47.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PicPager extends BasePager {

	private NewCenterItem newCenterItem;
	private boolean isListView = true;
	private ImageCacheUtils cacheUtils;
	private PhotosBean photosBean;
	private MyAdapter myAdapter;

	@ViewInject(R.id.lv)
	private ListView lv;

	@ViewInject(R.id.gv)
	private GridView gv;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ImageCacheUtils.SUCCESS:
				// 将下载的图片显示到控件上去做处理
				Bitmap bitmap = (Bitmap) msg.obj;
				// 通过tag找到listview上的子控件
				ImageView imageView = (ImageView) lv.findViewWithTag(msg.arg1);
				if (null != bitmap && null != imageView) {
					imageView.setImageBitmap(bitmap);
				}
				break;
			case ImageCacheUtils.FAIL:
				Toast.makeText(context, "图片获取失败", 0).show();
				break;
			}
		};
	};

	public PicPager(Context context, NewCenterItem newCenterItem) {
		super(context);
		this.newCenterItem = newCenterItem;
		cacheUtils = new ImageCacheUtils(context, handler);
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.layout_pic, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		//请求数据的操作
		String result = SharedPreferencesUtil.getStringData(context, HMApi.PHOTOS_URL, "");
		if(!TextUtils.isEmpty(result)){
			processData(result);
		}
		getData();
	}

	private void getData() {
		requestData(HttpMethod.GET, HMApi.PHOTOS_URL, null, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				SharedPreferencesUtil.saveStringData(context, HMApi.PHOTOS_URL, responseInfo.result);
				processData(responseInfo.result);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}

	private void processData(String result) {
		photosBean = GsonUtils.json2Bean(result, PhotosBean.class);
		if(myAdapter == null){
			myAdapter = new MyAdapter(photosBean.data.news, context);
			lv.setAdapter(myAdapter);
		}else{
			myAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 切换图片显示方式
	 * 
	 * @param imgbtn_text
	 */
	public void switchListViewOrGridView(ImageButton imgbtn_text) {
		if (isListView) {
			// 转换成gridView
			isListView = false;
			imgbtn_text.setImageResource(R.drawable.icon_pic_grid_type);
			lv.setVisibility(View.GONE);
			gv.setVisibility(View.VISIBLE);
			gv.setAdapter(new MyAdapter(photosBean.data.news, context));
		} else {
			// 转换ListView
			isListView = true;
			imgbtn_text.setImageResource(R.drawable.icon_pic_list_type);
			lv.setVisibility(View.VISIBLE);
			gv.setVisibility(View.GONE);
			lv.setAdapter(new MyAdapter(photosBean.data.news, context));
		}
	}

	/**
	 * 适配器
	 * 
	 * @author rong
	 */
	class MyAdapter extends HMAdapter<PhotosItem> {
		public MyAdapter(List<PhotosItem> list, Context context) {
			super(list, context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = view.inflate(context, R.layout.layout_pic_item,
						null);

				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.tv = (TextView) convertView.findViewById(R.id.tv);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(list.get(position).title);
			holder.image.setImageResource(R.drawable.pic_item_list_default);
			holder.image.setTag(position);

			Bitmap imageBitmap = cacheUtils.getImageBitmap(
					list.get(position).listimage, position);
			if (imageBitmap != null) {
				holder.image.setImageBitmap(imageBitmap);
			}

			return convertView;
		}
	}

	/**
	 * 家庭组
	 * 
	 * @author rong
	 */
	class ViewHolder {
		ImageView image;
		TextView tv;
	}

}
