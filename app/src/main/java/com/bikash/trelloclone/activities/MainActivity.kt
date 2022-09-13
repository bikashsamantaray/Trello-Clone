package com.bikash.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    var appbarMain:Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        appbarMain = findViewById(R.id.toolbar_main_activity)
        setContentView(binding?.root)


        //showErrorSnackBar("signed in successfully")
    }

    private fun setupActionBar(){
        setSupportActionBar(appbarMain)
        appbarMain?.setNavigationIcon(R.drawable.)

    }

    private fun setSupportActionBar(appbarMain: Toolbar?) {

    }


}

}