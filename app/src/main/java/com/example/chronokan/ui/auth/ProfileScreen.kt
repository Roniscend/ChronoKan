package com.example.chronokan.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val user = authViewModel.currentUser

    // Logic to generate a fun Superhero Name based on their UID
    val heroName = rememberHeroName(user?.uid)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hero Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile Image Section
            if (user?.photoUrl != null) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Superhero Name (The "Good Looking" Name)
            Text(
                text = heroName,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Real Name/Email (Subtext)
            Text(
                text = user?.displayName ?: user?.email ?: "Secret Identity",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Hero Statistics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hero Registry", fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Text("Identity: Verified", style = MaterialTheme.typography.bodyMedium)
                    // We show the "Clean" version of the UID here
                    Text("Hero ID: #${user?.uid?.take(6)?.uppercase()}", style = MaterialTheme.typography.bodyMedium)
                    Text("Provider: ${if (user?.photoUrl != null) "Google Alliance" else "Email Ops"}")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { authViewModel.signOut() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Retire from Duty (Logout)", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Fun helper function to assign a Superhero name based on the UID string
 */
@Composable
fun rememberHeroName(uid: String?): String {
    if (uid == null) return "Unknown Avenger"

    val heroPrefix = listOf("Iron", "Cyber", "Mega", "Shadow", "Captain", "Sonic", "Ultra", "Night", "Star", "Glacial")
    val heroSuffix = listOf("Knight", "Stryker", "Titan", "Phantom", "Nova", "Blade", "Guardian", "Flash", "Hunter", "Storm")

    // Use the hashcode of the UID to pick names so it's consistent for that user
    val index1 = Math.abs(uid.hashCode() % heroPrefix.size)
    val index2 = Math.abs((uid.hashCode() / 2) % heroSuffix.size)

    return "${heroPrefix[index1]} ${heroSuffix[index2]}"
}