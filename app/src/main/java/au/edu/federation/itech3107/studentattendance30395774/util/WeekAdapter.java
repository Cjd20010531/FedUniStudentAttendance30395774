package au.edu.federation.itech3107.studentattendance30395774.util;


import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.R;

public class WeekAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectIndex = 2;

    public WeekAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_text, item);
        TextView view = helper.getView(R.id.tv_text);
        if (selectIndex == helper.getAdapterPosition()) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
    }

}
