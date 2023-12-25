package com.app.matrimony.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.matrimony.R
import com.app.matrimony.databinding.FragmentItemListDialogListDialogBinding
import com.app.matrimony.utils.apputils.DividerItemDecorator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ItemListDialogFragment(private val callback: (String) -> Unit) : BottomSheetDialogFragment(),
    OnOptionSelection {

    private var _binding: FragmentItemListDialogListDialogBinding? = null

    private val binding get() = _binding!!

    lateinit var viewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { context ->
            ContextCompat.getDrawable(context, R.drawable.item_divider)?.let {
                binding.list.addItemDecoration(DividerItemDecorator(it))
            }
        }
        viewModel.pickerOptions.observe(this) {
            binding.list.adapter = ItemAdapter(it, this)
        }
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionSelected(value: String) {
        dismiss()
        callback.invoke(value)
    }
}