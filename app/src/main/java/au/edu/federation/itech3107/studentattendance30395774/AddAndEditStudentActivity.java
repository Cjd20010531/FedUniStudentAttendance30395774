package au.edu.federation.itech3107.studentattendance30395774;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3107.studentattendance30395774.databinding.ActivityAddStudentBinding;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.StudentBean;
import au.edu.federation.itech3107.studentattendance30395774.util.StringUtil;


public class AddAndEditStudentActivity extends AppCompatActivity {

    private ActivityAddStudentBinding bind;
    private int mId;
    private StudentBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAddStudentBinding.inflate(getLayoutInflater());
        mId = getIntent().getIntExtra("id", 0);
        setContentView(bind.getRoot());
        initView();
    }

    private void initView() {
        boolean a = getIntent().getBooleanExtra("add", false);
        if (!a) {
            bean = (StudentBean) getIntent().getSerializableExtra("bean");
            if (bean != null) {
                bind.etName.setText(bean.getName());
                bind.etNumber.setText(bean.getNumber()+"");
                bind.btnDelete.setVisibility(View.VISIBLE);
            }else
                bind.btnDelete.setVisibility(View.GONE);
        }else
            bind.btnDelete.setVisibility(View.GONE);
        bind.btnSubmit.setOnClickListener(v -> {
            if (StringUtil.isEmpty(bind.etName.getText().toString()) || StringUtil.isEmpty(bind.etNumber.getText().toString())) {
                Toast.makeText(AddAndEditStudentActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (a) {
                //新增
                StudentBean bean1 = new StudentBean();
                bean1.setName(bind.etName.getText().toString());
                bean1.setClassId(mId);
                bean1.setNumber(Long.parseLong(bind.etNumber.getText().toString()));
                DataBase.getInstance(AddAndEditStudentActivity.this).getStudentDao().insert(bean1);
                Toast.makeText(AddAndEditStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //修改
                bean.setName(bind.etName.getText().toString());
                bean.setNumber(Long.parseLong(bind.etNumber.getText().toString()));
                DataBase.getInstance(AddAndEditStudentActivity.this).getStudentDao().update(bean);
                Toast.makeText(AddAndEditStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        bind.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase.getInstance(AddAndEditStudentActivity.this).getStudentDao().delete(bean);
                Toast.makeText(AddAndEditStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
