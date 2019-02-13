package com.simarro.joshu.clientsqr.Activities;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Fragments.Tabla_Fragment;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.MyFragmentPagerAdapter;

import java.util.ArrayList;

public class graficos extends FragmentActivity {

    private ViewPager pager = null;
    private TextView titulo;
    private MyFragmentPagerAdapter pagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);
        //this.getLayoutInflater().inflate(R.layout.fragment_pantallas,(RelativeLayout)findViewById(R.id.contenedor_viewPager),true);
        this.pager = this.findViewById(R.id.pager);
        titulo = findViewById(R.id.text_titulo_fragment);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(Tabla_Fragment.newInstance());
        pagerAdapter.addFragment(Tabla_Fragment.newInstance());
        this.pager.setAdapter(pagerAdapter);
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
