package com.example.finmate


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.finmate.util.PrefsHelper
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

// -------- Data Model --------
data class OnboardingPage(
    val title: String,
    val description: String,
    val animationRes: Int
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            title = "Welcome to FinMate",
            description = "Your smart financial companion to help you take control of your money.",
            animationRes = R.raw.board2,
        ),
        OnboardingPage(
            title = "Track. Save. Grow.",
            description = "Easily monitor your expenses, set savings goals, and develop better money habits – all in one place.",
            animationRes = R.raw.board2
        ),
        OnboardingPage(
            title = "Secure & Effortless Access",
            description = "Your data is encrypted. Logging in is fast and safe. Get started in just a few taps." +
                    "\n",
            animationRes = R.raw.board2
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0EAFC), // top soft sky blue
                        Color(0xFFCFDEF3)  // bottom icy blue
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPageUI(page = pages[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(12.dp),
            activeColor = Color.Black,
            inactiveColor = Color.Gray,
            indicatorWidth = 10.dp,
            indicatorHeight = 10.dp,
            spacing = 8.dp
        )

        // Bottom buttons logic
        if (pagerState.currentPage < pages.lastIndex) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        PrefsHelper.setOnboardingCompleted(context)
                        onFinish()
                    }
                ) {
                    Text("Skip", fontSize = 14.sp, color = Color.Black)
                }

                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Next", fontSize = 14.sp, color = Color.Black)
                }
            }
        } else {
            Button(
                onClick = {
                    PrefsHelper.setOnboardingCompleted(context)
                    onFinish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Let’s Get Started", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun OnboardingPageUI(page: OnboardingPage) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(page.animationRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 22.sp,
                color = Color.Black,

            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = Color.Black,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
            lineHeight = 22.sp
        )
    }
}