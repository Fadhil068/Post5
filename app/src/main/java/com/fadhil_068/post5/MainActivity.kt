package com.fadhil_068.post5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.view.LayoutInflater
import android.net.Uri
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var rvCerita: RecyclerView
    private lateinit var rvPost: RecyclerView
    private lateinit var fabAddPost: FloatingActionButton

    private lateinit var ceritaAdapter: CeritaAd
    private lateinit var postAdapter: PostAd

    private val ceritaList = mutableListOf<Cerita>()
    private val postList = mutableListOf<Post>()
    private var tempImageUri: Uri? = null
    private lateinit var dialogImageView: ImageView

    private var newPostCounter = 0

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            tempImageUri = uri
            dialogImageView.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvCerita = findViewById(R.id.rv_stories)
        rvPost = findViewById(R.id.rv_posts)
        fabAddPost = findViewById(R.id.fab_add_p)

        loadDummyCerita()
        loadDummyPosts()

        ceritaAdapter = CeritaAd(ceritaList)
        postAdapter = PostAd(postList)

        rvCerita.adapter = ceritaAdapter
        rvPost.adapter = postAdapter

        setupFabListener()
    }

    private fun loadDummyCerita() {
        ceritaList.add(Cerita(1, "Ichigo", R.drawable.ichigo))
        ceritaList.add(Cerita(2, "Marin", R.drawable.marin))
        ceritaList.add(Cerita(3, "Zenitsu", R.drawable.zenitsu))
        ceritaList.add(Cerita(4, "Tanjiro", R.drawable.tanjiro))
        ceritaList.add(Cerita(5, "Sasuke", R.drawable.sasuke))
        ceritaList.add(Cerita(6, "Naruto", R.drawable.naruto))
        ceritaList.add(Cerita(7, "Sakura", R.drawable.sakura))
    }

    private fun loadDummyPosts() {
        postList.add(Post(1, "Marin", R.drawable.marin, R.drawable.bromo, "Gunung Bromo Bos Senggol Dong", 10))
        postList.add(Post(2, "Zenitsu", R.drawable.zenitsu, R.drawable.alpen, "Aku Lagi di sekitar gunung alpen", 25))
        postList.add(Post(3, "Sasuke", R.drawable.sasuke, R.drawable.zurich, "Kapan lagi ye kan main main di zurich heheh", 150))
    }

    private fun setupFabListener() {
        fabAddPost.setOnClickListener {
            showPostDialog(isEdit = false, position = -1)
        }
    }

    fun editPost(position: Int) {
        showPostDialog(isEdit = true, position = position)
    }

    private fun showPostDialog(isEdit: Boolean, position: Int) {
        val dialogView = LayoutInflater.from(this).inflate(
            if (isEdit) R.layout.edit_post else R.layout.tambah_post,
            null
        )

        val etUsername: EditText = dialogView.findViewById(R.id.et_username)
        val etCaption: EditText = dialogView.findViewById(R.id.et_caption)
        val btnSimpan: Button = dialogView.findViewById(R.id.btn_simpan)
        val btnTambahGambar: LinearLayout = dialogView.findViewById(R.id.btn_tambah_gambar)

        dialogImageView = dialogView.findViewById(R.id.img_post_preview)
        tempImageUri = null

        if (isEdit && position >= 0) {
            val post = postList[position]
            etUsername.setText(post.username)
            etCaption.setText(post.caption)

            when (post.postImageUrl) {
                is Int -> dialogImageView.setImageResource(post.postImageUrl as Int)
                is Uri -> dialogImageView.setImageURI(post.postImageUrl as Uri)
            }
        } else {
            dialogImageView.setImageResource(R.drawable.bromo)
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnTambahGambar.setOnClickListener {
            pickImageLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        btnSimpan.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val caption = etCaption.text.toString().trim()

            if (username.isEmpty() || caption.isEmpty()) {
                if (username.isEmpty()) etUsername.error = "Username wajib diisi"
                if (caption.isEmpty()) etCaption.error = "Caption wajib diisi"
                return@setOnClickListener
            }

            val imageUrlToSave: Any = tempImageUri ?: R.drawable.bromo

            if (isEdit && position >= 0) {
                val post = postList[position]
                post.username = username
                post.caption = caption
                post.postImageUrl = imageUrlToSave

                postAdapter.notifyItemChanged(position)
                Toast.makeText(this, "Postingan berhasil diedit", Toast.LENGTH_SHORT).show()
            } else {
                val newPost = Post(
                    id = postList.size + 1,
                    username = username,
                    userImageUrl = R.drawable.ic_camera,
                    postImageUrl = imageUrlToSave,
                    caption = caption,
                    likes = 0
                )

                postList.add(0, newPost)
                postAdapter.notifyItemInserted(0)
                rvPost.scrollToPosition(0)
                Toast.makeText(this, "Postingan berhasil di-upload", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        dialog.show()
    }
}
