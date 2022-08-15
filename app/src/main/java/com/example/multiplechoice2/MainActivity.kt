package com.example.multiplechoice2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.multiplechoice2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var aboutFragment: Fragment? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController=navHostFragment.navController
        val builder= AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration=builder.build()
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController=findNavController(R.id.nav_host_fragment_container)
        if(item.itemId == android.R.id.home){
            onBackPressed();
            return true;
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) is AboutFragment && !isQuizFinished){
            val navController=findNavController(R.id.nav_host_fragment_container)
            navController.navigate(R.id.action_aboutFragment_to_quizFragment)
        }
        else{
            super.onBackPressed()
        }
    }


    companion object{
        var isQuizFinished=false
        var isQuizStarted=false
    }
}