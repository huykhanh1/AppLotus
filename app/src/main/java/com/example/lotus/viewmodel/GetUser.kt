//import com.example.lotus.api.MainRetrofit
//import com.example.lotus.model.UserModel
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.*
//
//fun getUserFlow(): Flow<UserModel> = flow {
//    val users = MainRetrofit.instance.getUsers() // Gọi API để lấy danh sách người dùng
//    users.forEach { user ->
//        emit(user) // Phát ra từng user trong danh sách
//    }
//}
//
//fun main() = runBlocking {
//    try {
//        getUserFlow().collect { user ->
//            println("User: ${user.userName}") // In ra tên người dùng
//        }
//    } catch (e: Exception) {
//        println("Error: ${e.localizedMessage}")
//    }
//}
