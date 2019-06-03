package com.coders.codebook

object dataUser {

    private var nickName: String? = null
    private var password: String? = null

    fun create(nickname: String, password: String) {
        this.nickName = nickname
        this.password = password
    }

    fun getnickName (): String? {
        return nickName
    }

    fun getPassword (): String? {
        return password
    }
}