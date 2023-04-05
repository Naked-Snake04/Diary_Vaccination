package com.example.diary_vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.diary_vaccination.database.VaccinationDatabase
import com.example.diary_vaccination.databinding.FragmentSettingsMenuBinding

class SettingsMenuFragment : Fragment() {
    private lateinit var viewModelVaccine: AddNewVaccineViewModel
    private lateinit var viewModelEntry: AddNewEntryViewModel
    private lateinit var viewModelPatient: AddNewPatientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSettingsMenuBinding>(
            inflater, R.layout.fragment_settings_menu, container, false)

        val application = requireNotNull(this.activity).application
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelVaccineFactory = AddNewVaccineViewModelFactory(dao, application)
        val viewModelEntryFactory = AddNewEntryViewModelFactory(dao, application)
        val viewModelPatientFactory = AddNewPatientViewModelFactory(dao, application)

        viewModelVaccine = ViewModelProvider(this, viewModelVaccineFactory)[AddNewVaccineViewModel::class.java]
        viewModelEntry = ViewModelProvider(this, viewModelEntryFactory)[AddNewEntryViewModel::class.java]
        viewModelPatient = ViewModelProvider(this, viewModelPatientFactory)[AddNewPatientViewModel::class.java]

        val toastClearPatients = "Patients list has been clear"
        val toastNewVaccine = "Vaccine has been added"
        val toastVaccineError = "Vaccine name is not filled. Try again"
        val toastClearVaccine = "Vaccines list has been clear"
        val toastClearEntries = "Entries has been clear"
        val duration = Toast.LENGTH_SHORT

        /**
         * TODO: Сделать адаптер
         */

        binding.apply {
            clearAllPatient.setOnClickListener {
                viewModelPatient.clearAllPatients()
                Toast.makeText(application, toastClearPatients, duration).show()
            }

            clearAllEntry.setOnClickListener {
                viewModelEntry.clearAllEntries()
                Toast.makeText(application, toastClearEntries, duration).show()
            }

            saveNewVaccine.setOnClickListener {
                if(binding.vaccinationName.text.toString() != ""){
                    viewModelVaccine.initNewVaccine(binding.vaccinationName.text.toString(),
                        binding.vaccinationComponent.isChecked)
                    binding.vaccinationName.text.clear()
                    Toast.makeText(application, toastNewVaccine, duration).show()
                } else {
                    Toast.makeText(application, toastVaccineError, duration).show()
                }
            }

            clearAllVaccination.setOnClickListener {
                viewModelVaccine.clearAllVaccine()
                Toast.makeText(application, toastClearVaccine, duration).show()
            }
        }

        return binding.root
    }
}