package com.example.diary_vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.diary_vaccination.database.VaccinationDatabase
import com.example.diary_vaccination.databinding.FragmentSelectPatientBinding

class SelectPatientFragment : Fragment() {
    private lateinit var viewModelVaccine: AddNewVaccineViewModel
    private lateinit var viewModelEntry: EntryViewModel
    private lateinit var viewModelPatient: PatientViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelVaccineFactory = AddNewVaccineViewModelFactory(dao, application)
        val viewModelEntryFactory = AddNewEntryViewModelFactory(dao, application)
        val viewModelPatientFactory = AddNewPatientViewModelFactory(dao, application)

        viewModelVaccine = ViewModelProvider(this, viewModelVaccineFactory)[AddNewVaccineViewModel::class.java]
        viewModelEntry = ViewModelProvider(this, viewModelEntryFactory)[EntryViewModel::class.java]
        viewModelPatient = ViewModelProvider(this, viewModelPatientFactory)[PatientViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentSelectPatientBinding>(
            inflater, R.layout.fragment_select_patient, container, false)

        val adapterPatient = AllPatientAdapterList(
            object: PatientOnClickListener{
                override fun patientOnClick(id: String) {
                    viewModelPatient.clearToPatient(id)
                    viewModelEntry.clearEntryByPatientId(id)
                }
            })
        binding.selectPatients.adapter = adapterPatient
        viewModelPatient.patients.observe(viewLifecycleOwner, Observer { patients ->
            if(patients!=null)
                adapterPatient.data = patients
        })

        return binding.root
    }
}