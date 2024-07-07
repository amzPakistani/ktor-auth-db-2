package example.com.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username:String,
    val password:String,
    val salt:String,
    @BsonId val id:ObjectId = ObjectId()
)