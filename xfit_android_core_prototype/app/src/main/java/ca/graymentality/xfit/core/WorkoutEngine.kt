package ca.graymentality.xfit.core

import kotlin.math.floor

/**
 * xFit Core Workout Engine
 *
 * Purpose:
 * Generates the core 54-workout xFit stage from user-selected exercises.
 *
 * Current prototype rules confirmed:
 * - App: xfit
 * - Brand: Gray Mentality
 * - Subtitle: A compliance-based exercise program
 * - Exercise count: 3 to 8 exercises
 * - Stage length: 54 workouts
 * - Structure: 9 mini-cycles of 6 workouts each
 * - Rep phases: 15 reps, then 10 reps, then 5 reps
 * - Intensity ramp: 75%, 80%, 85%, 90%, 95%, 100%
 * - Weight rounding: floor to nearest 2.5 lb
 * - Sixth workout in each mini-cycle must equal that mini-cycle's target RM exactly
 */

const val MIN_EXERCISES = 3
const val MAX_EXERCISES = 8
const val WORKOUTS_PER_STAGE = 54
const val WORKOUTS_PER_MINI_CYCLE = 6

/**
 * Conservative xFit RM conversion constants.
 *
 * These are programming constants for xFit, not universal physiology laws.
 *
 * Confirmed rules:
 * - estimated 1RM = new15Rm * 1.40
 * - estimated 10RM = estimated1Rm / 1.27
 * - estimated 5RM = estimated1Rm / 1.11
 */
const val XFIT_15RM_TO_1RM_MULTIPLIER = 1.40
const val XFIT_1RM_TO_10RM_DIVISOR = 1.27
const val XFIT_1RM_TO_5RM_DIVISOR = 1.11

enum class WorkoutStatus {
    Pending,
    Done,
    Missed,
    Failed
}

enum class RepPhase(val reps: Int) {
    Fifteen(15),
    Ten(10),
    Five(5)
}

data class PhaseTargets(
    val rm15: Double,
    val rm10: Double,
    val rm5: Double
) {
    fun targetFor(phase: RepPhase): Double {
        return when (phase) {
            RepPhase.Fifteen -> rm15
            RepPhase.Ten -> rm10
            RepPhase.Five -> rm5
        }
    }

    companion object {
        fun fromNew15Rm(new15Rm: Double): PhaseTargets {
            require(new15Rm > 0.0) { "New 15RM must be greater than zero." }

            val estimated1Rm = new15Rm * XFIT_15RM_TO_1RM_MULTIPLIER

            return PhaseTargets(
                rm15 = new15Rm,
                rm10 = estimated1Rm / XFIT_1RM_TO_10RM_DIVISOR,
                rm5 = estimated1Rm / XFIT_1RM_TO_5RM_DIVISOR
            )
        }
    }
}

data class ProgramExercise(
    val id: Long,
    val name: String,
    val init15Rm: Double,
    val new15Rm: Double,
    val cycleIncrement: Double = 2.5,
    val phaseTargets: PhaseTargets = PhaseTargets.fromNew15Rm(new15Rm)
) {
    init {
        require(id > 0) { "Exercise id must be greater than zero." }
        require(name.isNotBlank()) { "Exercise name cannot be blank." }
        require(init15Rm > 0.0) { "Initial 15RM must be greater than zero." }
        require(new15Rm > 0.0) { "New 15RM must be greater than zero." }
        require(cycleIncrement >= 0.0) { "Cycle increment cannot be negative." }
        require(phaseTargets.rm15 > 0.0) { "15RM target must be greater than zero." }
        require(phaseTargets.rm10 > 0.0) { "10RM target must be greater than zero." }
        require(phaseTargets.rm5 > 0.0) { "5RM target must be greater than zero." }
    }
}

data class WorkoutExercise(
    val exerciseId: Long,
    val exerciseName: String,
    val workoutNumber: Int,
    val miniCycleNumber: Int,
    val miniCyclePosition: Int,
    val repPhase: RepPhase,
    val sets: Int,
    val reps: Int,
    val targetRm: Double,
    val intensity: Double,
    val weight: Double,
    val totalPossibleReps: Int,
    val totalPossibleWeight: Double,
    val status: WorkoutStatus = WorkoutStatus.Pending
)

data class WorkoutSession(
    val workoutNumber: Int,
    val miniCycleNumber: Int,
    val miniCyclePosition: Int,
    val repPhase: RepPhase,
    val sets: Int,
    val reps: Int,
    val exercises: List<WorkoutExercise>
)

data class GeneratedStage(
    val sessions: List<WorkoutSession>,
    val rows: List<WorkoutExercise>
)

object IntensityRamp {
    private val ramp = listOf(0.75, 0.80, 0.85, 0.90, 0.95, 1.00)

    fun forMiniCyclePosition(position: Int): Double {
        require(position in 1..WORKOUTS_PER_MINI_CYCLE) {
            "Mini-cycle position must be between 1 and 6."
        }
        return ramp[position - 1]
    }
}

fun floorToNearestTwoPointFive(value: Double): Double {
    return floor(value / 2.5) * 2.5
}

object StageStructure {
    fun miniCycleNumber(workoutNumber: Int): Int {
        validateWorkoutNumber(workoutNumber)
        return ((workoutNumber - 1) / WORKOUTS_PER_MINI_CYCLE) + 1
    }

    fun miniCyclePosition(workoutNumber: Int): Int {
        validateWorkoutNumber(workoutNumber)
        return ((workoutNumber - 1) % WORKOUTS_PER_MINI_CYCLE) + 1
    }

    fun repPhase(workoutNumber: Int): RepPhase {
        validateWorkoutNumber(workoutNumber)
        return when (workoutNumber) {
            in 1..18 -> RepPhase.Fifteen
            in 19..36 -> RepPhase.Ten
            in 37..54 -> RepPhase.Five
            else -> error("Invalid workout number: $workoutNumber")
        }
    }

    fun sets(workoutNumber: Int): Int {
        validateWorkoutNumber(workoutNumber)
        return when (workoutNumber) {
            in 1..6 -> 1
            in 7..12 -> 2
            in 13..18 -> 3
            in 19..24 -> 1
            in 25..30 -> 2
            in 31..36 -> 3
            in 37..42 -> 1
            in 43..48 -> 2
            in 49..54 -> 3
            else -> error("Invalid workout number: $workoutNumber")
        }
    }

    private fun validateWorkoutNumber(workoutNumber: Int) {
        require(workoutNumber in 1..WORKOUTS_PER_STAGE) {
            "Workout number must be between 1 and 54."
        }
    }
}

object WorkoutGenerator {
    fun generateStage(exercises: List<ProgramExercise>): GeneratedStage {
        validateExercises(exercises)

        val sessions = (1..WORKOUTS_PER_STAGE).map { workoutNumber ->
            val miniCycleNumber = StageStructure.miniCycleNumber(workoutNumber)
            val miniCyclePosition = StageStructure.miniCyclePosition(workoutNumber)
            val phase = StageStructure.repPhase(workoutNumber)
            val sets = StageStructure.sets(workoutNumber)
            val reps = phase.reps
            val intensity = IntensityRamp.forMiniCyclePosition(miniCyclePosition)

            val workoutRows = exercises.map { exercise ->
                val targetRm = exercise.phaseTargets.targetFor(phase)
                val weight = calculateWorkoutWeight(
                    targetRm = targetRm,
                    intensity = intensity,
                    miniCyclePosition = miniCyclePosition
                )

                WorkoutExercise(
                    exerciseId = exercise.id,
                    exerciseName = exercise.name,
                    workoutNumber = workoutNumber,
                    miniCycleNumber = miniCycleNumber,
                    miniCyclePosition = miniCyclePosition,
                    repPhase = phase,
                    sets = sets,
                    reps = reps,
                    targetRm = targetRm,
                    intensity = intensity,
                    weight = weight,
                    totalPossibleReps = sets * reps,
                    totalPossibleWeight = sets * reps * weight
                )
            }

            WorkoutSession(
                workoutNumber = workoutNumber,
                miniCycleNumber = miniCycleNumber,
                miniCyclePosition = miniCyclePosition,
                repPhase = phase,
                sets = sets,
                reps = reps,
                exercises = workoutRows
            )
        }

        return GeneratedStage(
            sessions = sessions,
            rows = sessions.flatMap { it.exercises }
        )
    }

    fun calculateWorkoutWeight(
        targetRm: Double,
        intensity: Double,
        miniCyclePosition: Int
    ): Double {
        require(targetRm > 0.0) { "Target RM must be greater than zero." }
        require(intensity > 0.0) { "Intensity must be greater than zero." }
        require(miniCyclePosition in 1..WORKOUTS_PER_MINI_CYCLE) {
            "Mini-cycle position must be between 1 and 6."
        }

        return if (miniCyclePosition == WORKOUTS_PER_MINI_CYCLE) {
            targetRm
        } else {
            floorToNearestTwoPointFive(targetRm * intensity)
        }
    }

    private fun validateExercises(exercises: List<ProgramExercise>) {
        require(exercises.size in MIN_EXERCISES..MAX_EXERCISES) {
            "xFit prototype requires between $MIN_EXERCISES and $MAX_EXERCISES exercises."
        }

        val duplicateNames = exercises
            .groupBy { it.name.trim().lowercase() }
            .filter { it.value.size > 1 }
            .keys

        require(duplicateNames.isEmpty()) {
            "Duplicate exercise names are not allowed: ${duplicateNames.joinToString()}"
        }
    }
}

fun sampleGeneratedStage(): GeneratedStage {
    val exercises = listOf(
        ProgramExercise(
            id = 1,
            name = "Bench Press",
            init15Rm = 135.0,
            new15Rm = 140.0
        ),
        ProgramExercise(
            id = 2,
            name = "Squat",
            init15Rm = 185.0,
            new15Rm = 190.0
        ),
        ProgramExercise(
            id = 3,
            name = "Row",
            init15Rm = 115.0,
            new15Rm = 120.0
        )
    )

    return WorkoutGenerator.generateStage(exercises)
}

fun previewFirstSession(): String {
    val stage = sampleGeneratedStage()
    val first = stage.sessions.first()

    return buildString {
        appendLine("Workout ${first.workoutNumber}")
        appendLine("Mini-cycle ${first.miniCycleNumber}, position ${first.miniCyclePosition}")
        appendLine("${first.sets} set(s) x ${first.reps} reps")
        appendLine()

        first.exercises.forEach { row ->
            appendLine("${row.exerciseName}: ${row.weight} lb")
        }
    }
}

object WorkoutEngineSmokeTests {
    fun runAll(): String {
        val results = mutableListOf<String>()

        results += checkExerciseCountValidation()
        results += checkStageRowCounts()
        results += checkMiniCycleRampAndExactSixthWorkout()
        results += checkRepPhaseBoundaries()
        results += checkSetBoundaries()
        results += checkRmConversions()

        return buildString {
            appendLine("xFit Core Engine Smoke Tests")
            appendLine("================================")
            results.forEach { appendLine(it) }
        }
    }

    private fun checkExerciseCountValidation(): String {
        val twoExercises = listOf(
            ProgramExercise(1, "Bench Press", 135.0, 140.0),
            ProgramExercise(2, "Squat", 185.0, 190.0)
        )

        return try {
            WorkoutGenerator.generateStage(twoExercises)
            "FAIL: Exercise count validation did not reject fewer than 3 exercises."
        } catch (_: IllegalArgumentException) {
            "PASS: Exercise count validation rejects fewer than 3 exercises."
        }
    }

    private fun checkStageRowCounts(): String {
        val stage = sampleGeneratedStage()
        val expectedSessions = 54
        val expectedRows = 54 * 3

        return if (stage.sessions.size == expectedSessions && stage.rows.size == expectedRows) {
            "PASS: Stage generates $expectedSessions sessions and $expectedRows exercise rows for 3 exercises."
        } else {
            "FAIL: Expected $expectedSessions sessions and $expectedRows rows, got ${stage.sessions.size} sessions and ${stage.rows.size} rows."
        }
    }

    private fun checkMiniCycleRampAndExactSixthWorkout(): String {
        val exercise = ProgramExercise(
            id = 1,
            name = "Bench Press",
            init15Rm = 135.0,
            new15Rm = 140.0
        )

        val target = exercise.phaseTargets.rm15
        val expectedWeights = listOf(
            floorToNearestTwoPointFive(target * 0.75),
            floorToNearestTwoPointFive(target * 0.80),
            floorToNearestTwoPointFive(target * 0.85),
            floorToNearestTwoPointFive(target * 0.90),
            floorToNearestTwoPointFive(target * 0.95),
            target
        )

        val actualWeights = (1..6).map { position ->
            WorkoutGenerator.calculateWorkoutWeight(
                targetRm = target,
                intensity = IntensityRamp.forMiniCyclePosition(position),
                miniCyclePosition = position
            )
        }

        return if (actualWeights == expectedWeights) {
            "PASS: Mini-cycle ramp floors workouts 1-5 and forces workout 6 to exact target RM."
        } else {
            "FAIL: Ramp mismatch. Expected $expectedWeights but got $actualWeights."
        }
    }

    private fun checkRepPhaseBoundaries(): String {
        val checks = mapOf(
            1 to RepPhase.Fifteen,
            18 to RepPhase.Fifteen,
            19 to RepPhase.Ten,
            36 to RepPhase.Ten,
            37 to RepPhase.Five,
            54 to RepPhase.Five
        )

        val failures = checks.filter { (workoutNumber, expectedPhase) ->
            StageStructure.repPhase(workoutNumber) != expectedPhase
        }

        return if (failures.isEmpty()) {
            "PASS: Rep phase boundaries are correct at workouts 1, 18, 19, 36, 37, and 54."
        } else {
            "FAIL: Rep phase boundary failures: $failures"
        }
    }

    private fun checkSetBoundaries(): String {
        val checks = mapOf(
            1 to 1,
            6 to 1,
            7 to 2,
            12 to 2,
            13 to 3,
            18 to 3,
            19 to 1,
            24 to 1,
            25 to 2,
            30 to 2,
            31 to 3,
            36 to 3,
            37 to 1,
            42 to 1,
            43 to 2,
            48 to 2,
            49 to 3,
            54 to 3
        )

        val failures = checks.filter { (workoutNumber, expectedSets) ->
            StageStructure.sets(workoutNumber) != expectedSets
        }

        return if (failures.isEmpty()) {
            "PASS: Set boundaries are correct across all 9 mini-cycles."
        } else {
            "FAIL: Set boundary failures: $failures"
        }
    }

    private fun checkRmConversions(): String {
        val new15Rm = 140.0
        val targets = PhaseTargets.fromNew15Rm(new15Rm)
        val expected1Rm = new15Rm * XFIT_15RM_TO_1RM_MULTIPLIER
        val expected10Rm = expected1Rm / XFIT_1RM_TO_10RM_DIVISOR
        val expected5Rm = expected1Rm / XFIT_1RM_TO_5RM_DIVISOR

        val pass = targets.rm15 == new15Rm &&
            targets.rm10 == expected10Rm &&
            targets.rm5 == expected5Rm &&
            targets.rm5 > targets.rm10

        return if (pass) {
            "PASS: RM conversions use xFit constants and 5RM is heavier than 10RM."
        } else {
            "FAIL: RM conversion mismatch. Targets: $targets"
        }
    }
}
