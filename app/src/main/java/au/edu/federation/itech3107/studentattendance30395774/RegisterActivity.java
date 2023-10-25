package au.edu.federation.itech3107.studentattendance30395774;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.databinding.ActivityRegisterBinding;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.DataBase;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.UserBean;
import au.edu.federation.itech3107.studentattendance30395774.sqlite.UserDao;
import au.edu.federation.itech3107.studentattendance30395774.util.StringUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private int au = 1;
    private ActivityRegisterBinding inflate;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        userDao = DataBase.getInstance(this).getUserDao();
        initView();
    }

    private void initView() {
        //Sign in
        inflate.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify that the input box is empty
                if (!checkNull()){
                    return;
                }
               //Sign in
                register();
            }
        });

    }

    private void register() {
        //Check whether the account is the same
        userDao.getUserByName(inflate.etAccount.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<UserBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<UserBean> list) {
                        //Query result
                        if (list != null && list.size() != 0) {
                            //If the account is repeated, prompt is displayed
                            Toast.makeText(RegisterActivity.this, getString(R.string.accountNotice), Toast.LENGTH_SHORT).show();
                        } else {
                            //Save data
                            insertData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //No result found
                        insertData();
                    }
                });
    }

    private boolean checkNull() {
        if (StringUtil.isEmpty(inflate.etNick.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.nick) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(inflate.etAccount.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.account) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(inflate.etPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(inflate.etPassword2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!inflate.etPassword.getText().toString().equals(inflate.etPassword2.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_not_again), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtil.isEmpty(inflate.etMobile.getText().toString())) {
            Toast.makeText(RegisterActivity.this, getString(R.string.mobile) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void insertData() {
        //Database insert data
        UserBean userBean = new UserBean();
        userBean.setName(inflate.etAccount.getText().toString());
        userBean.setPwd(inflate.etPassword.getText().toString());
        userBean.setNick(inflate.etNick.getText().toString());
        userBean.setMobile(inflate.etMobile.getText().toString());
        userBean.setAuthon(au);
        userDao.insert(userBean);
        Toast.makeText(RegisterActivity.this, getString(R.string.registerNotice), Toast.LENGTH_SHORT).show();
        finish();
    }

}