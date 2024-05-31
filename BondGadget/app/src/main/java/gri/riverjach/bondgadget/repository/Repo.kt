package gri.riverjach.bondgadget.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gri.riverjach.bondgadget.Gadget
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
}