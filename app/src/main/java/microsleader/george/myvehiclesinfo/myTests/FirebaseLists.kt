package microsleader.george.myvehiclesinfo.myTests

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import microsleader.george.myvehiclesinfo.R
import microsleader.george.myvehiclesinfo.classes.Vehicle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class FirebaseLists {
    val BASE_URL = "https://jsonplaceholder.typicode.com/"

    class TodoViewModel : ViewModel() {
        private val _todoList = mutableStateListOf<CardBrandsAndModels>()
        var errorMessage: String by mutableStateOf("")
        val todoList: List<CardBrandsAndModels>
            get() = _todoList

        fun getTodoList1() {
            viewModelScope.launch {
                val apiService = APIService.getInstance()
                try {
                    _todoList.clear()
                    _todoList.addAll(apiService.getTodos())

                } catch (e: Exception) {
                    errorMessage = e.message.toString()
                }
            }
        }

        companion object {
            fun getTodoList() {
                TODO("Not yet implemented")
            }
        }
    }

    data class CardBrandsAndModels(
        var id: Int,
        var brand: String,
        var model: String
    )


    interface APIService {
        
        @GET("cars")
        suspend fun getTodos(): List<CardBrandsAndModels>

        companion object {
            var apiService: APIService? = null
            fun getInstance(): APIService {
                if (apiService == null) {
                    val BASE_URL = "https://private-2eaa0a-carsapi1.apiary-mock.com/"
                    apiService = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(APIService::class.java)
                }
                return apiService!!
            }
        }
    }





    fun addBrandsList(context: Context, vehicle: Vehicle): String {
        var msg = "";

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid

        val db = Firebase.firestore
        // Add a new document with a generated ID
        val vehiclesMap = hashMapOf(
            "brand" to vehicle.brand,
            "model" to vehicle.model,
            "year" to vehicle.year,
            "kivika" to vehicle.kivika,
            "hp" to vehicle.hp,
            "userID" to uid,
        )

        db.collection("Vehicles")
            .document().set(vehiclesMap)
            .addOnSuccessListener {
                msg = context.resources.getString (R.string.success_vehicle_store)
                //  msg = R.string.success_vehicle_store
            }
            .addOnFailureListener { e ->
                msg = "Δεν πραγματοποιήθηκε η αποθήκευση \n " + e.message;
                // msg = R.string.save_error;
            }



        return msg
    }


    fun addModelsList(context: Context, vehicle: Vehicle): String {
        var msg = "";

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid

        val db = Firebase.firestore
        // Add a new document with a generated ID
        val vehiclesMap = hashMapOf(
            "brand" to vehicle.brand,
            "model" to vehicle.model,
            "year" to vehicle.year,
            "kivika" to vehicle.kivika,
            "hp" to vehicle.hp,
            "userID" to uid,
        )

        db.collection("Vehicles")
            .document().set(vehiclesMap)
            .addOnSuccessListener {
                msg = context.resources.getString (R.string.success_vehicle_store)
                //  msg = R.string.success_vehicle_store
            }
            .addOnFailureListener { e ->
                msg = "Δεν πραγματοποιήθηκε η αποθήκευση \n " + e.message;
                // msg = R.string.save_error;
            }



        return msg
    }


}