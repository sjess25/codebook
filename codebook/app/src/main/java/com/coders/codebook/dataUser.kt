package com.coders.codebook

object dataUser {

    private var nickName: String = ""
    private var password: String = ""
    private var fullName: String? = null
    private var id: Int = -1
    private var teacher: Boolean = false

    fun create(nickname: String, password: String) {
        this.nickName = nickname
        this.password = password
    }

    fun getNickName (): String {
        return nickName
    }

    fun getPassword (): String {
        return password
    }

    fun getTeacher (): Boolean {
        return teacher
    }

    fun getID (): Int {
        return id
    }

    fun setTeacher (teacher: Boolean) {
        this.teacher = teacher
    }

    fun setID (id: Int) {
        this.id = id
    }

    fun setName(name: String){
        this.fullName = name
    }

    override fun toString(): String {
        return "ID:$id,NickName:$nickName,teacher:$teacher"
    }
}