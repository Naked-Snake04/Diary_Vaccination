package com.example.diary_vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.diary_vaccination.database.VaccinationDatabase
import com.example.diary_vaccination.databinding.FragmentEditEntryBinding

private lateinit var viewModelVaccine: VaccineViewModel
private lateinit var viewModelEntry: EntryViewModel
private lateinit var viewModelPatient: PatientViewModel

class EditEntryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application =
            requireNotNull(this.activity).application //Инициализация класса для общения с бд
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelVaccineFactory = VaccineViewModelFactory(dao, application)
        val viewModelEntryFactory = EntryViewModelFactory(dao, application)
        val viewModelPatientFactory = PatientViewModelFactory(dao, application)

        viewModelVaccine =
            ViewModelProvider(this, viewModelVaccineFactory)[VaccineViewModel::class.java]
        viewModelEntry = ViewModelProvider(this, viewModelEntryFactory)[EntryViewModel::class.java]
        viewModelPatient =
            ViewModelProvider(this, viewModelPatientFactory)[PatientViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentEditEntryBinding>(
            inflater, R.layout.fragment_edit_entry, container, false
        )

        val toastEntry = "Entry edited"
        val toastError = "Fields is not filled. Try again"
        val duration = Toast.LENGTH_SHORT

        val adapterVaccines = VaccineAdapterList(application)
        val adapterPatients = PatientsAdapterList(application)

        binding.vaccinationId.adapter = adapterVaccines
        binding.patientId.adapter = adapterPatients

        viewModelVaccine.vaccines.observe(viewLifecycleOwner) { vaccines ->
            if (vaccines != null)
                adapterVaccines.dataVaccine = vaccines
        }
        viewModelPatient.patients.observe(viewLifecycleOwner) { patients ->
            if (patients != null)
                adapterPatients.dataPatient = patients
        }

        val args = EditEntryFragmentArgs.fromBundle(requireArguments())

        binding.saveEntry.setOnClickListener {
            if(binding.vaccinationId.selectedItemId >= 0 &&
                binding.patientId.selectedItemId >= 0 &&
                binding.editTextDate.text.toString() != "" &&
                binding.editTextTime.text.toString() != ""){
                viewModelEntry.updateEntry(
                    args.entryId,
                    binding.patientId.selectedItemId.toString(),
                    binding.vaccinationId.selectedItemId.toString(),
                    binding.componentNumber.selectedItem.toString(),
                    binding.editTextDate.text.toString(),
                    binding.editTextTime.text.toString()
                )
                Toast.makeText(application, toastEntry, duration).show()
                binding.editTextDate.text.clear()
                binding.editTextTime.text.clear()
            } else {
                Toast.makeText(application, toastError, duration).show()
            }
        }

        return binding.root
    }
}