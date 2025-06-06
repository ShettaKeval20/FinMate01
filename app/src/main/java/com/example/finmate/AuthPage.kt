package com.example.finmate

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AuthPage(
//    onlogin: () -> Unit = {},
//    onGoogleLoginClick: () -> Unit = {},
//    onSignupClick: () -> Unit = {},
//    onForgotPasswordClick: () -> Unit = {},
//    onBackClick: () -> Unit = {},
//    onTermsClick: () -> Unit = {},
//    onPrivacyClick: () -> Unit = {},
//    pwdVisiblity: MutableState<Boolean> = remember { mutableStateOf(false) },
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var isLogin by remember { mutableStateOf(true) }
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val auth = FirebaseAuth.getInstance()
//
//    Surface(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column (modifier = Modifier.fillMaxSize()
//            .padding(horizontal = 24.dp),
//            horizontalAlignment  = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        ){
////            Header Section
//            Spacer(modifier = Modifier.height(48.dp))
//            Text(text = stringResource(R.string.login),
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold,
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text(text = stringResource(R.string.text),
//                style = MaterialTheme.typography.titleSmall,
//            )
//
//            Spacer(modifier = Modifier.height(60.dp))
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                placeholder = { Text("Please enter your email") },
//                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                placeholder = { Text("Please enter your password") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                singleLine = true,
//                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//                    val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            if (!isLogin) {
//                OutlinedTextField(
//                    value = confirmPassword,
//                    onValueChange = { confirmPassword = it },
//                    label = { Text("Confirm Password") },
//                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
//                    placeholder = { Text("Please confirm your password") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    singleLine = true,
//                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        val image = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
//                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
//                            Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    if (isLogin) {
//                        auth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    val user = auth.currentUser
//                                    if (user != null && user.isEmailVerified) {
//                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
//                                        Log.d("Auth", "Login success")
//                                        email = ""
//                                        password = ""
//                                        confirmPassword = ""
//                                        context.startActivity(Intent(context, MainActivity::class.java))
//                                    } else {
//                                        auth.signOut()
//                                        Toast.makeText(context, "Please verify your email before logging in", Toast.LENGTH_LONG).show()
//                                    }
//                                } else {
//                                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                                    Log.e("Auth", "Login error: ${task.exception?.message}")
//                                }
//                            }
//                    } else {
//                        if (password == confirmPassword) {
//                            auth.createUserWithEmailAndPassword(email, password)
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        val user = auth.currentUser
//                                        user?.sendEmailVerification()
//                                            ?.addOnCompleteListener { emailTask ->
//                                                if (emailTask.isSuccessful) {
//                                                    Toast.makeText(context, "Verification email sent. Please verify before login.", Toast.LENGTH_LONG).show()
//                                                    Log.d("Auth", "Verification email sent")
//                                                    isLogin = true
//                                                    email = ""
//                                                    password = ""
//                                                    confirmPassword = ""
//                                                } else {
//                                                    Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
//                                                    Log.e("Auth", "Verification email error: ${emailTask.exception?.message}")
//                                                }
//                                            }
//                                    } else {
//                                        Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                                        Log.e("Auth", "Registration error: ${task.exception?.message}")
//                                    }
//                                }
//                        } else {
//                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                },
//                        modifier = Modifier.fillMaxWidth()
//            )
//
//            {
//                Text(text = if (isLogin) "Login" else "Register")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = if (isLogin) "Don't have an account? Register" else "Already have an account? Login",
//                color = MaterialTheme.colorScheme.primary,
//                fontSize = 14.sp,
//                modifier = Modifier.clickable { isLogin = !isLogin }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
////          Divider with text
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Divider(modifier = Modifier.weight(1f))
//                Text(
//                    text = "  or continue with  ",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                )
//                Divider(modifier = Modifier.weight(1f))
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//// Social login icons row
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(24.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                IconButton(onClick = onGoogleLoginClick) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.google),
//                        contentDescription = "Google Sign-In",
//                        modifier = Modifier.size(32.dp),
//                        tint = Color.Unspecified // Keep original logo colors
//                    )
//                }
//                IconButton(onClick = {
//                    Toast.makeText(context, "Facebook login not implemented", Toast.LENGTH_SHORT).show()
//                }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.facebook),
//                        contentDescription = "Facebook Sign-In",
//                        modifier = Modifier.size(32.dp),
//                        tint = Color.Unspecified
//                    )
//                }
//            }
//
//        }
//    }
//}

// This is my new Design

@Composable
fun ModernAuthPage(
    onGoogleLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = if (isLogin) "Welcome Back 👋" else "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = if (isLogin) "Login to FinMate to continue" else "Register to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("example@mail.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            )

            // Confirm Password for Register
            if (!isLogin) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    placeholder = { Text("Re-enter password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle confirm password visibility"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    if (isLogin) {
                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = auth.currentUser
                                if (user?.isEmailVerified == true) {
                                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                } else {
                                    auth.signOut()
                                    Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        if (password == confirmPassword) {
                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    auth.currentUser?.sendEmailVerification()
                                    Toast.makeText(context, "Check your email for verification", Toast.LENGTH_LONG).show()
                                    isLogin = true
                                } else {
                                    Toast.makeText(context, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = if (isLogin) "Login" else "Register")
            }

            // Toggle Login/Register
            Text(
                text = if (isLogin) "Don't have an account? Register" else "Already have an account? Login",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { isLogin = !isLogin }
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    text = "  OR  ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social login icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                IconButton(onClick = onGoogleLoginClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Sign-In",
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = {
                    Toast.makeText(context, "Facebook login not implemented", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook Sign-In",
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}
