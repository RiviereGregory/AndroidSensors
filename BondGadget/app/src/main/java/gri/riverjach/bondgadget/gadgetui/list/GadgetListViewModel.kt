package gri.riverjach.bondgadget.gadgetui.list

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

    // MediatorLiveData permet d'appelle des liveData en cascade
    private val viewState = MediatorLiveData<GadgetListViewState>()

    fun getViewState(): LiveData<GadgetListViewState> = viewState

    init {
        viewState.addSource(repo.getAllGadgets()) { gadgets ->
            val oldState = viewState.value!!
            viewState.value = oldState.copy(
                hasGadgetsChanged = true,
                gadgets = gadgets
            )
        }

        //initial state
        viewState.value = GadgetListViewState(
            hasGadgetsChanged = false,
            gadgets = emptyList()
        )
    }

    fun addGadget(gadget: Gadget) {
        repo.addGadget(gadget)
    }
}