package gri.riverjach.bondgadget.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gri.riverjach.bondgadget.Gadget
import gri.riverjach.bondgadget.GadgetNfc
import gri.riverjach.bondgadget.GadgetQRCode
import java.util.Date

class Repo {
    private val gadgets = mutableListOf<Gadget>()

    private val gadgetsLiveDate = MutableLiveData<List<Gadget>>()
    fun getAllGadgets(): LiveData<List<Gadget>> = gadgetsLiveDate


    companion object { // pour que l'on ne puisse pas repartir de 1 si plusieur instance
        private var gadgetId = 1
    }

    fun addGadget(gadget: Gadget) {
        gadget.id = gadgetId
        gadget.dateCreated = Date()
        gadgets.add(0, gadget)
        gadgetId++

        gadgetsLiveDate.value = gadgets
    }

    fun getGadgetQRCodeById(gadgetId: Int): GadgetQRCode? {
        return findGadgetById(gadgetId)
    }

    fun getGadGetNfcById(gadgetId: Int): GadgetNfc? {
        return findGadgetById(gadgetId)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Gadget> findGadgetById(gadgetId: Int): T? {
        return gadgets.find { gadget -> gadget.id == gadgetId } as T?
    }
}