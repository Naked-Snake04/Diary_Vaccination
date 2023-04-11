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
import com.example.diary_vaccination.databinding.FragmentAddNewPatientBinding

class AddNewPatientFragment : Fragment() {

    private lateinit var viewModel: PatientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentAddNewPatientBinding>(
            inflater, R.layout.fragment_add_new_patient, container, false)

        val application = requireNotNull(this.activity).application
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelFactory = AddNewPatientViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[PatientViewModel::class.java]

        val toastText = "The Patient has been added"
        val toastError =  "Fields is not filled. Try again"
        val duration = Toast.LENGTH_SHORT

        binding.saveNewPatient.setOnClickListener {
            if (binding.Name.text.toString() != "" &&
                binding.LastName.text.toString() != "" &&
                binding.Surname.text.toString() != ""){
                viewModel.initNewPatient(
                    binding.Name.text.toString(),
                    binding.LastName.text.toString(),
                    binding.Surname.text.toString(),
                    binding.Birthday.text.toString()
                )
                Toast.makeText(application, toastText, duration).show() // Успешный успех
                binding.Name.text.clear()
                binding.LastName.text.clear()
                binding.Surname.text.clear()
                binding.Birthday.text.clear()
            } else {
                Toast.makeText(application, toastError, duration).show() //Не успешный не успех
            }
        }

        return binding.root
    }
}