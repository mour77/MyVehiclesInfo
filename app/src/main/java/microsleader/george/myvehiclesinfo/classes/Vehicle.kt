package microsleader.george.myvehiclesinfo.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//class Vehicle(brand: String, model: String, year: Int, kivika: Int, hp: Int) {
//    var brand ="";
//    var model ="";
//    var year = 0;
//    var kivika = 0;
//    var hp = 0;
//    // initializer block
//    init {
//        this.brand = brand
//        this.model = model
//        this.year = year
//        this.kivika = kivika
//        this.hp = hp
//
//    }
//
//}

data class Vehicle(
    var brand: String? = null,
    var model: String? = null,
    var year: Int? = 0,
    var kivika: Int? = 0,
    var hp: Int? = 0,
    var userID: String? = null

)

class VehicleViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>> = _vehicles

    init {
        loadVehicles()
    }
    val vehicles1: Flow<List<Vehicle>> = flow {
        try {
            val snapshot = firestore.collection("Vehicles").get().await()
            val vehicleList = snapshot.documents.mapNotNull { document ->
                document.toObject(Vehicle::class.java)
            }
            emit(vehicleList)
        } catch (e: Exception) {
            // Handle the exception
        }
    }
    private fun loadVehicles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("Vehicles").get().await()
                val vehicleList = snapshot.documents.mapNotNull { document ->
                    document.toObject(Vehicle::class.java)
                }
                _vehicles.postValue(vehicleList)
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }
}

