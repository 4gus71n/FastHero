import Foundation
import ComposeApp

struct MilestoneEntry {
    let title: String
    let description: String
    let warnings: String? // Optional, can be nil
    let advice: String? // Optional, can be nil
    let timestampInSeconds: Double
}

struct iOSMockData {
    static let fastingOptions: [SettingOptionList] = [
        SettingOptionList(
            id: "12:12",
            title: "12:12 - Easy/Starter",
            description: "12 hrs fasting, 12 hrs eating. Every day.",
            fastingTimeInSeconds: 12 * 3600
        ),
        SettingOptionList(
            id: "14:10",
            title: "14:10 - Relaxed",
            description: "14 hrs fasting, 10 hrs eating. 5-7 days a week.",
            fastingTimeInSeconds: 14 * 3600
        ),
        SettingOptionList(
            id: "16:8",
            title: "16:8 - Popular",
            description: "16 hrs fasting, 8 hrs eating. 5-7 days a week.",
            fastingTimeInSeconds: 16 * 3600
        ),
        SettingOptionList(
            id: "18:6",
            title: "18:6 - Advanced",
            description: "18 hrs fasting, 6 hrs eating. 5-7 days a week.",
            fastingTimeInSeconds: 18 * 3600
        ),
        SettingOptionList(
            id: "20:4",
            title: "20:4 - Autophagy",
            description: "20 hrs fasting, 4 hrs eating. 5-7 days a week.",
            fastingTimeInSeconds: 20 * 3600
        ),
        SettingOptionList(
            id: "OMAD",
            title: "OMAD",
            description: "One Meal A Day. 5-7 days a week.",
            fastingTimeInSeconds: 24 * 3600
        ),
        SettingOptionList(
            id: "14:10 Crescendo",
            title: "14:10 (Crescendo)",
            description: "Fast 3 days a week. Alternative to regular 14:10.",
            fastingTimeInSeconds: 14 * 3600
        )
    ]

    static let fastingMilestones: [MilestoneEntry] = [
            MilestoneEntry(
                title : "Ready, Set, Go!",
                description : "Track every change your body experiences right here as you begin your journey.",
                warnings : nil,
                advice : nil,
                timestampInSeconds : 0.0
            ),

            [
                MilestoneEntry(
                    title : "Blood Sugar Levels Drop",
                    description : "Your insulin levels begin to decrease, leading to better blood sugar control.",
                    warnings : "If prone to blood sugar drops, monitor how you feel.",
                    advice : "Stay hydrated with water or herbal tea.",
                    timestampInSeconds : 1.0 * 3600
                ),
                MilestoneEntry(
                    title : "Blood Sugar Stabilization",
                    description : "Insulin levels drop slightly, allowing better glucose control.",
                    warnings : nil,
                    advice : "Drinking water helps curb cravings.",
                    timestampInSeconds : 1.0 * 3600
                ),
                MilestoneEntry(
                    title : "Blood Sugar Levels Drop",
                    description : "Insulin levels decrease, improving glucose control.",
                    warnings : nil,
                    advice : "Drink water or herbal tea to stay hydrated.",
                    timestampInSeconds : 1.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Fat Burning Begins",
                    description : "The body starts using fat as an energy source.",
                    warnings : "Some may experience slight fatigue.",
                    advice : "A light walk can help boost metabolism.",
                    timestampInSeconds : 2.0 * 3600
                ),
                MilestoneEntry(
                    title : "Metabolism Shift",
                    description : "Your body begins transitioning to burning stored fat for energy.",
                    warnings : nil,
                    advice : "Staying active can enhance fat burn.",
                    timestampInSeconds : 2.0 * 3600
                ),
                MilestoneEntry(
                    title : "Energy from Fat",
                    description : "Your body starts breaking down fat for energy, reducing insulin dependence.",
                    warnings : "You might feel an energy dip before adjustment.",
                    advice : "Hydration and light stretching can help.",
                    timestampInSeconds : 2.0 * 3600
                ),
                MilestoneEntry(
                    title : "Initial Hunger Signals",
                    description : "Your ghrelin levels rise, triggering hunger pangs.",
                    warnings : "Hunger is temporary and will fade.",
                    advice : "Stay occupied with non-food activities.",
                    timestampInSeconds : 2.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Metabolism Remains Stable",
                    description : "Despite fasting, your metabolism continues functioning normally.",
                    warnings : nil,
                    advice : "Light physical activity can boost fat burning.",
                    timestampInSeconds : 3.0 * 3600
                ),
                MilestoneEntry(
                    title : "Glycogen Depletion Begins",
                    description : "Your body starts consuming stored glucose for energy.",
                    warnings : nil,
                    advice : "Avoid excessive caffeine to prevent energy dips.",
                    timestampInSeconds : 3.0 * 3600
                ),
                MilestoneEntry(
                    title : "Glycogen Usage Increases",
                    description : "Your body starts using stored glucose for energy.",
                    warnings : nil,
                    advice : "Avoid high-intensity workouts to prevent energy dips.",
                    timestampInSeconds : 3.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Hunger Hormones Fluctuate",
                    description : "Your body is adjusting to lower insulin levels and reduced food intake.",
                    warnings : "You may feel hunger pangs.",
                    advice : "Drinking water or tea can help suppress hunger.",
                    timestampInSeconds : 4.0 * 3600
                ),
                MilestoneEntry(
                    title : "Appetite Regulation Begins",
                    description : "Ghrelin levels start adjusting, reducing hunger cravings.",
                    warnings : nil,
                    advice : "Deep breathing exercises can help manage cravings.",
                    timestampInSeconds : 4.0 * 3600
                ),
                MilestoneEntry(
                    title : "Early Adaptation Phase",
                    description : "Your digestive system is resting, allowing for metabolic improvements.",
                    warnings : "Some may feel lightheaded.",
                    advice : "Ensure you are hydrated and consume electrolytes if needed.",
                    timestampInSeconds : 4.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Initiates",
                    description : "Your body starts utilizing fat for energy.",
                    warnings : "You might feel a slight drop in energy levels.",
                    advice : "Stay hydrated and take deep breaths if feeling tired.",
                    timestampInSeconds : 4.0 * 3600
                ),
                MilestoneEntry(
                    title : "Mild Fat Burning Begins",
                    description : "Your body starts dipping into fat stores for energy.",
                    warnings : "You may feel a slight drop in energy.",
                    advice : "Drink a glass of water if you feel sluggish.",
                    timestampInSeconds : 4.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Initiates",
                    description : "Your body starts relying on fat stores for energy.",
                    warnings : "Mild cravings may occur but will subside.",
                    advice : "Stay hydrated to support fat metabolism.",
                    timestampInSeconds : 4.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Metabolism Remains Stable",
                    description : "Fasting does not slow metabolism at this stage.",
                    warnings : nil,
                    advice : "Mild physical activity can help boost fat oxidation.",
                    timestampInSeconds : 5.0 * 3600
                ),
                MilestoneEntry(
                    title : "Increased Mental Clarity",
                    description : "Your brain benefits from stable glucose and mild ketone production.",
                    warnings : nil,
                    advice : "Use this time for focus-heavy tasks.",
                    timestampInSeconds : 5.0 * 3600
                ),
                MilestoneEntry(
                    title : "Insulin Levels Decline",
                    description : "Lower insulin levels allow for improved fat metabolism.",
                    warnings : "Cravings might appear but will pass.",
                    advice : "Focus on deep breathing exercises to curb cravings.",
                    timestampInSeconds : 5.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Fat Burning Accelerates",
                    description : "Your body is relying more on fat stores for energy.",
                    warnings : "Energy levels may fluctuate.",
                    advice : "Light movement can help sustain energy.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Glucose Depletion Starts",
                    description : "Your body begins to use stored glucose more efficiently.",
                    warnings : "You may feel slightly irritable or fatigued.",
                    advice : "Avoid caffeine if prone to energy crashes.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Deeper Glycogen Depletion",
                    description : "More glucose is used up, making fat the preferred energy source.",
                    warnings : "Energy levels may fluctuate as the body adapts.",
                    advice : "Listen to your body and rest if needed.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Insulin Drops Further",
                    description : "Lower insulin levels promote fat breakdown.",
                    warnings : nil,
                    advice : "Continue drinking water to aid the process.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Metabolic Boost",
                    description : "Your metabolism is becoming more efficient at using fat for fuel.",
                    warnings : "Mild headaches can occur.",
                    advice : "Consuming electrolytes may help.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Gradual Fat Adaptation",
                    description : "Your body starts shifting towards fat for energy but at a slower pace than daily fasting.",
                    warnings : "It may take longer to see noticeable benefits.",
                    advice : "Stay consistent and patient for best results.",
                    timestampInSeconds : 6.0 * 3600
                ),
                MilestoneEntry(
                    title : "Ketone Production Begins",
                    description : "Your body produces ketones as an alternative energy source.",
                    warnings : "You might feel mild mental fog as your body adjusts.",
                    advice : "Stay hydrated and include electrolytes if needed.",
                    timestampInSeconds : 6.0 * 3600
                ),
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Fat Oxidation Increases",
                    description : "Your body relies more on fat for energy.",
                    warnings : nil,
                    advice : "Consider light stretching or walking to enhance fat burning.",
                    timestampInSeconds : 7.0 * 3600
                ),
                MilestoneEntry(
                    title : "Ketone Production Begins",
                    description : "Your body starts making ketones, providing an alternative fuel source.",
                    warnings : "You might experience increased thirst.",
                    advice : "Replenish electrolytes with a pinch of salt in water.",
                    timestampInSeconds : 7.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Increases",
                    description : "Your body starts transitioning from glucose to fat as a fuel source.",
                    warnings : nil,
                    advice : "A walk or light stretching can help boost metabolism.",
                    timestampInSeconds : 7.0 * 3600
                )
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Hormonal Balance Improves",
                    description : "Hormones like norepinephrine increase, helping fat mobilization.",
                    warnings : nil,
                    advice : "Consider adding electrolytes if you feel fatigued.",
                    timestampInSeconds : 8.0 * 3600
                ),
                MilestoneEntry(
                    title : "Metabolism Stays Active",
                    description : "Your metabolic rate remains steady without significant drops.",
                    warnings : nil,
                    advice : "Mild exercise can help enhance metabolic efficiency.",
                    timestampInSeconds : 8.0 * 3600
                ),
                MilestoneEntry(
                    title : "Autophagy Begins",
                    description : "Cells start breaking down and removing damaged components.",
                    warnings : nil,
                    advice : "If comfortable, extend fasting for deeper autophagy.",
                    timestampInSeconds : 8.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Ramps Up",
                    description : "Your body prioritizes fat as its main energy source.",
                    warnings : nil,
                    advice : "If you feel weak, a quick stretch can boost circulation.",
                    timestampInSeconds : 8.0 * 3600
                )
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Mild Autophagy Starts",
                    description : "Your body starts recycling old or damaged cells.",
                    warnings : nil,
                    advice : "Extend fasting if you want deeper autophagy effects.",
                    timestampInSeconds : 9.0 * 3600
                ),
                MilestoneEntry(
                    title : "Autophagy Triggers Slightly",
                    description : "Minor autophagy begins, though longer fasting leads to stronger effects.",
                    warnings : nil,
                    advice : "Prioritize whole, nutritious foods when breaking the fast.",
                    timestampInSeconds : 9.0 * 3600
                )
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Blood Sugar Control Improves",
                    description : "Your body starts regulating glucose levels better.",
                    warnings : nil,
                    advice : "Cutting down on processed carbs will enhance these effects.",
                    timestampInSeconds : 10.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Intensifies",
                    description : "Your body increasingly shifts to fat as a fuel source.",
                    warnings : "You may feel mild energy fluctuations.",
                    advice : "Stay hydrated and consider light movement.",
                    timestampInSeconds : 10.0 * 3600
                ),
                MilestoneEntry(
                    title : "Growth Hormone Levels Rise",
                    description : "Helps preserve muscle and promote fat loss.",
                    warnings : nil,
                    advice : "Great time for light strength training if energy allows.",
                    timestampInSeconds : 10.0 * 3600
                ),
                MilestoneEntry(
                    title : "Metabolism Optimization",
                    description : "Your metabolic rate stays stable, improving fat oxidation.",
                    warnings : nil,
                    advice : "Keep drinking water to maintain hydration.",
                    timestampInSeconds : 10.0 * 3600
                ),
                MilestoneEntry(
                    title : "Metabolism Remains Active",
                    description : "Contrary to myths, fasting does not slow down metabolism at this stage.",
                    warnings : nil,
                    advice : "Drink black coffee or green tea for an energy boost.",
                    timestampInSeconds : 10.0 * 3600
                )
            ].shuffled().first!,

            MilestoneEntry(
                title : "Body Prepares for Refeeding",
                description : "Your digestive enzymes prepare for incoming nutrients.",
                warnings : "Avoid breaking the fast with high sugar foods.",
                advice : "Opt for protein and healthy fats when eating again.",
                timestampInSeconds : 11.0 * 3600
            ),

            [
                MilestoneEntry(
                    title : "Ketosis Begins",
                    description : "Your body shifts into ketosis, burning fat as its primary fuel source.",
                    warnings : "You may experience keto flu symptoms.",
                    advice : "Electrolytes can help with fatigue and headaches.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Adaptation Starts",
                    description : "Your body is adapting to use fat more efficiently for energy.",
                    warnings : nil,
                    advice : "Consume enough electrolytes to maintain balance.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Transition to Fat Burning",
                    description : "Stored fat starts being used as your body's main fuel source.",
                    warnings : "You may feel sluggish as your body adapts.",
                    advice : "Drinking bone broth can ease adaptation symptoms.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Mild Autophagy Begins",
                    description : "Your body initiates cell cleanup and repair.",
                    warnings : "Deeper autophagy happens in longer fasting periods.",
                    advice : "Consider fasting longer on some days for additional benefits.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning in Full Effect",
                    description : "Your body primarily uses fat for energy as glycogen is nearly depleted.",
                    warnings : "Some may experience mild fatigue or cold hands/feet.",
                    advice : "Stay hydrated and consider adding electrolytes.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Ketone Production Increases",
                    description : "Your body enters a stronger state of ketosis.",
                    warnings : "You might experience mild mental fog initially.",
                    advice : "Drinking electrolyte water can help.",
                    timestampInSeconds : 12.0 * 3600
                ),
                MilestoneEntry(
                    title : "Inflammation Reduces",
                    description : "Fasting decreases oxidative stress and inflammation markers.",
                    warnings : nil,
                    advice : "Consider extending fasting occasionally for long-term benefits.",
                    timestampInSeconds : 12.0 * 3600
                ),
            ].shuffled().first!,

            MilestoneEntry(
                title : "Body Prepares for Refeeding",
                description : "Your digestion system prepares for nutrient absorption.",
                warnings : "Avoid high-carb or sugary foods when breaking fast.",
                advice : "Opt for proteins, healthy fats, and fiber-rich foods.",
                timestampInSeconds : 13.5 * 3600
            ),

            [
                MilestoneEntry(
                    title : "Fat Burning at Maximum",
                    description : "Your body is now running almost exclusively on fat for energy.",
                    warnings : "You may feel occasional energy dips before adapting.",
                    advice : "Drink water, herbal tea, or black coffee to stay alert.",
                    timestampInSeconds : 14.0 * 3600
                ),
                MilestoneEntry(
                    title : "Deep Ketosis",
                    description : "Your liver produces a high amount of ketones, fueling the brain and muscles.",
                    warnings : "Some may experience temporary dizziness or light-headedness.",
                    advice : "If feeling weak, try adding a pinch of salt to water.",
                    timestampInSeconds : 14.0 * 3600
                ),
                MilestoneEntry(
                    title : "Deep Cellular Repair Begins",
                    description : "Autophagy accelerates, improving cellular regeneration.",
                    warnings : nil,
                    advice : "Consider extending fasting occasionally for added benefits.",
                    timestampInSeconds : 14.0 * 3600
                ),
            ].shuffled().first!,

            MilestoneEntry(
                title : "Body Prepares for Refeeding",
                description : "Digestive enzymes increase to process incoming nutrients.",
                warnings : "Avoid breaking the fast with high sugar or processed foods.",
                advice : "Start with proteins and healthy fats to avoid energy crashes.",
                timestampInSeconds : 15.5 * 3600
            ),

            [
                MilestoneEntry(
                    title : "Autophagy Increases",
                    description : "Your body starts breaking down and recycling damaged cells for energy.",
                    warnings : "You may experience mild fatigue.",
                    advice : "Get adequate rest to support this process.",
                    timestampInSeconds : 16.0 * 3600
                ),
                MilestoneEntry(
                    title : "Cellular Repair Begins",
                    description : "Your body starts repairing damaged cells and regenerating new ones.",
                    warnings : nil,
                    advice : "Prioritize rest and hydration to enhance this process.",
                    timestampInSeconds : 16.0 * 3600
                ),
                MilestoneEntry(
                    title : "Deep Cellular Cleanup",
                    description : "The body starts removing dysfunctional components from cells.",
                    warnings : "You might feel temporary weakness as your body detoxifies.",
                    advice : "Light stretching and deep breathing exercises can help.",
                    timestampInSeconds : 16.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fat Burning Peaks",
                    description : "Your body is now highly efficient at using fat for fuel.",
                    warnings : nil,
                    advice : "Light exercise can enhance these effects.",
                    timestampInSeconds : 16.0 * 3600
                ),
                MilestoneEntry(
                    title : "Autophagy Boosts",
                    description : "Your body removes old or damaged cells, improving overall function.",
                    warnings : nil,
                    advice : "Consider fasting longer occasionally for even deeper effects.",
                    timestampInSeconds : 16.0 * 3600
                ),
                MilestoneEntry(
                    title : "Ketone Levels Peak",
                    description : "Your brain and body are fully fueled by ketones.",
                    warnings : "Mild fatigue may occur if you’re not fully adapted.",
                    advice : "If feeling weak, replenish electrolytes with salt water.",
                    timestampInSeconds : 16.0 * 3600
                )
            ].shuffled().first!,

            [
                MilestoneEntry(
                    title : "Autophagy at Full Speed",
                    description : "Cellular repair mechanisms are in full swing, clearing out damaged cells.",
                    warnings : nil,
                    advice : "Extended fasting beyond this point may further enhance autophagy.",
                    timestampInSeconds : 18.0 * 3600
                ),
                MilestoneEntry(
                    title : "Fast Ends",
                    description : "Time to break the fast with a nutritious meal.",
                    warnings : nil,
                    advice : "Avoid high sugar foods to prevent an energy crash.",
                    timestampInSeconds : 18.0 * 3600
                ),
                MilestoneEntry(
                    title : "Hormonal Optimization",
                    description : "Human growth hormone levels increase, supporting fat loss and muscle maintenance.",
                    warnings : nil,
                    advice : "This is a great time for strength training if energy allows.",
                    timestampInSeconds : 18.0 * 3600
                ),
            ].shuffled().first!,

            MilestoneEntry(
                title : "Anti-Inflammatory Effects Strengthen",
                description : "Your body's inflammation markers significantly reduce.",
                warnings : nil,
                advice : "Fasting longer occasionally may enhance longevity benefits.",
                timestampInSeconds : 19.0 * 3600
            ),
            MilestoneEntry(
                title : "Muscle Preservation with Growth Hormone",
                description : "Human growth hormone levels rise significantly, preserving muscle mass.",
                warnings : nil,
                advice : "Strength training can help maintain lean muscle during OMAD.",
                timestampInSeconds : 20.0 * 3600
            ),
            MilestoneEntry(
                title : "Brain Function Enhances",
                description : "Ketones provide a steady energy source for the brain, improving focus.",
                warnings : nil,
                advice : "Use this mental clarity for productive tasks.",
                timestampInSeconds : 22.0 * 3600
            )
        ].flatMap { $0 }
}
