package com.app.matrimony.ui.main

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.matrimony.R
import com.app.matrimony.databinding.ActivityMainBinding
import com.app.matrimony.ui.bottomsheet.ItemListDialogFragment
import com.app.matrimony.utils.Constants.AdapterOptions.UPDATE_PHOTO
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_CAMERA
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_GALLERY
import com.app.matrimony.utils.Constants.PickerOptionsType.TYPE_WHATSAPP
import com.app.matrimony.utils.PermissionUtils.requestPhotoFromCamera
import com.app.matrimony.utils.PermissionUtils.requestPhotoFromGallery
import com.app.matrimony.utils.Utils
import com.app.matrimony.utils.apputils.AlertUtils
import com.app.matrimony.utils.apputils.CustomItemTouchHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnClickAction {

    lateinit var viewModel: MainViewModel

    var binding: ActivityMainBinding? = null

    var type: String = ""
    var cameraImageUri: Uri? = null

    lateinit var photosAdapter: PhotosAdapter

    private val itemTouchHelper by lazy {
        CustomItemTouchHelper.getCustomItemHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        photosAdapter = PhotosAdapter(viewModel.photos)
        photosAdapter.onClickAction = this

        binding?.let {
            it.toolbar.setNavigationOnClickListener {
                finish()
            }
            it.contentMain.tvAddPhoto.setOnClickListener {
                pickPhoto()
            }
            val spanCount =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
            it.contentMain.rvPhotos.apply {
                layoutManager =
                    GridLayoutManager(this@MainActivity, spanCount, RecyclerView.VERTICAL, false)
                adapter = photosAdapter
            }
            itemTouchHelper.attachToRecyclerView(it.contentMain.rvPhotos)
        }

        viewModel.insertAddPhotoItem()

        viewModel.subTitle.observe(this) {
            binding?.contentMain?.tvSubtitle?.text = getString(it)
        }

        lifecycleScope.launch {
            viewModel.eventChannelFlow.collect { state ->
                when (state) {
                    is UpdateListState.DeleteState -> {
                        if (::photosAdapter.isInitialized) {
                            photosAdapter.notifyItemRemoved(state.position)
                            if (state.position == 0) photosAdapter.notifyItemChanged(
                                0,
                                UPDATE_PHOTO
                            )
                        }
                    }

                    is UpdateListState.InsertState -> {
                        if (::photosAdapter.isInitialized) {
                            photosAdapter.notifyItemInserted(state.position)
                        }
                    }

                    is UpdateListState.BulkInsertState -> {
                        if (::photosAdapter.isInitialized) {
                            photosAdapter.notifyItemRangeInserted(state.startPos, state.count)
                        }
                    }

                    is UpdateListState.ErrorState -> {
                        Toast.makeText(this@MainActivity, state.res, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    private fun pickPhoto() {
        if (viewModel.photos.size < 6)
            ItemListDialogFragment {
                type = it
                pickerPhoto()
            }.show(supportFragmentManager, "PhotoUploadOption")
        else Toast.makeText(this, R.string.toast_max_limit, Toast.LENGTH_SHORT).show()
    }

    private fun pickerPhoto() {
        when (type) {
            TYPE_CAMERA -> {
                Utils.getImageFile(this).let {
                    cameraImageUri =
                        FileProvider.getUriForFile(
                            this,
                            "$packageName.provider",
                            it
                        )
                    requestPhotoFromCamera(
                        this, cameraImageUri,
                        registerForPermissionRequest, registerPhotoFromCamera
                    )
                }
            }

            TYPE_GALLERY -> {
                requestPhotoFromGallery(
                    this,
                    registerForPermissionRequest, registerPhotoFromGallery
                )
            }

            TYPE_WHATSAPP -> {
                Utils.launchWhatsapp(this)
            }
        }
    }

    private val registerForPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) pickerPhoto()
        }

    private val registerPhotoFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                cameraImageUri?.let {
                    viewModel.addPhotos(it)
                }
            }
        }

    private val registerPhotoFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                activityResult.data?.clipData?.let {
                    viewModel.bulkAddPhotos(it)
                }
                activityResult.data?.data?.let {
                    viewModel.addPhotos(it)
                }
            }
        }

    override fun onClickDelete(item: String) {
        AlertUtils.alertDialogTwoAction(
            this,
            getString(R.string.delete_photo),
            getString(R.string.delete_message)
        ) {
            if (!it) {
                viewModel.removePhoto(item)
            }
        }
    }

    override fun onClickAddPhoto() {
        pickPhoto()
    }

}