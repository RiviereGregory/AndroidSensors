package gri.riverjach.bondgadget.gadgetui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import gri.riverjach.bondgadget.App
import gri.riverjach.bondgadget.R

class GadgetListFragment : Fragment() {

    private lateinit var viewModel: GadgetListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gadget_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = GadgetUiViewModelFactory(App.repo)
        viewModel =
            ViewModelProviders.of(requireActivity(), factory).get(GadgetListViewModel::class.java)
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { updateUi(it!!) })
    }

    private fun updateUi(state: GadgetListViewState) {

    }
}