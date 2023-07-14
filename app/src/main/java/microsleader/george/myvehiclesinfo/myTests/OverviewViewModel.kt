package microsleader.george.myvehiclesinfo.myTests

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    val _status = MutableLiveData<String>()
    val _resultBrands = MutableLiveData<List<Brands>>()
    val _resultModels = MutableLiveData<List<Models>>()

    // The external immutable LiveData for the request status
    val status: LiveData<String> = _status
    val resultBrands: LiveData<List<Brands>> = _resultBrands
    val resultModels: LiveData<List<Models>> = _resultModels
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getBrands()
        getModels()
    }




    fun getBrands() :List<Brands>?{
        viewModelScope.launch {
            try {
                //ΜΕΘΟΔΟΣ ΠΟΥ ΒΑΖΩ ΤΑ ΜΟΝΤΕΛΑ ΚΑΙ ΤΙΣ ΜΑΡΚΕΣ ΣΤΗ ΒΑΣΗ
                val listResult = MarsApi.retrofitService.getBrands()
              //  _status.value = "Success: ${listResult.size} Mars photos retrieved"
                _resultBrands.value = MarsApi.retrofitService.getBrands()

                val db = Firebase.firestore


                for (items in _resultBrands.value!!){
                    items.name?.let { Log.d(TAG, it) }
                    val brandMap = hashMapOf(
                        "id" to items.id,
                        "name" to items.name,
                    )

                    db.collection("Brands")
                        .document(items.id.toString()).set(brandMap)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot added ")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                    items.name?.let { Log.d(TAG, it) }
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
        return   _resultBrands.value

    }


    fun getModels() :List<Models>?{
        viewModelScope.launch {
            try {
                //ΜΕΘΟΔΟΣ ΠΟΥ ΒΑΖΩ ΤΑ ΜΟΝΤΕΛΑ ΚΑΙ ΤΙΣ ΜΑΡΚΕΣ ΣΤΗ ΒΑΣΗ
                val listResult = MarsApi.retrofitService.getModels()
              //  _status.value = "Success: ${listResult.size} Mars photos retrieved"
                _resultModels.value = MarsApi.retrofitService.getModels()

                val db = Firebase.firestore


                for (items in _resultModels.value!!){
                    items.make?.let { Log.d(TAG, it) }
                    val modelMap = hashMapOf(
                        "id" to items.id,
                        "make" to items.make,
                        "model" to items.model,
                    )

                    db.collection("Models")
                        .document(items.id.toString()).set(modelMap)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot added ")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                    items.model?.let { Log.d(TAG, it) }
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
        return   _resultModels.value

    }
}
