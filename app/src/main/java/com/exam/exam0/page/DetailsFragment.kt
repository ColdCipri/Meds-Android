package com.exam.exam0.page

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModel
import com.exam.exam0.R
import com.exam.exam0.base.BaseFragment
import com.exam.exam0.data.local.LocalModel
import com.exam.exam0.data.remote.RemoteDto
import com.exam.exam0.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.util.*


class DetailsFragment(private var item: LocalModel) : BaseFragment<ViewModel>() {

    override val viewModel: AppViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false);


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnectivity()
        setupListeners()
        fillTextboxes()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                val transaction = activity!!.supportFragmentManager.beginTransaction()
                transaction.remove(this@DetailsFragment)
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }

    private fun fillTextboxes() {

        id_textview.setText(item.id.toString())
        name_edittext_details.setText(item.name)

        val piecesSpinnerAdapter : ArrayAdapter<Int> = ArrayAdapter(context!!,
            R.layout.support_simple_spinner_dropdown_item, (0..100).toList())
        pieces_spinner_details.adapter = piecesSpinnerAdapter
        val posOfPieces = piecesSpinnerAdapter.getPosition(item.pieces)

        pieces_spinner_details.setSelection(posOfPieces)

        val typeSpinner : ArrayAdapter<String> = type_spinner_details.adapter as ArrayAdapter<String>
        val posOfType = typeSpinner.getPosition(item.type)

        type_spinner_details.setSelection(posOfType)

        /*var dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var formattedDate = item.best_before.format(dateTimeFormat)

        best_before_edittext_details.setText(LocalDateTime.parse(item.best_before.toString(), dateTimeFormat).toString())*/

        best_before_edittext_details.setText(item.best_before.toString())


        base_substance_edittext_details.setText(item.base_substance)

        val stringSplit = item.base_substance_quantity.split(" ")

        val baseSubstanceQuantity1Spinner : ArrayAdapter<Int> = quantity1_spinner_details.adapter as ArrayAdapter<Int>
        val posOfBsq1 = baseSubstanceQuantity1Spinner.getPosition(stringSplit[0].toInt())

        quantity1_spinner_details.setSelection(posOfBsq1)

        val baseSubstanceQuantity2Spinner : ArrayAdapter<String> = quantity1_spinner_details.adapter as ArrayAdapter<String>
        val posOfBsq2 = baseSubstanceQuantity2Spinner.getPosition(stringSplit[1])

        quantity2_spinner_details.setSelection(posOfBsq2)

        description_edittext_details.setText(item.description)


    }

    private fun observeConnectivity() {
        viewModel.getConnectivityStatus().observeNonNull(this) {
            update_button.isEnabled = it
            remove_button.isEnabled = it
        }
    }


    private fun setupListeners() {

        update_button.setOnClickListener {
            if (name_edittext_details.text.isNullOrEmpty()){
                showError("Name is empty!")
            } else if (pieces_spinner_details == null || pieces_spinner_details.getSelectedItem() == null ) {
                showError("Pieces is empty!")
            } else if (type_spinner_details == null || type_spinner_details.getSelectedItem() == null ) {
                showError("Type is empty!")
            } else if (best_before_edittext_details.text.isNullOrEmpty() ) {
                showError("Best before is empty!")
            } else if (base_substance_edittext_details.text.isNullOrEmpty() ) {
                showError("Base substance is empty!")
            } else if (quantity1_spinner_details == null || quantity1_spinner_details.getSelectedItem() == null ||
                quantity2_spinner_details == null || quantity2_spinner_details.getSelectedItem() == null ) {
                showError("Base substance quantity is empty!")
            } else if (description_edittext_details.text.isNullOrEmpty() ) {
                showError("Description is empty!")
            } else {
                val id = id_textview.text.toString().toInt()

                val name = name_edittext_details.text.toString()

                val pieces = pieces_spinner_details.selectedItem.toString().toInt()

                val type = type_spinner_details.selectedItem.toString()

                val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd")
                val date =  java.sql.Date.valueOf(LocalDate.parse(best_before_edittext_details.text.toString(), formatter).toString())

                //date needs to be updated

                val imageView = image_details
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val image_in_byte = baos.toByteArray()

                val base_substance = base_substance_edittext_details.text.toString()

                val base_substance_quantity1 = quantity1_spinner_details.selectedItem.toString()
                val base_substance_quantity2 = quantity2_spinner_details.selectedItem.toString()
                val concat = "$base_substance_quantity1 $base_substance_quantity2"

                val description = description_edittext_details.text.toString()

                val orderDto = RemoteDto(id, name, pieces, type, date,  Base64.getEncoder().encodeToString(image_in_byte), base_substance, concat, description)
                viewModel.updateMedRemote(id, orderDto) {showError}
            }

        }

        remove_button.setOnClickListener {
            val id = id_textview.text.toString().toInt()
            viewModel.deleteMedRemote(id) {showError}
        }

        cancel_button_details.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.remove(this@DetailsFragment)
            transaction.commit()
        }
    }

    private val showError: (String) -> Unit = {
        activity?.runOnUiThread {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}
