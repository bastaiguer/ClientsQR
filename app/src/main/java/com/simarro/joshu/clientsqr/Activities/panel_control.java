package com.simarro.joshu.clientsqr.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Fragments.Mapa;
import com.simarro.joshu.clientsqr.Fragments.Tabla_Fragment;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.MyFragmentPagerAdapter;

public class panel_control extends FragmentActivity {

    private ViewPager pager = null;
    private TextView titulo;
    private MyFragmentPagerAdapter pagerAdapter = null;
    private TabLayout tabsTitulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_control);
        this.pager = this.findViewById(R.id.pager);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(Tabla_Fragment.newInstance());
        pagerAdapter.addFragment(Mapa.newInstance());
        this.pager.setAdapter(pagerAdapter);
        tabsTitulos = findViewById(R.id.tabLayout_titulos);
        tabsTitulos.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabsTitulos.setupWithViewPager(pager);
    }



    @Override
    public void onBackPressed() {
        if(this.pager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            this.pager.setCurrentItem(this.pager.getCurrentItem()-1);
        }
    }
}
