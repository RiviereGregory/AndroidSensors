package gri.riverjach.bondgadget.gadgetui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gri.riverjach.bondgadget.App
import gri.riverjach.bondgadget.Gadget
import gri.riverjach.bondgadget.GadgetNfc
import gri.riverjach.bondgadget.GadgetQRCode
import gri.riverjach.bondgadget.R
import gri.riverjach.bondgadget.gadgetui.GadgetUiViewModelFactory
import timber.log.Timber

class GadgetListFragment : Fragment(), GadgetListAdapter.GadgetListAdapterListener {

    private lateinit var viewModel: GadgetListViewModel
    private lateinit var gadgetListAdapter: GadgetListAdapter
    private val gadgets = mutableListOf<Gadget>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_gadget_list, container, false)
        gadgetListAdapter = GadgetListAdapter(requireContext(), gadgets, this)
        val recyclerView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gadgetListAdapter
        // Inflate the layout for this fragment
        return inflate
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = GadgetUiViewModelFactory(App.repo)
        viewModel =
            ViewModelProviders.of(requireActivity(), factory)[GadgetListViewModel::class.java]
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { updateUi(it!!) })

        viewModel.addGadget(GadgetQRCode(url ="http://qrCode"))
        viewModel.addGadget(GadgetNfc(url ="http://nfc"))
    }

    private fun updateUi(state: GadgetListViewState) {
        Timber.i("New state=$state")

        if (state.hasGadgetsChanged) {
            gadgets.clear()
            gadgets.addAll(state.gadgets)
            gadgetListAdapter.notifyDataSetChanged()
        }
    }

    override fun onGadgetClicked(gadget: Gadget) {
    }
}