package com.example.finmate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.finmate.ui.theme.FinMateTheme
import com.example.finmate.utils.AnalyticsHelper
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class AuthActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnalyticsHelper.logEvent("auth_screen_viewed")

        // Initialize Google Sign-In Client
        googleSignInClient = GoogleSignInHelper.getGoogleSignInClient(this)


        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)

        // Create CallbackManager for Facebook login
        callbackManager = CallbackManager.Factory.create()

        // Register Facebook login callback
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(this@AuthActivity, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@AuthActivity, "Facebook Login Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

        // Register launcher for Google Sign-In result
        googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Compose UI
        setContent {
            FinMateTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ModernAuthPage(
                        onGoogleLoginClick = {
                            val signInIntent = googleSignInClient.signInIntent
                            googleLauncher.launch(signInIntent)
                        },
                        onFacebookLoginClick = {
                            LoginManager.getInstance().logInWithReadPermissions(
                                this@AuthActivity,
                                listOf("email", "public_profile")
                            )
                        }
                    )
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                // Profile exists, go to MainActivity
                                AnalyticsHelper.logEvent("user_login")
                                Firebase.analytics.setUserId(FirebaseAuth.getInstance().currentUser?.uid)
                                Firebase.analytics.setUserProperty("email", FirebaseAuth.getInstance().currentUser?.email)
                                Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                            } else {
                                // No profile, go to UserProfileActivity
                                startActivity(Intent(this, UserProfileActivity::class.java))
                            }
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Firebase Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val user = FirebaseAuth.getInstance().currentUser
                                AnalyticsHelper.logEvent("user_login")
                                Firebase.analytics.setUserId(FirebaseAuth.getInstance().currentUser?.uid)
                                Firebase.analytics.setUserProperty("email", FirebaseAuth.getInstance().currentUser?.email)

                                Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                startActivity(Intent(this, UserProfileActivity::class.java))
                            }
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Firebase Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsHelper.logEvent("app_resumed", null)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsHelper.logEvent("app_paused", null)
    }
}
