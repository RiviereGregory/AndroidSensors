package gri.riverjach.theendormap.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import timber.log.Timber

class EndorInfoWindowAdapter @SuppressLint("ClickableViewAccessibility") constructor(
    layoutResId: Int,
    mapView: MapView?
) : MarkerInfoWindow(layoutResId, mapView) {


    lateinit var buttonUrl: Button
    val UNDEFINED_RES_ID = 0

    var mTitleId = UNDEFINED_RES_ID
    var mDescriptionId = UNDEFINED_RES_ID
    var mSubDescriptionId = UNDEFINED_RES_ID
    var mImageId = UNDEFINED_RES_ID
    var snippet = ""

    init {
        if (mTitleId == UNDEFINED_RES_ID && mapView != null) {
            setResIds(
                mapView.context
            )
        }
        mView.setOnTouchListener { _, e ->
            if (e.action == MotionEvent.ACTION_UP) {
                close()
            }
            true
        }
    }


    private fun setResIds(context: Context) {
        val packageName = context.packageName //get application package name
        mTitleId = context.resources.getIdentifier("id/titleTextView", null, packageName)
        mSubDescriptionId =
            context.resources.getIdentifier("id/descriptionTextView", null, packageName)

        mDescriptionId =
            context.resources.getIdentifier("id/urlTextView", null, packageName)
        buttonUrl = mView.findViewById<View>(mDescriptionId /*R.id.description*/) as Button
        mImageId = context.resources.getIdentifier("id/imageView", null, packageName)
        if (mTitleId == UNDEFINED_RES_ID || mDescriptionId == UNDEFINED_RES_ID || mSubDescriptionId == UNDEFINED_RES_ID || mImageId == UNDEFINED_RES_ID) {
            Timber.e(
                "EndorInfoWindowAdapter: unable to get res ids in $packageName"
            )
        }
    }

    override fun onOpen(item: Any?) {
        val overlay = item as OverlayWithIW
        var title = overlay.title
        if (title == null) title = ""
        if (mView == null) {
            Timber.w("Error trapped, BasicInfoWindow.open, mView is null!")
            return
        }
        val temp = mView.findViewById<View>(mTitleId /*R.id.title*/) as TextView

        temp.text = title

        snippet = overlay.snippet
        if (snippet == null) snippet = ""
        val snippetHtml = HtmlCompat.fromHtml(snippet, HtmlCompat.FROM_HTML_MODE_LEGACY)

        buttonUrl.text = snippetHtml


        //handle sub-description, hidding or showing the text view:
        val subDescText = mView.findViewById<View>(mSubDescriptionId) as TextView
        val subDesc = overlay.subDescription
        if (subDesc != null && "" != subDesc) {
            subDescText.text = HtmlCompat.fromHtml(subDesc, HtmlCompat.FROM_HTML_MODE_LEGACY)
            subDescText.visibility = View.VISIBLE
        } else {
            subDescText.visibility = View.GONE
        }

        mMarkerRef = item as Marker
        if (mView == null) {
            Timber.w("Error trapped, MarkerInfoWindow.open, mView is null!")
            return
        }

        val imageView =
            mView.findViewById<View>(mImageId /*R.id.image*/ /*R.id.image*/) as ImageView
        val image: Drawable = mMarkerRef!!.getImage()
        imageView.setImageDrawable(image) //or setBackgroundDrawable(image)?
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView.visibility = View.VISIBLE

    }

    override fun onClose() {
        mMarkerRef = null
    }
}