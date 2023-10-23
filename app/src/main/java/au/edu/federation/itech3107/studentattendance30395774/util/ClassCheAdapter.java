package au.edu.federation.itech3107.studentattendance30395774.util;


import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.Constant;
import au.edu.federation.itech3107.studentattendance30395774.R;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.ClassBean;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 *
 * @author
 * @date 2023/10/19
 * @description
 */

public class ClassCheAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {

    public ClassCheAdapter(int layoutResId, @Nullable List<ClassBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassBean item) {
        helper.setText(R.id.tv_item,item.getName());
        CheckBox view = helper.getView(R.id.cb);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!Constant.select_class.contains(item.getId()+",")) {
                        Constant.select_class = Constant.select_class + item.getId() + ",";
                        Log.e("hao", "选中: "+Constant.select_class);
                    }
                }else {
                    if (Constant.select_class.contains(item.getId()+",")) {
                        String[] split = Constant.select_class.split(",");
                        String newDes = "";
                        for (String i : split){
                            if (!StringUtil.isEmpty(i)){
                                int i1 = Integer.parseInt(i);
                                if (item.getId() != i1){
                                    newDes = newDes + i1+",";
                                }
                            }
                        }
                        Constant.select_class = newDes;
                        Log.e("hao", "不选中: "+Constant.select_class);
                    }
                }
            }
        });
        view.setChecked(false);
        String selectClass = Constant.select_class;
        if (!StringUtil.isEmpty(selectClass)) {
            String[] split = selectClass.split(",");
            for (String s : split) {
                if (!StringUtil.isEmpty(s)) {
                    if ((s+",").equals(item.getId()+",")) {
                        view.setChecked(true);
                    }
                }
            }
        }
    }
}