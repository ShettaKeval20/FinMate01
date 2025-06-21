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
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Google Sign-In Client
        googleSignInClient = GoogleSignInHelper.getGoogleSignInClient(this)
        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()


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
                    user?.let {
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            val loginBundle = Bundle().apply {
                                putString(FirebaseAnalytics.Param.METHOD, "google")
                            }
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle)

                            // Store email and photoUrl if profile doesn't exist
                            val userInfo = mapOf(
                                "email" to user.email,
                                "photoUrl" to user.photoUrl?.toString()
                            )
                            if (!dataSnapshot.exists()) {
                                userRef.setValue(userInfo)
                            }

                            val intent = if (dataSnapshot.exists()) {
                                Intent(this@AuthActivity, MainActivity::class.java)
                            } else {
                                Intent(this@AuthActivity, UserProfileActivity::class.java)
                            }

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
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


//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = FirebaseAuth.getInstance().currentUser
//                    user?.let {
//                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
//                        userRef.get().addOnSuccessListener { dataSnapshot ->
//                            val loginBundle = Bundle().apply {
//                                putString(FirebaseAnalytics.Param.METHOD, "google")
//                            }
//                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle)
//
//                            val intent = if (dataSnapshot.exists()) {
//                                Intent(this@AuthActivity, MainActivity::class.java)
//                            } else {
//                                Intent(this@AuthActivity, UserProfileActivity::class.java)
//                            }
//
//                            // Clear activity stack before navigating
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            startActivity(intent)
//                        }.addOnFailureListener {
//                            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(this, "Firebase Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            val loginBundle = Bundle().apply {
                                putString(FirebaseAnalytics.Param.METHOD, "facebook")
                            }
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle)

                            // Store email and photoUrl if profile doesn't exist
                            val userInfo = mapOf(
                                "email" to user.email,
                                "photoUrl" to user.photoUrl?.toString()
                            )
                            if (!dataSnapshot.exists()) {
                                userRef.setValue(userInfo)
                            }

                            val intent = if (dataSnapshot.exists()) {
                                Intent(this@AuthActivity, MainActivity::class.java)
                            } else {
                                Intent(this@AuthActivity, UserProfileActivity::class.java)
                            }

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
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


//    private fun handleFacebookAccessToken(token: AccessToken) {
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = FirebaseAuth.getInstance().currentUser
//                    user?.let {
//                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
//                        userRef.get().addOnSuccessListener { dataSnapshot ->
//                            val loginBundle = Bundle().apply {
//                                putString(FirebaseAnalytics.Param.METHOD, "facebook")
//                            }
//                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, loginBundle)
//
//                            val intent = if (dataSnapshot.exists()) {
//                                Intent(this@AuthActivity, MainActivity::class.java)
//                            } else {
//                                Intent(this@AuthActivity, UserProfileActivity::class.java)
//                            }
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            startActivity(intent)
//                        }.addOnFailureListener {
//                            Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(this, "Firebase Auth failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent("app_resumed", null)
    }

    override fun onPause() {
        super.onPause()
        firebaseAnalytics.logEvent("app_paused", null)
    }
}
