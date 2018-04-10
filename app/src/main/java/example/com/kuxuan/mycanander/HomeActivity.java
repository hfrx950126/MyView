package example.com.kuxuan.mycanander;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView mListView;
    HomeAdapter mHomeAdapter;
    List<String> mList;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_home);
        mListView = findViewById(R.id.listview);
        mList = new ArrayList<>();
        mList.add("自定义圆");
        mList.add("模拟ViewPager");
        mList.add("自定义流布局");
        mList.add("粘性拖拽");
        mHomeAdapter = new HomeAdapter(this, mList);
        mListView.setAdapter(mHomeAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    intent = new Intent(HomeActivity.this, CircleActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    intent = new Intent(HomeActivity.this, FlowActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    intent = new Intent(HomeActivity.this, TouchActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
