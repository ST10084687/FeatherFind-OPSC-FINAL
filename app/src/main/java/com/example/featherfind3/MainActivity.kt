package com.example.featherfind3
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.GpsStatus
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.telephony.SmsManager
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.featherfind3.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity(), IMyLocationProvider, MapListener, GpsStatus.Listener {

    //variables
    private val LOCATION_REQUEST_CODE = 100
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay
    private lateinit var controller: IMapController


    //Declare global variables for latitude and longitude
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val noteMap = HashMap<GeoPoint, String>()

    val hotspotLocations =  listOf(
        Pair("Hluhluwe iMfolozi Park", GeoPoint(-28.219831, 31.951865)),
        Pair("Umgeni River Bird Park", GeoPoint(-29.808167, 31.017467)),
        Pair("Durban Japanese Gardens", GeoPoint(-29.7999, 31.03758)),
        Pair("Addo Elephant National Park", GeoPoint(-33.483883, 25.751253)),
        Pair("Kruger National Park", GeoPoint(-24.917765, 31.491016)),
        Pair("Table Mountain National Park", GeoPoint(-34.353305, 18.470304)),
        Pair("Blyde River Canyon Nature Reserve", GeoPoint(-24.574592, 30.795642)),
        Pair("iSimangaliso Wetland Park", GeoPoint(-27.831878, 32.500623)),
        Pair("De Hoop Nature Reserve", GeoPoint(-34.439913, 20.476661)),
        Pair("Golden Gate Highlands National Park", GeoPoint(-28.612878, 28.460703)),
        Pair("Robben Island", GeoPoint(-33.808201, 18.368387))

    )
    private val hotspotMarkers =
        mutableListOf<Marker>() //Initialize an empty list for hotspot markers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init binding
        //val binding: ActivityMainBinding by lazy{
        //  ActivityMainBinding.inflate(layoutInflater)
        //}
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



// Now, you can set the listener for the bottomNavigationView using data binding
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true

                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.navigation_dashboard -> {
                    startActivity(Intent(applicationContext, SaveBirdObservation::class.java))
                    finish()
                    true
                }

                R.id.navigation_notifications -> {
                    startActivity(Intent(applicationContext, AboutUs::class.java))
                    finish()
                    true
                }
                R.id.navigation_education -> {
                    startActivity(Intent(applicationContext, FeatherEd::class.java))
                    finish()
                    true
                }
                R.id.navigation_avian -> {
                    startActivity(Intent(applicationContext, BirdActivity::class.java))
                    finish()
                    true
                }



                else -> false
            }
        }


        // -------------------------------------------------------------------------------------------------
        //code for sms button and phone number
     //   val sendSmsButton = findViewById<Button>(R.id.sendLocationButton)
      //  val phoneNumberEditText = findViewById<EditText>(R.id.phoneNumberEditText)
// -------------------------------------------------------------------------------------------------

        // Inside your `onCreate` method, add the following code to find the "Settings" button and set a click listener:

        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)

        settingsButton.setOnClickListener {
            // Open the SettingsActivity when the "Settings" button is clicked
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        //Inside your OnCreate() Method


        //managePermissions()
        //setupMap()
        getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.mapCenter
        mapView.setMultiTouchControls(true)
        mapView.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        controller = mapView.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true

        //set the initial zoom level
        controller.setZoom(6.0)

        mapView.overlays.add(mMyLocationOverlay)
        setupMap()
        mapView.addMapListener(this)

        //check and request location permissions
        managePermissions()

        //create a custom overlay for the animated marker
        val animatedMarkerOverlay = object : Overlay(this) {
            override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                //calculate the latitude and longitude from the geopoint
                val geoPoint = mMyLocationOverlay.myLocation
                val latitude = geoPoint.latitude
                val longitude = geoPoint.longitude

                //create a custom dialog or info window to display the latitude and longitude
                val dialog = Dialog(this@MainActivity)
                dialog.setContentView(R.layout.custom)

                val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)
                val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)

                latitudeTextView.text = "Latitude: $latitude"
                longitudeTextView.text = "Longitude: $longitude"

                dialog.show()

                return true
            }
        }
        //add the animatedMarkerOverlay to the map
        mapView.overlays.add(animatedMarkerOverlay)

        //Get a reference to the view hotspots button
        val viewHotspotsButton = findViewById<Button>(R.id.viewHotspotsButton)

        //add an OnClickListener to the button
        viewHotspotsButton.setOnClickListener {
            // Inside the click listener for the "View Hotspots" button
            val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val selectedDistanceUnit = sharedPreferences.getString("distanceUnit", "Miles") ?: "Miles"
            val maximumDistance = sharedPreferences.getFloat("maximumDistance", 10f)

// Clear existing hotspot markers
            mapView.overlays.removeAll(hotspotMarkers)

// Calculate new hotspots based on the selected distance unit and maximum distance
            val newHotspotLocations = calculateNewHotspots(selectedDistanceUnit, maximumDistance)

// Add the new hotspot markers
            addHotspotMarkers(newHotspotLocations)

        }
        val showRoutesButton = findViewById<Button>(R.id.showRoutesButton)
        showRoutesButton.setOnClickListener {
            calculateAndDisplayRoutes()
        }

        //sms button code --------------------------------------------------------------------------------------------------------
      /*  sendSmsButton.setOnClickListener {
            if (latitude != 0.0 && longitude != 0.0) {

                val phoneNumber = phoneNumberEditText.text.toString().trim()

                if (phoneNumber.isNotEmpty()) {
                    val message = "Latitude: $latitude, Longitude: $longitude"

                    sendSms(phoneNumber, message)
                } else {
                    Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                Toast.makeText(this, "Location information is not available", Toast.LENGTH_SHORT)
                    .show()

            }


        }

       */
        //-----------------------------------------------------------------------------------------------------------------------------
    }

    private fun sendSms(destinationPhoneNumber: String, message: String) {
        try{

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(destinationPhoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }

    private fun calculateAndDisplayRoutes(){
        val startPoint = mMyLocationOverlay.myLocation

        if (startPoint == null){
            Toast.makeText(this, "Location loading errors.", Toast.LENGTH_SHORT).show()
            return
        }

        for ((startLocationName, endPoint) in hotspotLocations){
            GlobalScope.launch(Dispatchers.IO){
                val roadManager = OSRMRoadManager(this@MainActivity, "OBP_Tuto/1.0")
                var road: Road? = null
                var retryCount = 0

                while (road == null && retryCount < 3){
                    road = try {
                        roadManager.getRoad(arrayListOf(startPoint, endPoint))
                    } catch (e: Exception){
                        null
                    }
                    retryCount++
                }
                withContext(Dispatchers.Main){
                    if (road != null && road.mStatus == Road.STATUS_OK){
                        val roadOverlay = RoadManager.buildRoadOverlay(road)
                        mapView.overlays.add(roadOverlay)

                        //Display the route details in an AlertDialog
                        val routeDetails = "Start Location: Your current location\nEnd Location: $startLocationName\nDistance: ${road.mLength}"
                        showRouteDetailsDialog(routeDetails)

                        mapView.invalidate()
                    }else {
                        Toast.makeText(this@MainActivity, "Error when loading road - status=${road?.mStatus ?: "unknown"}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun calculateNewHotspots(selectedDistanceUnit: String, maximumDistance: Float): List<Pair<String, GeoPoint>> {
        // Define your list of hotspot locations
        val allHotspotLocations =
            listOf(
                Pair("Hluhluwe iMfolozi Park", GeoPoint(-28.219831, 31.951865)),
                Pair("Umgeni River Bird Park", GeoPoint(-29.808167, 31.017467)),
                Pair("Durban Japanese Gardens", GeoPoint(-29.7999, 31.03758)),
                Pair("Addo Elephant National Park", GeoPoint(-33.483883, 25.751253)),
                Pair("Kruger National Park", GeoPoint(-24.917765, 31.491016)),
                Pair("Table Mountain National Park", GeoPoint(-34.353305, 18.470304)),
                Pair("Blyde River Canyon Nature Reserve", GeoPoint(-24.574592, 30.795642)),
                Pair("iSimangaliso Wetland Park", GeoPoint(-27.831878, 32.500623)),
                Pair("De Hoop Nature Reserve", GeoPoint(-34.439913, 20.476661)),
                Pair("Golden Gate Highlands National Park", GeoPoint(-28.612878, 28.460703)),
                Pair("Robben Island", GeoPoint(-33.808201, 18.368387))

        )

        // Convert the maximum distance to meters if the selected unit is kilometers
        val maxDistanceMeters = when (selectedDistanceUnit) {
            "Kilometers" -> maximumDistance * 1000
            else -> maximumDistance
        }

        val currentLocation = mMyLocationOverlay.myLocation ?: GeoPoint(0.0, 0.0)

        val newHotspotLocations = allHotspotLocations.filter { (_, location) ->
            location.distanceToAsDouble(currentLocation) <= maxDistanceMeters
        }

        return newHotspotLocations
    }





    private fun showRouteDetailsDialog(routeDetails: String){
        runOnUiThread {
            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Route Details")
            alertDialog.setMessage(routeDetails)
            alertDialog.setPositiveButton("OK") {dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }
    }

    private fun addHotspotMarkers(newHotspotLocations: List<Pair<String, GeoPoint>>) {
        val hotspotMarkers = mutableListOf<Marker>() // Initialize an empty list for hotspot markers

        for ((name, location) in newHotspotLocations) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location, null)

            marker.setOnMarkerClickListener { marker, mapView ->
                val dialog = Dialog(this@MainActivity)
                dialog.setContentView(R.layout.custom_marker_dialog)

                val noteEditText = dialog.findViewById<EditText>(R.id.noteEditText)
                val saveNoteButton = dialog.findViewById<Button>(R.id.saveNoteButton)
                val displayNoteTextView = dialog.findViewById<TextView>(R.id.displayNoteTextView)

                val locationNameTextView = dialog.findViewById<TextView>(R.id.locationNameTextView)
                locationNameTextView.text = name

                // Load any existing note for this location
                val savedNote = loadNote(marker.position)
                displayNoteTextView.text = savedNote

                // Handle saving the note
                saveNoteButton.setOnClickListener {
                    val note = noteEditText.text.toString()
                    saveNote(marker.position, note)
                    displayNoteTextView.text = note
                    dialog.dismiss()
                }

                dialog.show()
                true // Return true to indicate that the event is consumed
            }

            hotspotMarkers.add(marker)
        }

        mapView.overlays.addAll(hotspotMarkers)
        mapView.invalidate()
    }


    private fun saveNote(location: GeoPoint, note: String) {
        noteMap[location] = note

    }

    private fun loadNote(location: GeoPoint): String{
        return noteMap[location] ?: ""
    }



    private fun setupMap() {
        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        //mapView = binding.mapView
        mapController = mapView.controller
        mapView.setMultiTouchControls(true)

        //init the start point
        val startPoint = GeoPoint(-29.8587, 31.0218)
        mapController.setCenter(startPoint)
        mapController.setZoom(12.0)

        //create marker
        //val marker = Marker(mapView)
        //marker.position = startPoint
        //marker.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_location,null)
        //add marker to mapview
        //mapView.overlays.add(marker)
        val icLocationMarker = Marker(mapView)
        icLocationMarker.position = startPoint
        icLocationMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location, null)

        //add a click listener to the ic_location marker
        icLocationMarker.setOnMarkerClickListener { marker, mapView ->
            val latitude = marker.position.latitude
            val longitude = marker.position.longitude

            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.custom)

            val latitudeTextView = dialog.findViewById<TextView>(R.id.latitudeTextView)
            val longitudeTextView = dialog.findViewById<TextView>(R.id.longitudeTextView)

            latitudeTextView.text = "Latitude: $latitude"
            longitudeTextView.text = "Longitude: $longitude"

            dialog.show()

            true //Return true to indicate that the event is consumed
        }

        // Add the ic_location marker to the mapView
        mapView.overlays.add(icLocationMarker)
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        //handle map scroll event here
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //handle map zoom event here
        return false
    }

    override fun onGpsStatusChanged(p0: Int) {
        //handle GPS status changes here
    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        //start location provider here
        return true
    }

    override fun stopLocationProvider() {
        //stop location provider here
    }

    override fun getLastKnownLocation(): Location {
        // get last known location here
        return Location("last_known_location")
    }

    override fun destroy() {
        //destroy resources here
    }

    //handle permissions
    private fun isLocationPermissionGranted(): Boolean {
        val fineLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLocation && coarseLocation
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //handle permission granted
                        //you can re-initialize the map here if needed
                        //setupMap()
                    } else {
                        Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun managePermissions() {
        val requestPermissions = mutableListOf<String>()
        if (!isLocationPermissionGranted()) {
            //if theses weren't granted
            requestPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        //sms permission
        if (!isSmsPerrmissionGranted()) {
            requestPermissions.add(android.Manifest.permission.SEND_SMS)
        }

        if (requestPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                requestPermissions.toTypedArray(),
                LOCATION_REQUEST_CODE
            )
        }
    }

    private fun isSmsPerrmissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.SEND_SMS
        )== PackageManager.PERMISSION_GRANTED
    }


    fun saveObservationScreen(view: View) {
        val intent = Intent(this, SaveBirdObservation::class.java)
        startActivity(intent)
    }
    fun birdsChat(view: View)

    {
        val intent = Intent(this, BirdChat::class.java)

        startActivity(intent)

    }
}