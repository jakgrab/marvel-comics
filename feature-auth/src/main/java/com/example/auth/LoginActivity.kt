package com.example.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.auth.databinding.ActivityLoginBinding
import com.example.auth.utils.navigateToActivity
import com.example.core.constants.Constants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        installSplashScreen()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        isUserSignedIn(currentUser)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_login)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun isUserSignedIn(currentUser: FirebaseUser?) {
        if (currentUser!=null) {
            this.navigateToActivity(Constants.MAIN_ACTIVITY_PATH)
        }
    }

}