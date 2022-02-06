package com.example.tinkoff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import model.PostModel
import org.json.JSONException
import com.bumptech.glide.Glide
import pl.droidsonroids.gif.GifImageView


class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://developerslife.ru/"

    lateinit var mRequestQueue: RequestQueue
    lateinit var description: TextView
    lateinit var back: Button
    lateinit var next: Button
    lateinit var img: GifImageView

    val postList: MutableList<PostModel> = mutableListOf()
    var postIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRequestQueue = Volley.newRequestQueue(this)
        description = findViewById(R.id.description)
        back = findViewById(R.id.back)
        next = findViewById(R.id.next)
        img = findViewById(R.id.img_container)

        getPost(BASE_URL + "random?json=true")
    }

    private fun getPost(url: String) {
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val description = response.getString("description")
                    val gifURL = response.getString("gifURL")
                    postList.add(PostModel(gifURL, description))
                    postIndex++
                    loadPost()
                }catch (e: JSONException){ e.printStackTrace()}
            },
            { error ->
                error.printStackTrace()
            })
        mRequestQueue.add(request)
    }

    private fun loadPost() {
        val post = postList[postIndex]

        Glide.with(this)
            .asGif()
            .load(post.gifURL)
            .error(R.drawable.ic_launcher_foreground)
            .into(img)

        description.text = post.description
    }

    fun nextPost(view: View) {
        if(postIndex == postList.size-1)
            getPost(BASE_URL + "random?json=true")
        else {
            postIndex++
            loadPost()
        }
    }

    fun previousPost(view: View) {
        if(postIndex > 0) {
            postIndex--
            loadPost()
        }
    }

}