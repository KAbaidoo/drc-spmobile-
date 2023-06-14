package io.bewsys.spmobile.ui.households.form

import android.content.Context
import android.net.Uri
import android.os.Bundle

import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddMembersDBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class SectionDAddMemberFragment : Fragment(R.layout.fragment_add_members_d) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

//    val args: SectionDAddMemberFragmentArgs by navArgs()

    //

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddMembersDBinding.bind(view)
        val dropdownLayout = R.layout.dropdown_item

        binding.apply {

            Glide.with(photoViewButton)
                .load(R.drawable.baseline_add_a_photo_24)
                .apply(RequestOptions.centerCropTransform())
                .into(photoViewButton)

            setFragmentResultListener("photo_capture_request") { _, bundle ->
                val result = bundle.getString("photo_capture_results")

                Glide.with(photoViewButton)
                    .load(result)
                    .fallback(R.drawable.baseline_add_a_photo_24)
                    .apply(RequestOptions.centerCropTransform())
                    .into(photoViewButton)

                viewModel.photoUri  = fileFromContentUri(requireContext(), Uri.parse(result)).absolutePath

//                Log.d(TAG,"${ viewModel.photoUri}")
            }








            photoViewButton.setOnClickListener {
                val action =
                    SectionDAddMemberFragmentDirections.actionSectionDAddMemberFragmentToCameraFragment()
                findNavController().navigate(action)
            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.memberHasBlankFields.collectLatest {
                    btnFinish.isEnabled = it.not()
                }
            }

            val tils = listOf(
                tilMemberFirstname,
                tilMemberMiddleName,
                tilMemberLastname,
                tilMemberAge
            )


            rgIsHeadOrRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbHead.id -> {
                        viewModel.apply {
                            isMemberHead = getString(R.string.yes)
                            isMemberRespondent = getString(R.string.no)

                            memberFirstname = headFirstName ?: ""
                            memberMiddleName = headMiddleName ?: ""
                            memberLastname = headLastName ?: ""
                            memberSex = headSex ?: ""
                            memberAge = headAge ?: ""

                            tilMemberFirstname.editText?.setText(headFirstName)
                            tilMemberMiddleName.editText?.setText(headMiddleName)
                            tilMemberLastname.editText?.setText(headLastName)
                            tilMemberAge.editText?.setText(headAge ?: "")

                            tils.forEach {
                                it.isEnabled = false
                            }

                            when (viewModel.memberSex) {
                                rbMale.text -> rgMemberSex.check(rbMale.id)
                                rbFemale.text -> rgMemberSex.check(rbFemale.id)
                            }


                        }
                    }

                    rbRespondent.id -> {
                        viewModel.apply {
                            isMemberRespondent = getString(R.string.yes)
                            isMemberHead = getString(R.string.no)

                            memberFirstname = respondentFirstName
                            memberMiddleName = respondentMiddleName
                            memberLastname = respondentLastName
                            memberSex = respondentSex
                            memberAge = respondentAge

                            when (viewModel.memberSex) {
                                rbMale.text -> rgMemberSex.check(rbMale.id)
                                rbFemale.text -> rgMemberSex.check(rbFemale.id)
                            }

                            tilMemberFirstname.editText?.setText(respondentFirstName)
                            tilMemberMiddleName.editText?.setText(respondentMiddleName)
                            tilMemberLastname.editText?.setText(respondentLastName)
                            tilMemberAge.editText?.setText(respondentAge)

                            tils.forEach {
                                it.isEnabled = false
                            }
                            tilMemberAge.isEnabled = true

                        }

                    }

                    else -> {
                        viewModel.apply {
                            memberFirstname = ""
                            memberMiddleName = ""
                            memberLastname = ""
                            memberSex = ""
                            memberAge = ""

                            tils.forEach {
                                it.isEnabled = true
                                it.editText?.text?.clear()
                            }
                            rgMemberSex.clearCheck()

                        }

                    }
                }
            }


            rgMemberAgeKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAge.id -> {
                        viewModel.memberAgeKnown = rbYesAge.text as String
                        tvMemberAge.isEnabled = true
                        tilMemberAge.isEnabled = true
                    }

                    else -> {
                        viewModel.memberAgeKnown = rbNoAge.text as String
                        tilMemberAge.editText?.text?.clear()
                        tvMemberAge.isEnabled = false
                        tilMemberAge.isEnabled = false

                    }
                }
            }
            rgMemberDobKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesDob.id -> {
                        viewModel.memberDOBKnown = rbYesDob.text as String
                        tvMemberDob.isEnabled = true
                        tilMemberDob.isEnabled = true
                    }

                    else -> {
                        viewModel.memberDOBKnown = rbNoDob.text as String
                        tilMemberDob.editText?.text?.clear()
                        tvMemberDob.isEnabled = false
                        tilMemberDob.isEnabled = false

                    }
                }
            }
            rgMemberSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.memberSex = rbMale.text as String
                        viewModel.memberPregnancyStatus = ""
                        rgMemberPregnancyStatus.clearCheck()
                        tvMemberPregnancyStatus.isVisible = false
                        rgMemberPregnancyStatus.isVisible = false
                        rbYesPregnant.isVisible = false
                        rbNoPregnant.isVisible = false
                        viewModel.memberHasBlankFields()
                    }

                    else -> {
                        viewModel.memberSex = rbFemale.text as String
                        tvMemberPregnancyStatus.isVisible = true
                        rgMemberPregnancyStatus.isVisible = true
                        rbNoPregnant.isVisible = true
                        rbYesPregnant.isVisible = true
                        viewModel.memberHasBlankFields()
                    }
                }
            }
            rgMemberPregnancyStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesPregnant.id -> {
                        viewModel.memberPregnancyStatus = rbYesPregnant.text as String
                    }

                    else -> {
                        viewModel.memberPregnancyStatus = rbNoPregnant.text as String
                    }
                }
            }
            rgBirthCertificate.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesBirthCert.id -> {
                        viewModel.memberBirthCertificate = rbYesBirthCert.text as String
                    }

                    else -> {
                        viewModel.memberBirthCertificate = rbNoBirthCert.text as String
                    }
                }
            }


            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(getString(R.string.select_dob))
                .build()
            tilMemberDob.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            tilMemberDob.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
            }

            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.format(it)
                tilMemberDob.editText!!.setText(date)
                viewModel.memberDob = date
            }


            tilMemberFirstname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.memberFirstname.isBlank()) {
                    tilMemberFirstname.error = getString(R.string.field_cannot_be_empty)
                } else tilMemberFirstname.error = null
            }
            tilMemberLastname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.memberLastname.isBlank()) {
                    tilMemberLastname.error = getString(R.string.field_cannot_be_empty)
                } else tilMemberLastname.error = null
            }



            tilMemberFirstname.editText?.addTextChangedListener {
                viewModel.memberFirstname = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberMiddleName.editText?.addTextChangedListener {
                viewModel.memberMiddleName = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberLastname.editText?.addTextChangedListener {
                viewModel.memberLastname = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberAge.editText?.addTextChangedListener {
                viewModel.memberAge = it.toString()
                viewModel.memberHasBlankFields()
            }

            val relationship = resources.getStringArray(R.array.relationship)
            (actRelationship as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, relationship).also {
                        addTextChangedListener {

                            viewModel.memberRelationship = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val maritalStatus = resources.getStringArray(R.array.marital_status)
            (actMaritalStatus as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, maritalStatus).also {
                        addTextChangedListener {

                            viewModel.memberMaritalStatus = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val eduLevel = resources.getStringArray(R.array.level_of_education)
            (actLevelOfEducation as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, eduLevel).also {
                        addTextChangedListener {

                            viewModel.memberEducational = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val memberSchoolAttendance = resources.getStringArray(R.array.school_attendance)
            (actMemberSchoolAttendance as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, memberSchoolAttendance).also {
                        addTextChangedListener {

                            viewModel.memberSchoolAttendance = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val memberDisability = resources.getStringArray(R.array.disability)
            (actMemberDisability as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, memberDisability).also {
                        addTextChangedListener {

                            viewModel.memberDisability = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val socioProfessionalCategory =
                resources.getStringArray(R.array.socio_professional_category)
            (actSocioProfessionalCategory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, socioProfessionalCategory).also {
                        addTextChangedListener {
                            viewModel.memberSocioProfessionalCategory = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val occupation = resources.getStringArray(R.array.occupation)
            (actOccupation as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, occupation).also {
                        addTextChangedListener {
                            viewModel.memberOccupation = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }




            rgMemberAgeKnown.check(rbYesAge.id)
            rgMemberDobKnown.check(rbYesDob.id)





            btnFinish.setOnClickListener {
                viewModel.createMember()
                viewModel.clearMemberFields()
                findNavController().popBackStack()
            }


        }

        // set up menu

        viewModel.memberHasBlankFields()
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    private fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = contentUri.lastPathSegment + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }
    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    companion object {
        const val TAG = "AddMemberFragment"
    }
}