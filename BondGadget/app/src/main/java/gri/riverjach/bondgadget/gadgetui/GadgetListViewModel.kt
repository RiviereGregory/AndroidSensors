package gri.riverjach.bondgadget.gadgetui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import gri.riverjach.bondgadget.Gadget
import gri.riverjach.bondgadget.repository.Repo

data class GadgetListViewState(
    val hasGadgetsChanged: Boolean,
    val gadgets: List<Gadget>
)

class GadgetListViewModel(private val repo: Repo) : ViewModel() {

    private val viewState = MediatorLiveData<GadgetListViewState>()

    fun getViewState(): LiveData<GadgetListViewState> = viewState

    fun addGadget(gadget: Gadget) {
        repo.addGadget(gadget)
    }
}