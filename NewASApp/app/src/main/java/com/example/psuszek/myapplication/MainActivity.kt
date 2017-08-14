package com.example.psuszek.myapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife


class MainActivity : Activity() {

    //val title: TextView by bindView(R.id.text1)

    var aa: Button?=null

    //@BindView(R.id.text1) public var title: TextView? = null

    @BindView(R.id.text1)
    lateinit var title2 : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        savedInstanceState.toString()

        ButterKnife.bind(this)

        //title = findViewById(R.id.seekBar1)

        title2.setText("kkk");

//        title2.setOnClickListener(new OnClickListener(){
//
//        })



    }
}
