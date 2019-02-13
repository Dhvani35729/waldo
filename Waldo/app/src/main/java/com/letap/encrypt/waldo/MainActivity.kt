package com.letap.encrypt.waldo;

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions;
import com.navisens.motiondnaapi.MotionDna
import com.navisens.motiondnaapi.MotionDnaApplication
import com.navisens.motiondnaapi.MotionDnaInterface
import com.navisens.motiondnaapi.MotionDnaPlugin.startMotionDna
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ui.BubbleIconFactory
import com.google.maps.android.ui.IconGenerator
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.PatternItem
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.solacesystems.jcsmp.JCSMPFactory
import com.solacesystems.jcsmp.JCSMPProperties
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import java.util.Arrays.asList


// Inheriting from GoogleMaps Callbacks and our MotionDnaInterface
class MainActivity : AppCompatActivity(), OnMapReadyCallback, MotionDnaInterface, GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?): Boolean {
        System.out.println(p0!!.title)
        System.out.println(p0!!.snippet)
        System.out.println(p0!!.id)
        last_clicked = p0!!

        mMap.clear()
        pos_marker = mMap.addMarker(
            MarkerOptions()
                .title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)).position(
                    pos_marker.position
                )
        )
        var k = mMap.addPolyline((PolylineOptions().clickable(true).add(pos_marker.position, p0!!.position)));
        val pattern = Arrays.asList(
            Dot(), Gap(20f), Dash(30f), Gap(20f)
        )
        k.pattern = pattern;

        val iconFactory = IconGenerator(this)
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);

        if(points_1.size > 0){

            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Mona Lisa")))
                    .snippet("Da Vinci").position(points_1.get(0)).anchor(
                        iconFactory.getAnchorU(),
                        iconFactory.getAnchorV()
                    )
            );

            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("The Starry Night")))
                    .snippet("Van Gogh").position(points_1.get(1)).anchor(
                        iconFactory.getAnchorU(),
                        iconFactory.getAnchorV()
                    )
            );

        }



//        var circle_1 = mMap.addCircle(
//            CircleOptions().center(points_1.get(1)).strokeColor(Color.RED)
//                .fillColor(Color.BLUE)
//        );
//        circle_1.radius = 3.0.toDouble();
//
//        var circle_2 = mMap.addCircle(
//            CircleOptions().center(points_1.get(0)).strokeColor(Color.RED)
//                .fillColor(Color.BLUE)
//        );
//        circle_2.radius = 3.0.toDouble();

        return true
    }

    // MotionDna Permissions value
    private val REQUEST_MDNA_PERMISSIONS = 1
    // Current location Google Marker
    private lateinit var pos_marker: Marker
    private lateinit var last_clicked: Marker

    // MotionDnaApplication instance
    private lateinit var motionDnaApp: MotionDnaApplication
    private lateinit var line_1: Polyline
    private lateinit var points_1: ArrayList<LatLng>

    private lateinit var daVinciDialog: Dialog
    private lateinit var vanGaughDialog: Dialog

    private lateinit var database : FirebaseDatabase;

    // Must do, MotionDna SDK uses ApplicationContext to request permission.
    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun receiveNetworkData(p0: MotionDna?) {
        // Receive location data from network when user is sharing.
    }

    override fun receiveNetworkData(p0: MotionDna.NetworkCode?, p1: MutableMap<String, out Any>?) {
        // Receive custom commands over network to interactive multi user applications.
    }


    // SDK Callback for current location estimate
    override fun receiveMotionDna(p0: MotionDna?) {
        // Current location estimate
        var latlon = LatLng(43.471737, -80.543640)
        var currLoc = LatLng(
            43.471737 + (p0!!.location.localLocation.x / 100000),
            -80.543640 + (p0!!.location.localLocation.y / 100000)
        )
        var paint_1 = LatLng(43.471797, -80.543840)
        var paint_2 = LatLng(43.471857, -80.543650)
        // var paintArr: Array<LatLng> = arrayOf(paint_1, paint_2);
        if (::pos_marker.isInitialized == false) {
            // Initialize marker
            pos_marker = mMap.addMarker(
                MarkerOptions()
                    .title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)).position(
                        currLoc
                    )
            )

            if(mMap.focusedBuilding != null && mMap.focusedBuilding.activeLevelIndex == 5){
            val iconFactory = IconGenerator(this)
            iconFactory.setStyle(IconGenerator.STYLE_GREEN);


            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Mona Lisa")))
                    .snippet("Da Vinci").position(paint_1).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
            );

            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("The Starry Night")))
                    .snippet("Van Gogh").position(paint_2).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
            );

            //  paint_marker_1 = mMap.addMarker(MarkerOptions().title("The Starry Night").snippet("Van Gogh").position(paint_2));
            //  mMap.addGroundOverlay(GroundOverlayOptions().position(paint_2, 1.0.toFloat()));

            points_1.add(paint_1)
            points_1.add(paint_2)

            //  mMap.addMarker(MarkerOptions().title("Move Me").position(latlon))
            }
            // Set marker to be flat.
            pos_marker.isFlat = true
            pos_marker.showInfoWindow()
            // Move camera to initial position
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 20.0.toFloat()));

            // pos_marker.rotation = 0.toFloat()

            // mMap.addPolyline((PolylineOptions().clickable(true).add(currLoc, paint_2)));
        }
        //Update location and heading

        var circle_1 = mMap.addCircle(
            CircleOptions().center(paint_2).strokeColor(Color.RED)
                .fillColor(Color.BLUE)
        );
        circle_1.radius = 3.0.toDouble();

        var circle_2 = mMap.addCircle(
            CircleOptions().center(paint_1).strokeColor(Color.RED)
                .fillColor(Color.BLUE)
        );
        circle_2.radius = 3.0.toDouble();



       if (mMap.focusedBuilding != null) {
           System.out.println(mMap.focusedBuilding.activeLevelIndex)
       }

        // System.out.println(SphericalUtil.computeDistanceBetween(pos_marker.position, circle.center).toString() + "MAYANK!")
        if (SphericalUtil.computeDistanceBetween(pos_marker.position, circle_1.center) <= circle_1.radius) {
            // show pop up
            vanGaughDialog.show();

        } else if (SphericalUtil.computeDistanceBetween(pos_marker.position, circle_2.center) <= circle_2.radius) {
            // show pop up 2
            daVinciDialog.show();
        } else {
            daVinciDialog.hide();
            vanGaughDialog.hide();
        }
        mMap.clear()

//        circle_1 = mMap.addCircle(
//            CircleOptions().center(paint_2).strokeColor(Color.RED)
//                .fillColor(Color.BLUE)
//        );
//        circle_1.radius = 3.0.toDouble();
//
//        circle_2 = mMap.addCircle(
//            CircleOptions().center(paint_1).strokeColor(Color.RED)
//                .fillColor(Color.BLUE)
//        );
//        circle_2.radius = 3.0.toDouble();

        pos_marker = mMap.addMarker(
            MarkerOptions()
                .title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.red_dot)).position(
                    currLoc
                )
        )

         database.getReference(p0!!.id.toString()).child("location").child("longitude").setValue(currLoc.longitude) .addOnSuccessListener {
             // Write was successful!
             // ...
         }
             .addOnFailureListener {
                 // Write failed
                 // ...
             }

        database.getReference(p0!!.id.toString()).child("location").child("latitude").setValue(currLoc.latitude) .addOnSuccessListener {
            // Write was successful!
            // ...
        }
            .addOnFailureListener {
                // Write failed
                // ...
            }

        pos_marker.position = currLoc
        pos_marker.rotation = -1.0.toFloat() * p0!!.location.localHeading.toFloat()

        if(mMap.focusedBuilding != null &&  mMap.focusedBuilding.activeLevelIndex == 5) {
            val iconFactory = IconGenerator(this)
            iconFactory.setStyle(IconGenerator.STYLE_GREEN);


            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Mona Lisa")))
                    .snippet("Da Vinci").position(paint_1).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
            );

            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

            mMap.addMarker(
                MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("The Starry Night")))
                    .snippet("Van Gogh").position(paint_2).anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
            );
        }

            if (::last_clicked.isInitialized == true) {
                val pattern = Arrays.asList(
                    Dot(), Gap(20f), Dash(30f), Gap(20f)
                )
                var k =
                    mMap.addPolyline(
                        (PolylineOptions().clickable(true).add(
                            pos_marker.position,
                            last_clicked.position
                        ))
                    );
                k.pattern = pattern;
            }



    }

//    private fun redrawLine() {
//
//        mMap.clear()  //clears all Markers and Polylines
//
//        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
//        for (i in 0 until points_1.size()) {
//            val point = points_1.get(i)
//            options.add(point)
//        }
//      //  addMarker() //add Marker in current position
//     //   line = googleMap.addPolyline(options) //add Polyline
//       // mMap.addPolyline((PolylineOptions().clickable(true).add(currLoc, paint_2)));
//    }

    override fun reportError(p0: MotionDna.ErrorCode?, p1: String?) {
        // Error reporting
    }

    // Must do, MotionDna SDK uses PackageManager to request permission.
    override fun getPkgManager(): PackageManager {
        return packageManager
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Write a message to the database
         database = FirebaseDatabase.getInstance()


        points_1 = ArrayList<LatLng>()

        daVinciDialog = Dialog(this)
        daVinciDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        var layout = layoutInflater.inflate(R.layout.popup, null)
        daVinciDialog.setContentView(layout)
        daVinciDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        var okButton = layout.findViewById(R.id.okay) as Button
        okButton.setOnClickListener {
            daVinciDialog.hide()
        }

        vanGaughDialog = Dialog(this)
        vanGaughDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        var layout_2 = layoutInflater.inflate(R.layout.popup2, null)
        vanGaughDialog.setContentView(layout_2)
        vanGaughDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        var okButton2 = layout_2.findViewById(R.id.okay) as Button
        okButton2.setOnClickListener {
            vanGaughDialog.hide()
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Callback for permissions to trigger.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (MotionDnaApplication.checkMotionDnaPermissions(this)) {
            // Instantiate MotionDnaApplication instance
            motionDnaApp = MotionDnaApplication(this)
            // Start SDK with the device key you retrieved from www.navisens.com
            motionDnaApp.runMotionDna("PUT YOUR NAVISENS KEY HERE")
            // Ensure GPS is turned on for Navisens' fusion systems to work
            motionDnaApp.setExternalPositioningState(MotionDna.ExternalPositioningState.HIGH_ACCURACY)
            // Start sensor fusion systems
//            motionDnaApp.setLocationNavisens()
            motionDnaApp.resetLocalHeading()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this);

        ActivityCompat.requestPermissions(
            this, MotionDnaApplication.needsRequestingPermissions()
            , REQUEST_MDNA_PERMISSIONS
        );
    }

}
