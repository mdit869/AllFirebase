package com.example.project13_allfirebase

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project13_allfirebase.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var dbRel: DatabaseReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo DatabaseReference
        dbRel = FirebaseDatabase.getInstance().getReference("FireBaseRaltime")

        getDataFirebase()

        binding.btnUpdate.setOnClickListener {
            val id = intent.getStringExtra("id") ?: ""
            val name = binding.edtUpdateName.text.toString()
            val stt = binding.edtUpdateStt.text.toString()
            val img = intent.getStringExtra("img") ?: ""

            if (name.isNotEmpty() && stt.isNotEmpty()) {
                if (id.isNotEmpty()) {
                    if (imageUri != null) {
                        uploadNewImageAndUpdateData(imageUri!!, id, name, stt)
                    } else {
                        // Nếu không có hình ảnh mới, chỉ cập nhật thông tin mà không thay đổi hình ảnh
                        updateDataInDatabase(id, name, stt, img)
                    }
                } else {
                    Toast.makeText(this, "ID is missing", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadNewImageAndUpdateData(imageUri: Uri, id: String, name: String, stt: String) {
        val fileName = System.currentTimeMillis().toString()
        val fileRef = FirebaseStorage.getInstance().getReference("FireBaseImg").child(fileName)

        fileRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updateDataInDatabase(id, name, stt, imageUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateDataInDatabase(id: String, name: String, stt: String, imgUrl: String) {
        val dbRef = dbRel.child(id)

        val fireData = mapOf(
            "name" to name,
            "stt" to stt,
            "img" to imgUrl
        )

        dbRef.updateChildren(fireData)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getDataFirebase() {
        val name = intent.getStringExtra("name") ?: ""
        val stt = intent.getStringExtra("stt") ?: ""
        val img = intent.getStringExtra("img") ?: ""

        binding.edtUpdateName.setText(name)
        binding.edtUpdateStt.setText(stt)

        binding.imgUpdate.setOnClickListener {
            laurch.launch("image/*")
        }

        if (img.startsWith("http")) {
            Glide.with(this)
                .load(img)
                .into(binding.imgUpdate)
        } else {
            val imageResourceId = resources.getIdentifier(img, "drawable", packageName)
            if (imageResourceId != 0) {
                binding.imgUpdate.setImageResource(imageResourceId)
            } else {
                Toast.makeText(this, "Image resource not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val laurch = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
        binding.imgUpdate.setImageURI(uri)
    }
}
