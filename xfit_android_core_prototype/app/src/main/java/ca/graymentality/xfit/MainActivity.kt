package ca.graymentality.xfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.graymentality.xfit.core.WorkoutEngineSmokeTests
import ca.graymentality.xfit.core.previewFirstSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
    val exercises = remember {
        listOf(
            ExerciseCatalog.all[0],
            ExerciseCatalog.all[1],
            ExerciseCatalog.all[2]
        )
    }

    FifteenRmEntryScreen(
        selectedExercises = exercises,
        onBack = {},
        onGenerateProgram = { programExercises ->
            val stage = WorkoutGenerator.generateStage(programExercises)

            println(stage.sessions.first())
        }
    )
}
    }
}

@Composable
fun XfitPrototypeApp() {
    val output = remember {
        mutableStateOf("Tap the button to generate the first xFit prototype workout and run smoke tests.")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111)),
        color = Color(0xFF111111)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "xfit",
                color = Color(0xFFFF7A18),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "A compliance-based exercise program",
                color = Color(0xFFE6E6E6),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Gray Mentality prototype: Kotlin core engine only. No Room database, no backend, no login yet.",
                color = Color(0xFFBDBDBD),
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    output.value = buildString {
                        appendLine(previewFirstSession())
                        appendLine()
                        appendLine(WorkoutEngineSmokeTests.runAll())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7A18),
                    contentColor = Color.Black
                )
            ) {
                Text("Generate Sample Stage")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1D1D1D),
                    contentColor = Color(0xFFEFEFEF)
                )
            ) {
                Text(
                    text = output.value,
                    modifier = Modifier.padding(16.dp),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
