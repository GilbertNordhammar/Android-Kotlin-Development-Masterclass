package com.example.quizapp

object Constants {

    const val USER_NAME = "user_name"
    const val TOTAL_QUESTIONS = "total_question"
    const val CORRECT_ANSWERS = "correct_answers"

    fun getQuestions() : ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        questionsList.add(Question(
            1,
            "What country does this flag belong to?",
            R.drawable.swedens_flag,
            "Denmark",
            "USA",
            "Sweden",
            "Turkmenistan",
            3
        ))

        questionsList.add(Question(
            2,
            "What game is this from?",
            R.drawable.world_of_warcraft,
            "World of Warcraft",
            "The Sims 2",
            "Warcraft 3",
            "League of Legends",
            1
        ))

        questionsList.add(Question(
            3,
            "Who is this?",
            R.drawable.chuck_norris,
            "Dwayne Johnson",
            "Robert Downey Jr",
            "Mike Shinoda",
            "Chuck Norris",
            4
        ))

        return questionsList
    }
}