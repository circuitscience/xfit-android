package ca.graymentality.xfit.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.graymentality.xfit.core.ProgramExercise
import ca.graymentality.xfit.templates.ExerciseCatalogItem

/**
 * 15RM Entry Screen
 *
 * Purpose:
 * After the user selects a template/exercise list, this screen collects the
 * user's current 15-rep max value for each selected exercise.
 *
 * v1 behavior:
 * - Displays one numeric input per selected exercise.
 * - Requires all 15RM fields to be valid numbers greater than 0.
 * - Converts selected exercises into ProgramExercise objects.
 * - Sets new15Rm = init15Rm + 5.0 for now.
 *
 * Important:
 * If the +5 starting bump changes later, update buildProgramExercises().
 */

@Composable
fun FifteenRmEntryScreen(
    selectedExercises: List<ExerciseCatalogItem>,
    onBack: () -> Unit,
    onGenerateProgram: (List<ProgramExercise>) -> Unit
) {
    val rmInputs = remember {
        mutableStateMapOf<Long, String>().apply {
            selectedExercises.forEach { exercise ->
                this[exercise.id] = ""
            }
        }
    }

    val validationMessage = remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Enter 15RM Values",
            color = Color(0xFFFF7A18),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter the weight you can complete for about 15 good reps for each selected exercise.",
            color = Color(0xFFE6E6E6),
            style = MaterialTheme.typography.bodyMedium
        )

        selectedExercises.forEach { exercise ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1D1D1D),
                    contentColor = Color(0xFFEFEFEF)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${exercise.primaryBodyPart} • ${exercise.equipment}",
                        color = Color(0xFFBDBDBD),
                        style = MaterialTheme.typography.bodySmall
                    )

                    OutlinedTextField(
                        value = rmInputs[exercise.id].orEmpty(),
                        onValueChange = { value ->
                            rmInputs[exercise.id] = value.filter { it.isDigit() || it == '.' }
                            validationMessage.value = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("15RM weight") },
                        suffix = { Text("lb") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }
        }

        validationMessage.value?.let { message ->
            Text(
                text = message,
                color = Color(0xFFFF6A6A),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF333333),
                    contentColor = Color.White
                )
            ) {
                Text("Back")
            }

            Button(
                onClick = {
                    val result = buildProgramExercises(
                        selectedExercises = selectedExercises,
                        rmInputs = rmInputs
                    )

                    if (result.isSuccess) {
                        onGenerateProgram(result.getOrThrow())
                    } else {
                        validationMessage.value = result.exceptionOrNull()?.message
                            ?: "Please check your 15RM values."
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7A18),
                    contentColor = Color.Black
                )
            ) {
                Text("Generate")
            }
        }
    }
}

fun buildProgramExercises(
    selectedExercises: List<ExerciseCatalogItem>,
    rmInputs: Map<Long, String>
): Result<List<ProgramExercise>> {
    if (selectedExercises.size !in 3..8) {
        return Result.failure(
            IllegalArgumentException("Select between 3 and 8 exercises before entering 15RM values.")
        )
    }

    val programExercises = mutableListOf<ProgramExercise>()

    selectedExercises.forEach { exercise ->
        val rawValue = rmInputs[exercise.id].orEmpty().trim()
        val init15Rm = rawValue.toDoubleOrNull()

        if (init15Rm == null || init15Rm <= 0.0) {
            return Result.failure(
                IllegalArgumentException("Enter a valid 15RM for ${exercise.name}.")
            )
        }

        programExercises += ProgramExercise(
            id = exercise.id,
            name = exercise.name,
            init15Rm = init15Rm,
            new15Rm = init15Rm + 5.0
        )
    }

    return Result.success(programExercises)
}
