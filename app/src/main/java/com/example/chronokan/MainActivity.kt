package com.example.chronokan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.chronokan.data.AppDatabase
import com.example.chronokan.data.repository.KanbanRepository
import com.example.chronokan.ui.auth.AuthViewModel
import com.example.chronokan.ui.auth.LoginScreen
import com.example.chronokan.ui.board.BoardViewModel
import com.example.chronokan.ui.board.BoardViewModelFactory
import com.example.chronokan.ui.navigation.ChronosNavGraph
import com.example.chronokan.ui.theme.ChronosTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "chronos-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        // 2. Initialize Repository
        val repository = KanbanRepository(
            cardDao = db.cardDao(),
            historyDao = db.historyDao()
        )

        setContent {
            ChronosTheme {
                val context = LocalContext.current

                // 3. Initialize ViewModels
                val authViewModel: AuthViewModel = viewModel()
                val boardViewModel: BoardViewModel = viewModel(
                    factory = BoardViewModelFactory(repository)
                )

                // 4. Google Sign-In Setup
                val webClientId = stringResource(id = R.string.default_web_client_id)

                val googleSignInLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        account?.idToken?.let { idToken ->
                            authViewModel.signInWithGoogle(idToken) { error ->
                                if (error != null) {
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } catch (e: ApiException) {
                        Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                // 5. Navigation Logic
                val currentUser = authViewModel.currentUser

                if (currentUser == null) {
                    LoginScreen(
                        viewModel = authViewModel,
                        onGoogleSignInClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(webClientId)
                                .requestEmail()
                                .build()
                            val client = GoogleSignIn.getClient(context, gso)
                            googleSignInLauncher.launch(client.signInIntent)
                        }
                    )
                } else {
                    // UPDATED: Now passing authViewModel to the NavGraph
                    ChronosNavGraph(
                        viewModel = boardViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}