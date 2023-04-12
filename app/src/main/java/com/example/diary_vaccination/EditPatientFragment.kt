package com.example.diary_vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.diary_vaccination.database.VaccinationDatabase
import com.example.diary_vaccination.databinding.FragmentEditPatientBinding

private lateinit var viewModelPatient: PatientViewModel

class EditPatientFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val application = requireNotNull(this.activity).application //Инициализация класса для общения с бд
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelPatientFactory = PatientViewModelFactory(dao, application)
        viewModelPatient = ViewModelProvider(this, viewModelPatientFactory)[PatientViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentEditPatientBinding>(
            inflater, R.layout.fragment_edit_patient, container, false
        )
        
        return binding.root
    }

}