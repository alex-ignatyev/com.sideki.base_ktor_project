package com.base_ktor_project.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId val username: String,
    val password: String,
    val salt: String = "",
    @BsonId val id: ObjectId = ObjectId()
)
