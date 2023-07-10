package microsleader.george.myvehiclesinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import microsleader.george.myvehiclesinfo.ui.theme.MyVehiclesInfoTheme
import microsleader.george.myvehiclesinfo.utils.getActivity
import java.util.concurrent.TimeUnit


val auth = FirebaseAuth.getInstance()
var storedVerificationId: String = ""



class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current;
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                // User is logged in
                context.startActivity(Intent(context, MainMenuActivity::class.java))
            }





            val loginDialogState = remember { mutableStateOf(false) }
            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task,context)
            }
            Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen(loginDialogState, googleSignInLauncher)
                    LoginDialog(loginDialogState)
                }

        }
    }




}

@Composable
fun LoginDialog(loginDialogState: MutableState<Boolean>) {

    if (loginDialogState.value) {
        Dialog(

            onDismissRequest = { loginDialogState.value = false },
            content = {
                CompleteDialogContent()
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = true
            )
        )
    }
}




@Composable
fun LoginScreen(
    loginDialogState: MutableState<Boolean>,
    googleSignInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    val context = LocalContext.current
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken("861107820797-sk3ltj07a8ag2psheu16ci2f2ithtudb.apps.googleusercontent.com") // Replace with your web client ID from Firebase
            .requestIdToken("861107820797-9d0jm07mitg68jns6p1rqvv93777jf5v.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                onClick = {googleSignInLauncher.launch(googleSignInClient.signInIntent)   }
            ) {
                Text(text = "Log in with Gmail")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { loginDialogState.value = true}
            ) {
                Text(text = "Log in via Phone Number")
            }
        }
        LoginDialog(loginDialogState)

    }
}


@Composable
fun CompleteDialogContent() {
    val context = LocalContext.current
    var phoneNumber by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var otp by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isOtpVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(1f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Login with phone number", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                ),
                placeholder = { Text("Enter phone number") },
                value = phoneNumber,
                onValueChange = {
                    if (it.text.length <= 10) phoneNumber = it
                },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 4.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            if(isOtpVisible) {
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ),
                    value = otp,
                    placeholder = { Text("Enter otp") },
                    onValueChange = { otp = it },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            if(!isOtpVisible) {
                Button(
                    onClick = { onPhoneLoginClicked(context,phoneNumber.text) {
                        Log.d("phoneBook","setting otp visible")
                        isOtpVisible = true
                    }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Send otp", color = Color.White)
                }
            }


            if(isOtpVisible) {
                Button(
                    onClick = { verifyPhoneNumberWithCode(context, storedVerificationId,otp.text) },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Verify", color = Color.White)
                }
            }


        }
    }
}

private fun verifyPhoneNumberWithCode(context: Context,verificationId: String, code: String) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(context,credential)
}

fun onPhoneLoginClicked (context: Context, phoneNumber: String,onCodeSent: () -> Unit) {

    auth.setLanguageCode("en")
    val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("phoneBook","verification completed")
            signInWithPhoneAuthCredential(context,credential)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("phoneBook","verification failed" + p0)
        }

        override fun onCodeSent(verificationId: String,
                                token: PhoneAuthProvider.ForceResendingToken) {
            Log.d("phoneBook","code sent" + verificationId)
            storedVerificationId = verificationId
            onCodeSent()
        }

    }
    val options = context.getActivity()?.let {
        PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+30"+phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(it)
            .setCallbacks(callback)
            .build()
    }
    if (options != null) {
        Log.d("phoneBook",options.toString())
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}

private fun signInWithPhoneAuthCredential(context: Context, credential: PhoneAuthCredential) {
    context.getActivity()?.let {
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    Log.d("phoneBook","logged in")
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.d("phoneBook","wrong otp")
                    }
                    // Update UI
                }
            }
    }
}



private fun handleSignInResult(task: Task<GoogleSignInAccount>, context: Context) {
    try {
        val account = task.getResult(ApiException::class.java)
        val msg = FireStoreMethods.loginGmail(context,account)
        if (account != null) {
            if (msg.isEmpty())
                context.startActivity(Intent(context, MainMenuActivity::class.java))
            else
                Toast.makeText(context, msg,Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Δεν πραγματοποιήθηκε η σύνδεση με το gmail",Toast.LENGTH_LONG).show();
        }
    } catch (e: ApiException) {
        //e.printStackTrace()
        //println(e.message)
        Toast.makeText(context, e.message,Toast.LENGTH_LONG).show();

        // Sign-in failed, handle error
    }
}


@Composable
fun gmailLogin() {
    val context = LocalContext.current
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("861107820797-sk3ltj07a8ag2psheu16ci2f2ithtudb.apps.googleusercontent.com") // Replace with your web client ID from Firebase
                //861107820797-9d0jm07mitg68jns6p1rqvv93777jf5v.apps.googleusercontent.com
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // Rest of your code...
}



@Preview(showBackground = true)
@Composable
fun DefaultLoginPreview() {
    MyVehiclesInfoTheme () {

       // CompleteDialogContent()
        val context = LocalContext.current;

        val googleSignInLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, context)
        }
        val loginDialogState = remember { mutableStateOf(false) }
        LoginScreen(loginDialogState,googleSignInLauncher)
    }
}

