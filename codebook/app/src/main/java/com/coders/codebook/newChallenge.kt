package com.coders.codebook

import org.json.JSONObject

object newChallenge {

    var technologie: String = ""
    var title: String = ""
    var description: String =  ""
    var difficulty: Int = 0
    var timeLimit: Int = 0
    var owner = 0
    var ref1: String = ""
    var ref2: String = ""
    var ref3: String = ""
    var answer: String = ""
    var likes: Int = 0
    var dislikes: Int = 0


    fun create(technologie: String, title: String, description: String, difficulty: Int, timeLimit: Int, owner: Int, ref1: String, ref2: String, ref3: String) {

        this.technologie = technologie
        this.title = title
        this.description =  description
        this.difficulty = difficulty
        this.timeLimit = timeLimit
        this.owner = owner
        this.ref1 = ref1
        this.ref2 = ref2
        this.ref3 = ref3
    }

    fun setAnswerChallenge(answer: String) {

        this.answer = answer
    }

    fun setInfoChallenge(title: String, description: String, difficulty: Int, timeLimit: Int, ref1: String, ref2: String, ref3: String, owner: Int, likes: Int, dislikes: Int) {

        this.title = title
        this.description =  description
        this.difficulty = difficulty
        this.timeLimit = timeLimit
        this.ref1 = ref1
        this.ref2 = ref2
        this.ref3 = ref3
        this.owner = owner
        this.likes = likes
        this.dislikes = dislikes

    }

    fun getJsonSENDpost(): JSONObject {

        val params = LinkedHashMap<String,String>()
        params["ID"] = "3"
        params["Technologie"] = technologie
        params["Title"] = title
        params["Description"] = description
        params["Difficulty"] = difficulty.toString()
        params["TimeLimit"] = timeLimit.toString()
        params["Owner"] = owner.toString()
        params["Answer"] = answer
        params["ref1"] = ref1
        params["ref2"] = ref2
        params["ref3"] = ref3

        val jsonChallenge = JSONObject(params)

        return jsonChallenge
    }

}