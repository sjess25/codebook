package com.coders.codebook

class answer (owner: String, answer: String){

    var owner: String = ""
    var answer: String = ""


    init {
        this.owner = owner
        this.answer = answer
    }

    fun insert(owner: String, answer: String){
        this.owner = owner
        this.answer = answer
    }
}