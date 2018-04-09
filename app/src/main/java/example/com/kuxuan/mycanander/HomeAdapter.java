package example.com.kuxuan.mycanander;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 *
 * @author huangfuruixin
 * @date 2018/4/9
 */

public class HomeAdapter extends BaseAdapter {
    Context context;

    public HomeAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    List<String> list;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout item = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.item_list_layout, parent,false);
        TextView textView = item.findViewById(R.id.textview);
        textView.setText(list.get(position));
        return item;
    }
}
