package ca.graymentality.xfit.templates

/**
 * xFit Template Flow
 *
 * v1 decision:
 * - Offer templates first.
 * - Offer DIY selection only if requested.
 * - Templates guide the user but do not permanently lock exercise choices.
 * - v2 can add smarter recommendations, scoring, onboarding questionnaires,
 *   and adaptive template selection.
 */

enum class ExperienceLevel {
    Beginner,
    Intermediate,
    Advanced,
    AllLevels
}

enum class TemplateIntensity {
    Low,
    Moderate,
    High
}

enum class BodyPart {
    Chest,
    Back,
    Shoulders,
    Arms,
    Legs
}

enum class EquipmentType {
    Barbell,
    Dumbbell,
    Cable
}

data class ExerciseCatalogItem(
    val id: Long,
    val name: String,
    val primaryBodyPart: BodyPart,
    val equipment: EquipmentType
)

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val shortDescription: String,
    val experienceLevel: ExperienceLevel,
    val intensity: TemplateIntensity,
    val estimatedMinutes: IntRange,
    val exerciseCount: IntRange,
    val suggestedBodyParts: List<BodyPart>,
    val suggestedExerciseIds: List<Long>
)

object ExerciseCatalog {
    val all = listOf(
        // Chest
        ExerciseCatalogItem(1, "Barbell Bench Press", BodyPart.Chest, EquipmentType.Barbell),
        ExerciseCatalogItem(2, "Close-Grip Bench Press", BodyPart.Chest, EquipmentType.Barbell),
        ExerciseCatalogItem(3, "Dumbbell Bench Press", BodyPart.Chest, EquipmentType.Dumbbell),
        ExerciseCatalogItem(4, "Dumbbell Incline Press", BodyPart.Chest, EquipmentType.Dumbbell),

        // Back
        ExerciseCatalogItem(5, "Barbell Row", BodyPart.Back, EquipmentType.Barbell),
        ExerciseCatalogItem(6, "Dumbbell Row", BodyPart.Back, EquipmentType.Dumbbell),
        ExerciseCatalogItem(7, "Cable Row", BodyPart.Back, EquipmentType.Cable),
        ExerciseCatalogItem(8, "Lat Pulldown", BodyPart.Back, EquipmentType.Cable),
        ExerciseCatalogItem(9, "Barbell Deadlift", BodyPart.Back, EquipmentType.Barbell),
        ExerciseCatalogItem(10, "Romanian Deadlift", BodyPart.Back, EquipmentType.Barbell),
        ExerciseCatalogItem(11, "Dumbbell Romanian Deadlift", BodyPart.Back, EquipmentType.Dumbbell),
        ExerciseCatalogItem(12, "Barbell Shrug", BodyPart.Back, EquipmentType.Barbell),

        // Shoulders
        ExerciseCatalogItem(13, "Barbell Overhead Press", BodyPart.Shoulders, EquipmentType.Barbell),
        ExerciseCatalogItem(14, "Dumbbell Shoulder Press", BodyPart.Shoulders, EquipmentType.Dumbbell),
        ExerciseCatalogItem(15, "Dumbbell Lateral Raise", BodyPart.Shoulders, EquipmentType.Dumbbell),
        ExerciseCatalogItem(16, "Cable Face Pull", BodyPart.Shoulders, EquipmentType.Cable),

        // Arms
        ExerciseCatalogItem(17, "Barbell Curl", BodyPart.Arms, EquipmentType.Barbell),
        ExerciseCatalogItem(18, "Dumbbell Curl", BodyPart.Arms, EquipmentType.Dumbbell),
        ExerciseCatalogItem(19, "Dumbbell Triceps Extension", BodyPart.Arms, EquipmentType.Dumbbell),
        ExerciseCatalogItem(20, "Cable Triceps Pushdown", BodyPart.Arms, EquipmentType.Cable),

        // Legs
        ExerciseCatalogItem(21, "Barbell Squat", BodyPart.Legs, EquipmentType.Barbell),
        ExerciseCatalogItem(22, "Front Squat", BodyPart.Legs, EquipmentType.Barbell),
        ExerciseCatalogItem(23, "Dumbbell Lunge", BodyPart.Legs, EquipmentType.Dumbbell)
    )

    fun groupedByBodyPart(): Map<BodyPart, List<ExerciseCatalogItem>> {
        return all.groupBy { it.primaryBodyPart }
    }

    fun findByIds(ids: List<Long>): List<ExerciseCatalogItem> {
        val idSet = ids.toSet()
        return all.filter { it.id in idSet }
    }
}

object TemplateCatalog {
    val all = listOf(
        WorkoutTemplate(
            id = "starter_full_body",
            name = "Starter Full Body",
            shortDescription = "A simple introduction for learning basic movement patterns without excessive fatigue.",
            experienceLevel = ExperienceLevel.Beginner,
            intensity = TemplateIntensity.Low,
            estimatedMinutes = 30..40,
            exerciseCount = 3..4,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Legs),
            suggestedExerciseIds = listOf(3, 6, 23)
        ),
        WorkoutTemplate(
            id = "beginner_strength",
            name = "Beginner Strength",
            shortDescription = "A basic strength template built around manageable free-weight fundamentals.",
            experienceLevel = ExperienceLevel.Beginner,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 40..50,
            exerciseCount = 4..5,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 5, 13, 21)
        ),
        WorkoutTemplate(
            id = "minimalist",
            name = "Minimalist",
            shortDescription = "For tight schedules. Few exercises, high consistency, low decision fatigue.",
            experienceLevel = ExperienceLevel.AllLevels,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 25..35,
            exerciseCount = 3..3,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 5, 21)
        ),
        WorkoutTemplate(
            id = "balanced_full_body",
            name = "Balanced Full Body",
            shortDescription = "A well-rounded template covering push, pull, legs, shoulders, and arms.",
            experienceLevel = ExperienceLevel.Intermediate,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 50..60,
            exerciseCount = 5..6,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Arms, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 7, 14, 20, 21)
        ),
        WorkoutTemplate(
            id = "upper_emphasis",
            name = "Upper Emphasis",
            shortDescription = "More upper-body work while keeping legs present enough for balance.",
            experienceLevel = ExperienceLevel.Intermediate,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 50..60,
            exerciseCount = 5..6,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Arms, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 6, 13, 17, 21)
        ),
        WorkoutTemplate(
            id = "lower_emphasis",
            name = "Lower Emphasis",
            shortDescription = "Squat and hinge bias with enough upper-body work to stay balanced.",
            experienceLevel = ExperienceLevel.Intermediate,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 50..60,
            exerciseCount = 5..6,
            suggestedBodyParts = listOf(BodyPart.Legs, BodyPart.Back, BodyPart.Chest, BodyPart.Shoulders),
            suggestedExerciseIds = listOf(21, 10, 1, 8, 14)
        ),
        WorkoutTemplate(
            id = "strength_bias",
            name = "Strength Bias",
            shortDescription = "Barbell-dominant template for users comfortable with heavier free-weight work.",
            experienceLevel = ExperienceLevel.Intermediate,
            intensity = TemplateIntensity.High,
            estimatedMinutes = 60..70,
            exerciseCount = 4..5,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 21, 9, 13)
        ),
        WorkoutTemplate(
            id = "volume_builder",
            name = "Volume Builder",
            shortDescription = "More exercises for users who tolerate longer sessions and want broader coverage.",
            experienceLevel = ExperienceLevel.Intermediate,
            intensity = TemplateIntensity.Moderate,
            estimatedMinutes = 60..75,
            exerciseCount = 6..8,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Arms, BodyPart.Legs),
            suggestedExerciseIds = listOf(3, 7, 14, 15, 18, 20, 23)
        ),
        WorkoutTemplate(
            id = "recovery_reentry",
            name = "Recovery / Re-entry",
            shortDescription = "A low-pressure path for users returning after time away from training.",
            experienceLevel = ExperienceLevel.AllLevels,
            intensity = TemplateIntensity.Low,
            estimatedMinutes = 30..40,
            exerciseCount = 3..4,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Legs),
            suggestedExerciseIds = listOf(3, 8, 23)
        ),
        WorkoutTemplate(
            id = "advanced_full_body",
            name = "Advanced Full Body",
            shortDescription = "Larger full-body workload for users with strong free-weight experience and time tolerance.",
            experienceLevel = ExperienceLevel.Advanced,
            intensity = TemplateIntensity.High,
            estimatedMinutes = 70..90,
            exerciseCount = 6..8,
            suggestedBodyParts = listOf(BodyPart.Chest, BodyPart.Back, BodyPart.Shoulders, BodyPart.Arms, BodyPart.Legs),
            suggestedExerciseIds = listOf(1, 21, 9, 13, 7, 16, 20)
        )
    )

    val diy = WorkoutTemplate(
        id = "diy",
        name = "Build My Own Program",
        shortDescription = "Start from a blank exercise list and choose your own 3 to 8 movements.",
        experienceLevel = ExperienceLevel.AllLevels,
        intensity = TemplateIntensity.Moderate,
        estimatedMinutes = 30..90,
        exerciseCount = 3..8,
        suggestedBodyParts = BodyPart.entries,
        suggestedExerciseIds = emptyList()
    )

    fun findById(id: String): WorkoutTemplate? {
        return if (id == diy.id) diy else all.firstOrNull { it.id == id }
    }
}
