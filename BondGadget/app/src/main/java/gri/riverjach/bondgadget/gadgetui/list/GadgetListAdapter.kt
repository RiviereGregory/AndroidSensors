package gri.riverjach.bondgadget.gadgetui.list

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import gri.riverjach.bondgadget.Gadget
import gri.riverjach.bondgadget.GadgetNfc
import gri.riverjach.bondgadget.GadgetQRCode
import gri.riverjach.bondgadget.R
import gri.riverjach.bondgadget.widget.toFormatedString

class GadgetListAdapter(
    context: Context,
    private val gadgets: List<Gadget>,
    private val listener: GadgetListAdapterListener? = null
) : RecyclerView.Adapter<GadgetListAdapter.ViewHolder>(), View.OnClickListener {

    private val qrCodeColor = ColorStateList.valueOf(
        ResourcesCompat.getColor(
            context.resources,
            R.color.qrcode_icon,
            null
        )
    )
    private val nfcCodeColor = ColorStateList.valueOf(
        ResourcesCompat.getColor(
            context.resources,
            R.color.nfc_icon,
            null
        )
    )

    interface GadgetListAdapterListener {
        fun onGadgetClicked(gadget: Gadget)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val iconView = itemView.findViewById<ImageView>(R.id.iconImageView)
        val urlTextView = itemView.findViewById<TextView>(R.id.urlTextView)
        val dateCreated = itemView.findViewById<TextView>(R.id.dateCreatedTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gadget, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gadget = gadgets[position]
        with(holder) {
            cardView.tag = gadget
            cardView.setOnClickListener(this@GadgetListAdapter)

            when (gadget) {
                is GadgetQRCode -> {
                    urlTextView.text = gadget.url
                    iconView.setImageResource(R.drawable.ic_qrcode)
                    ImageViewCompat.setImageTintList(iconView, qrCodeColor)
                }

                is GadgetNfc -> {
                    urlTextView.text = gadget.url
                    iconView.setImageResource(R.drawable.ic_nfc)
                    ImageViewCompat.setImageTintList(iconView, nfcCodeColor)
                }
            }

            dateCreated.text = gadget.dateCreated.toFormatedString()
        }
    }


    override fun getItemCount(): Int = gadgets.size

    override fun onClick(v: View) {
        listener?.onGadgetClicked(v.tag as Gadget)
    }
}