package com.example.project13_allfirebase

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project13_allfirebase.databinding.ActivityMainBinding
import com.example.project13_allfirebase.databinding.ActivitySignEmailBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var dbStorage: StorageReference
    private lateinit var adapter: FirebaseAdapter
    private lateinit var mlist: MutableList<FireData>
    private var Image: Uri ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("FireBaseRaltime")
        dbStorage = FirebaseStorage.getInstance().reference.child("FireBaseImg")

        binding.imgHinhAnh.setOnClickListener {
            laurch.launch("image/*")
        }

        binding.btnInsert.setOnClickListener {
            InsertData()
        }

        hienThiAdpater()
        HienThiChiTietData()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun HienThiChiTietData() {
        val imgRef = dbStorage.listAll()
        imgRef.addOnSuccessListener { reslut ->
            mlist.clear()
            var items = reslut.items
            var dowloadTask =items.map { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    dbRef.addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            mlist.clear()
                            for(snap in snapshot.children){
                                val item = snap.getValue(FireData::class.java)
                                mlist.add(item!!)
                            }
                            val adapter = FirebaseAdapter(mlist)
                            binding.rvFull.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to get URL for ${item.name}", Toast.LENGTH_SHORT).show()
                }
            }

            Tasks.whenAllComplete(dowloadTask).addOnCompleteListener {
                adapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to list items", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hienThiAdpater() {
        mlist = mutableListOf()
        binding.rvFull.layoutManager = LinearLayoutManager(this)
        adapter = FirebaseAdapter(mlist)
        binding.rvFull.adapter =adapter

    }

    private fun InsertData() {
        Image?.let {imgUri->
            val fileName = System.currentTimeMillis().toString()
            val fileRef = dbStorage.child(fileName)

            fileRef.putFile(imgUri).addOnCompleteListener { uploadTask ->
                if(uploadTask.isSuccessful){
                    fileRef.downloadUrl.addOnSuccessListener { downLoadUri ->
                        val imageUrl =downLoadUri.toString()
                        UploadDataToDatabase(imageUrl)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun UploadDataToDatabase(imageUrl: String) {
        val name = binding.edtInsertName.text.toString().trim()
        val stt = binding.edtInsrtStt.text.toString().trim()
        val id = dbRef.push().key!! // Tạo ID mới cho bản ghi

        val fireData = FireData(id, name, stt, imageUrl)
        dbRef.child(id).setValue(fireData).addOnSuccessListener {
            Toast.makeText(this, "Insert Successful", Toast.LENGTH_SHORT).show()
            binding.edtInsrtStt.setText("")
            binding.edtInsertName.setText("")
            binding.imgHinhAnh.setImageResource(R.drawable.img_1)
        }.addOnFailureListener {
            Toast.makeText(this, "Insert Failed", Toast.LENGTH_SHORT).show()
        }
    }


    private val laurch= registerForActivityResult(ActivityResultContracts.GetContent()){Uri ->
        Image = Uri
        binding.imgHinhAnh.setImageURI(Uri)
    }
}