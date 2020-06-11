package com.exam.exam0.page

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.exam.exam0.R
import com.exam.exam0.base.BaseFragment
import com.exam.exam0.data.remote.RemoteDto
import com.exam.exam0.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddMedFragment : BaseFragment<ViewModel>() {

    override val viewModel: AppViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnectivity()
        setupListeners()
    }

    private fun observeConnectivity() {
        viewModel.getConnectivityStatus().observeNonNull(this) {
            button_add.isEnabled = it
        }
    }

    private fun setupListeners() {
        best_before_edittext_add.setText(LocalDate.now().toString())
        button_add.setOnClickListener {
            if (name_edittext_add.text.isNullOrEmpty()){
                showError("Name is empty!")
            } else if (pieces_spinner_add == null || pieces_spinner_add.getSelectedItem() == null ) {
                showError("Pieces is empty!")
            } else if (type_spinner_add == null || type_spinner_add.getSelectedItem() == null ) {
                showError("Type is empty!")
            } else if (best_before_edittext_add.text.isNullOrEmpty() ) {
                showError("Best before is empty!")
            } else if (base_substance_edittext_add.text.isNullOrEmpty() ) {
                showError("Base substance is empty!")
            } else if (quantity1_spinner_add == null || quantity1_spinner_add.getSelectedItem() == null ||
                quantity2_spinner_add == null || quantity2_spinner_add.getSelectedItem() == null ) {
                showError("Base substance quantity is empty!")
            } else if (description_edittext_add.text.isNullOrEmpty() ) {
                showError("Description is empty!")
            }  else {
                val name = name_edittext_add.text.toString()

                val pieces = pieces_spinner_add.selectedItem.toString().toInt()

                val type = type_spinner_add.selectedItem.toString()

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date =  java.sql.Date.valueOf(LocalDate.parse(best_before_edittext_add.text.toString(), formatter).toString())


                val imageView = image_add
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val image_in_byte = baos.toByteArray()

                val base_substance = base_substance_edittext_add.text.toString()

                val base_substance_quantity1 = quantity1_spinner_add.selectedItem.toString()
                val base_substance_quantity2 = quantity2_spinner_add.selectedItem.toString()
                val concat = "$base_substance_quantity1 $base_substance_quantity2"

                val description = description_edittext_add.text.toString()
                GlobalScope.launch {
                    val orderDto = RemoteDto(viewModel.getNewId(), name, pieces, type, date, Base64.getEncoder().encodeToString(image_in_byte), base_substance, concat, description)
                    viewModel.addMedRemote(orderDto) {showError}
                    //TO-DO : show success message.
                }

            }

        }

        button_cancel_add.setOnClickListener{
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.remove(this@AddMedFragment)
            transaction.commit()
        }
    }

    private val showError: (String) -> Unit = {
        activity?.runOnUiThread {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

}
