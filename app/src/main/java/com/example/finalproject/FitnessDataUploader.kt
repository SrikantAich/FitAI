import android.content.Context
import android.widget.Toast
import com.example.finalproject.getAllFitnessData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun uploadFitnessData(context: Context) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    if (userId != null) {
        val fitnessData = getAllFitnessData(context)
        db.collection("users")
            .document(userId)
            .set(fitnessData)
            .addOnSuccessListener {
                Toast.makeText(context, "Fitness data uploaded successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to upload fitness data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
    }
}
