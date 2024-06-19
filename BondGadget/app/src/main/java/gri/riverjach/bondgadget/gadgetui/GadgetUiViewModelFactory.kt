package gri.riverjach.bondgadget.gadgetui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gri.riverjach.bondgadget.gadgetui.list.GadgetListViewModel
import gri.riverjach.bondgadget.repository.Repo

@Suppress("UNCHECKED_CAST")
class GadgetUiViewModelFactory(private val repo: Repo) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GadgetListViewModel::class.java) -> GadgetListViewModel(repo)

            else -> throw IllegalAccessException("Unexpected model class $modelClass")
        } as T
    }
}