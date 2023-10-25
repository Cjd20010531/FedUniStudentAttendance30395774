package au.edu.federation.itech3107.studentattendance30395774;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395774.util.TabViewPagerAdapter;

public class MainActivity extends AppCompatActivity {


    private List<AppCompatDialogFragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragments = new ArrayList<>();
        mFragments.add(new MainFragment());
        mFragments.add(new ClassFragment());
        ViewPager vp = findViewById(R.id.vp);
        TabLayout tab = findViewById(R.id.tab);
        String[] mTitle = new String[]{getResources().getString(R.string.course), getResources().getString(R.string.student)};
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitle);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);

        int type = getIntent().getIntExtra("type", 0);
        vp.setCurrentItem(type);
    }
}