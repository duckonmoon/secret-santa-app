package com.softserveinc.test.secretsanta.entity

class Member {
    var name: String = "Jack"
    var imagePath: String = ""

    constructor()

    constructor(name: String, imagePath: String) {
        this.name = name
        this.imagePath = imagePath
    }
}