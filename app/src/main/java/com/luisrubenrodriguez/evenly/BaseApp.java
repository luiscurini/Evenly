package com.luisrubenrodriguez.evenly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.luisrubenrodriguez.evenly.deps.DaggerDependencies;
import com.luisrubenrodriguez.evenly.deps.Dependencies;

/**
 * Created by GamingMonster on 22.05.2017.
 * Base class that other activities should implement.
 */

public class BaseApp extends AppCompatActivity {
    Dependencies mDependencies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDependencies = DaggerDependencies.builder().build();
    }

    public Dependencies getDependencies() {
        return mDependencies;
    }

}
