package microsleader.george.myvehiclesinfo

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import microsleader.george.myvehiclesinfo.classes.Vehicle

class FireStoreMethods {


    companion object {
        fun loginGmail(context: Context, account: GoogleSignInAccount): String {

            var msg = "";
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        // Successful login
                        val auth = FirebaseAuth.getInstance()
                        val uid = auth.currentUser!!.uid
                        val user = authTask.result?.user
                        // Create a new user with a first, middle, and last name
                        val userMap = hashMapOf(
                            "id" to account.id,
                            "idToken" to account.idToken,
                            "account" to account.account,
                            "displayName" to account.displayName,
                            "email" to account.email,
                            "givenName" to account.givenName,
                            "familyName" to account.familyName,
                            "isExpired" to account.isExpired,
                            "photoUrl" to account.photoUrl,
                            "serverAuthCode" to account.serverAuthCode,

                            )
                        val db = Firebase.firestore
                        // Add a new document with a generated ID
                        db.collection("Users")
                            .document(uid).set(userMap)
                            .addOnSuccessListener {
                                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $uid")
                            }
                            .addOnFailureListener { e ->
                                Log.w(ContentValues.TAG, "Error adding document", e)
                            }

                    } else {
                       msg = authTask.exception.toString()
                        // Failed login
                        // Handle the error
                    }
                }

            return msg
        }



        fun addVehicle(context: Context, vehicle: Vehicle): String {
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


}