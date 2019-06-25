package com.coders.codebook

import android.util.Log

object userChallengeList {

    var allChallenge = LinkedHashMap<Int,ArrayList<technology>>()

    fun insertListChallenge(index: Int){

        var challenge: ArrayList<technology> = ArrayList()

        for (i in 0.. (listChallenges.getList().size - 1)) {
            challenge.add(listChallenges.getList().get(i))
        }
        Log.d("all challenge", this.allChallenge.size.toString())
        this.allChallenge[index] = challenge
    }

    fun getList(index: Int): ArrayList<technology> {
        return allChallenge[index]!!
    }
    fun updateList(list: ArrayList<technology>, index: Int){
       // userChallengeList.allChallenge[index].clear()
        //userChallengeList.allChallenge.add(index, list)
    }

    fun getIndex(name: String): Int{

        when (name) {
            "Activos"-> return 0
            "C" -> return 1
            "Java" -> return 2
            "Ruby" -> return 3
            "Prolog" -> return 4
            "Asesor" -> return 5
        }
        return -1
    }
}