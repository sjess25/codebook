package com.coders.codebook

object listChallenges {

    val miChallenges: ArrayList<technology> = ArrayList()

    fun insertChallenge(challenge: technology){
        miChallenges.add(challenge)

    }

    fun getList(): ArrayList<technology>{
        return miChallenges
    }
}