package com.mkdev.testapplication

import android.os.Bundle
import androidx.navigation.findNavController
import com.mkdev.core_framework.base.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureNavController()
    }

    private fun configureNavController() {
        val navController = findNavController(R.id.nav_host_fragment)
        val navInflater = navController.navInflater
        navController.graph = navInflater.inflate(R.navigation.nav_graph_home_feature)
    }
}