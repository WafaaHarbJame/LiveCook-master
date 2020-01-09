package com.livecook.livecookapp.Activity

import android.Manifest
import android.app.PendingIntent.getActivity
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.takusemba.rtmppublisher.Publisher
import com.takusemba.rtmppublisher.PublisherListener
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import com.livecook.livecookapp.Api.MyApplication
import com.livecook.livecookapp.MainActivity
import com.livecook.livecookapp.Model.AllFirebaseModel
import com.livecook.livecookapp.Model.Constants
import com.livecook.livecookapp.Model.Constants.*
import com.livecook.livecookapp.R
import com.livecook.livecookapp.interfaces.onCookerLiveClick
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class LiveKotlenActivity : AppCompatActivity(), PublisherListener {

    private lateinit var publisher: Publisher
    private lateinit var glView: GLSurfaceView
    private lateinit var container: RelativeLayout
    private lateinit var publishButton: Button
    private lateinit var cameraButton: ImageView
    private lateinit var backbuuton: ImageView
    private var cook_id_profile_publish = 0


    private lateinit var label: TextView
    private lateinit var toolbar: Toolbar

    private val rtmp_path_cooker = "rtmp://167.86.71.40:1995/livecook/tayeh-10"
    private val handler = Handler()
    private var thread: Thread? = null
    private var isCounting = false
    private lateinit var cook_name_tv: TextView
    private lateinit var database: DatabaseReference


    private lateinit var cookimage_image: CircleImageView

    private var live_title = ""
    private var type_id = 0
    private var counter = 1
    private var name = ""
    private var full_mobile = ""
    private var date = ""
    var firebase_path = ""
    var livestream_path = ""
    var Authorization = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_kotlen)
        glView = findViewById(R.id.surface_view)
        container = findViewById(R.id.container)
        publishButton = findViewById(R.id.toggle_publish)
        cameraButton = findViewById(R.id.toggle_camera)
        label = findViewById(R.id.live_label)
        cook_name_tv = findViewById(R.id.cook_name)
        cookimage_image = findViewById(R.id.cookimageprofile)
        backbuuton = findViewById(R.id.backbuuton)

        database = FirebaseDatabase.getInstance().reference.child("Live")

        backbuuton.setOnClickListener {
            // finish();
            startActivity(Intent(this@LiveKotlenActivity, MainActivity::class.java))

        }


        val intent = intent

        val rtmp_path_cooker = intent.getStringExtra(Constants.rtmp_path_cooker)
        val cook_name = intent.getStringExtra(Constants.cook_name_profile)
        val cookimage = intent.getStringExtra(Constants.cookimage_profile)
        val first_child = intent.getStringExtra(Constants.first_child)
        val second_child = intent.getStringExtra(Constants.second_child)

//        firebase_path = intent.getStringExtra("firebase_path")
        livestream_path = intent.getStringExtra("livestream_path")
        Authorization = intent.getStringExtra("Authorization")


        live_title = intent.getStringExtra("live_title")
        type_id = intent.getIntExtra("type_id", 0)
//        counter = intent.getIntExtra("counter", 0)
        name = intent.getStringExtra("name")
        full_mobile = intent.getStringExtra("full_mobile")
        date = intent.getStringExtra("date")


        cook_id_profile_publish = intent.getIntExtra(Constants.id_liv, -1)
//        cook_id_profile_publish = intent.getIntExtra(Constants.cook_id_profile_publish, -1)
        cook_name_tv.text = cook_name
        Picasso.with(this).load(cookimage)
                // .resize(100,100)
                .error(R.drawable.ellipse)

                .into(cookimage_image)

        // Toast.makeText(this,"the cookname"+cook_name, Toast.LENGTH_SHORT)
        getCookerprofile()


        if (rtmp_path_cooker.isBlank()) {
            Toast.makeText(this, R.string.error_empty_url, Toast.LENGTH_SHORT)
                    .apply { setGravity(Gravity.CENTER, 0, 0) }
                    .run { show() }
        } else {
            Toast.makeText(this,"starting live",Toast.LENGTH_LONG).show()
            publisher = Publisher.Builder(this)
                    .setGlView(glView)
                    .setUrl(rtmp_path_cooker)
                    .setSize(Publisher.Builder.DEFAULT_WIDTH, Publisher.Builder.DEFAULT_HEIGHT)
                    .setAudioBitrate(320000)
                    .setVideoBitrate(Publisher.Builder.DEFAULT_VIDEO_BITRATE)
                    .setCameraMode(Publisher.Builder.DEFAULT_MODE)
                    .setListener(this)
                    .build()

            publishButton.setOnClickListener {
                if (publisher.isPublishing) {
                    publisher.stopPublishing()
                    database.child(cook_id_profile_publish.toString() + "_" + type_id)
                            .child(cook_id_profile_publish.toString() + "_" + type_id + "_" + counter)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    database.child(cook_id_profile_publish.toString() + "_" + type_id)
                                            .child(cook_id_profile_publish.toString() + "_" + type_id + "_" + counter)
                                            .child("status").setValue(false)

                                    Log.d("livefun", counter.toString() + " :counterstatus")
                                }

                            })

                    finish()

                } else {

//                    createcooklive(firebase_path, livestream_path, Authorization)
                    livefun()

/*
                    database.child(first_child).child(second_child).addValueEventListener(object : ValueEventListener

                    {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            p0.ref.child("status").setValue(true)
                        }

                    })*/

                }
            }

            cameraButton.setOnClickListener {
                publisher.switchCamera()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (rtmp_path_cooker.isNotBlank()) {
            updateControls()
        }
    }

    override fun onStarted() {
        /* Toast.makeText(this, R.string.started_publishing, Toast.LENGTH_SHORT)
                 .apply { setGravity(Gravity.CENTER, 0, 0) }
                 .run { show() }*/
        updateControls()
        startCounting()
    }

    override fun onStopped() {
        /*Toast.makeText(this, R.string.stopped_publishing, Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0) }
                .run { show() }*/
        updateControls()
        stopCounting()
    }

    override fun onDisconnected() {
        Toast.makeText(this, R.string.disconnected_publishing, Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0) }
                .run { show() }
        updateControls()
        stopCounting()
    }

    override fun onFailedToConnect() {
        Toast.makeText(this, R.string.failed_publishing, Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0) }
                .run { show() }
        updateControls()
        stopCounting()
    }

    private fun updateControls() {
        if (publisher.isPublishing) {
            publishButton.setText(R.string.stop_publishing)
            publishButton.setBackgroundColor(Color.RED)


        } else {
            publishButton.setText(R.string.start_publishing)
            publishButton.setBackgroundColor(Color.GREEN)
        }


        // publishButton.text = getString(if (publisher.isPublishing) R.string.stop_publishing
        //else R.string.start_publishing)


    }

    private fun startCounting() {
        isCounting = true
        label.text = getString(R.string.publishing_label, 0L.format(), 0L.format())
        label.visibility = View.VISIBLE
        val startedAt = System.currentTimeMillis()
        var updatedAt = System.currentTimeMillis()
        thread = Thread {
            while (isCounting) {
                if (System.currentTimeMillis() - updatedAt > 1000) {
                    updatedAt = System.currentTimeMillis()
                    handler.post {
                        val diff = System.currentTimeMillis() - startedAt
                        val second = diff / 1000 % 60
                        val min = diff / 1000 / 60
                        label.text = getString(R.string.publishing_label, min.format(), second.format())
                    }
                }
            }
        }
        thread?.start()
    }

    private fun stopCounting() {
        isCounting = false
        label.text = ""
        label.visibility = View.GONE
        thread?.interrupt()
    }

    private fun Long.format(): String {
        return String.format("%02d", this)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun getCookerprofile() {

        cook_id_profile_publish = intent.getIntExtra(Constants.id_liv, -1)
//        cook_id_profile_publish = intent.getIntExtra(Constants.cook_id_profile_publish, -1)
        val stringRequest = StringRequest(Request.Method.GET, "https://livecook.co/api/v1/cooker/$cook_id_profile_publish/profile"
                , Response.Listener { response ->
            Log.e("HZM", response)

            try {
                val task_respnse = JSONObject(response)
                val taskarray = task_respnse.getJSONObject("data")
                val name = taskarray.getString("name")
                val avatarURL = taskarray.getString("avatar_url")
                cook_name_tv.text = name


                if (avatarURL.matches("".toRegex()) || !avatarURL.startsWith("http")) {////https://image.flaticon.com/icons/svg/1055/1055672.svg
                    Picasso.with(this@LiveKotlenActivity).load(avatarURL).error(R.drawable.ellipse)
                            // .resize(100,100)
                            .into(cookimage_image)
                } else {
                    Picasso.with(this@LiveKotlenActivity).load(avatarURL)
                            // .resize(100,100)
                            .error(R.drawable.ellipse)

                            .into(cookimage_image)


                }


            } catch (e1: JSONException) {
                e1.printStackTrace()

            }

        }, Response.ErrorListener { })

        MyApplication.getInstance().addToRequestQueue(stringRequest)

    }


    fun livefun() {

//        val mFirebaseDatabase = FirebaseDatabase.getInstance().reference.child("Live")

        val prefs: SharedPreferences = getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE)
        var editor = prefs.edit()
        counter = prefs.getInt("counter", 0);

        // live_title//type_id // counter //name //full_mobile
        val allFirebaseModel = AllFirebaseModel("rtmp://167.86.71.40:1995/livecook/" + cook_id_profile_publish + "_" + type_id + "_" + counter,
                "http://167.86.71.40:8384/" + "video/" + cook_id_profile_publish + "_" + type_id + "_" + counter + ".flv.mp4",
                "http://167.86.71.40:89/livecook/" + cook_id_profile_publish + "_" + type_id + "_" + counter + "/index.m3u8", counter, full_mobile, cook_id_profile_publish, name,
                true, live_title, type_id, counter, date,
                "http://167.86.71.40:8384/video/" + cook_id_profile_publish + "_" + type_id + "_" + counter + ".jpg")

        Log.d("livefun", allFirebaseModel.livePath + "**" + allFirebaseModel.recordPath + "***")
        Log.d("livefun", date + "**" + cook_id_profile_publish + "**" + live_title + "***")


        database.child(cook_id_profile_publish.toString() + "_" + type_id)
                .child(cook_id_profile_publish.toString() + "_" + type_id + "_" + counter)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        counter += 1
                        Log.d("livefun", counter.toString() + " :counter")
                        editor.putInt("counter", counter)
                        editor.apply()

                        if (dataSnapshot.exists()) {

                            database.child(cook_id_profile_publish.toString() + "_" + type_id)
                                    .child(cook_id_profile_publish.toString() + "_" + type_id + "_" + counter)
                                    .setValue(allFirebaseModel).addOnCompleteListener {
                                        firebase_path = dataSnapshot.getRef().toString()
                                        createcooklive(firebase_path, livestream_path, Authorization)
                                    }

                            Log.d("livefun", "exists")
                        } else {
                            Log.d("livefun", "not exists")

                            database.child(cook_id_profile_publish.toString() + "_" + type_id)
                                    .child(cook_id_profile_publish.toString() + "_" + type_id + "_" + counter)
                                    .setValue(allFirebaseModel).addOnCompleteListener {
                                        firebase_path = dataSnapshot.getRef().toString()
                                        createcooklive(firebase_path, livestream_path, Authorization)
                                    }
                        }


                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })


    }


    fun createcooklive(firebase_path: String, livestream_path: String, access_token: String) {
        val stringRequest = object : StringRequest(Request.Method.POST, Constants.live_cook_path, Response.Listener { response ->
            try {
                val register_response = JSONObject(response)
                val status = register_response.getBoolean("status")
                val message = register_response.getString("message")

                if (status) {
                    Toast.makeText(this@LiveKotlenActivity, "تم انشاء البث بنجاح", Toast.LENGTH_SHORT).show()
                    publisher.startPublishing()

                } else {
                    Toast.makeText(this@LiveKotlenActivity, "لم يتم البث$message", Toast.LENGTH_SHORT).show()

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer  $access_token"
                headers["firebase_path"] = firebase_path
                headers["livestream_path"] = livestream_path

                return headers

            }


            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer  $access_token"
                headers["firebase_path"] = firebase_path
                headers["livestream_path"] = livestream_path

                return headers
            }
        }

        MyApplication.getInstance().addToRequestQueue(stringRequest)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        publisher.stopPublishing()
        database.child(first_child).child(second_child).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.ref.child("status").setValue(false)
            }

        })

        finish()
    }

}