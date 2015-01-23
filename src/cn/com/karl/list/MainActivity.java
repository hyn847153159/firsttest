package cn.com.karl.list;

import java.util.ArrayList;
import java.util.List;

import cn.com.karl.list.MyListView.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<String> data;
	private BaseAdapter adapter;
	private ProgressBar moreProgressBar;
	private MyListView listView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		data = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			data.add(String.valueOf(i));
		}

		listView = (MyListView) findViewById(R.id.listView);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item, null);
				TextView textView = (TextView) convertView
						.findViewById(R.id.textView_item);
				textView.setText(data.get(position));
				return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return data.get(position);
			}

			public int getCount() {
				return data.size();
			}
		};
		listView.setAdapter(adapter);
		// 添加listview底部获取更多按钮（可自定义）
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.footer, null);
		RelativeLayout footerView = (RelativeLayout) view
				.findViewById(R.id.list_footview);
		moreProgressBar = (ProgressBar) view.findViewById(R.id.footer_progress);
		listView.addFooterView(footerView);
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moreProgressBar.setVisibility(View.VISIBLE);
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
							for (int i = 0; i < 5; i++) {
								data.add("加载后的内容" + i);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
						moreProgressBar.setVisibility(View.GONE);
					}

				}.execute();
			}
		});
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						data.add(0, "刷新后的内容");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getApplicationContext(), "你点击了第" + arg2 + "行",
						Toast.LENGTH_SHORT).show();

			}
		});
	}
}