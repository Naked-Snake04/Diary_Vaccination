package com.example.diary_vaccination

import android.os.Bundle
import android.os.TokenWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        val toastText = "The Patient has been edited"
        val toastError =  "Fields is not filled. Try again"
        val duration = Toast.LENGTH_SHORT

        val binding = DataBindingUtil.inflate<FragmentEditPatientBinding>(
            inflater, R.layout.fragment_edit_patient, container, false
        )
        val args = EditPatientFragmentArgs.fromBundle(requireArguments())
        binding.savePatient.setOnClickListener {
            if (binding.Name.text.toString() != "" &&
                binding.LastName.text.toString() != "" &&
                binding.Surname.text.toString() != ""&&
                binding.Birthday.text.toString() != ""){
                viewModelPatient.updatePatient(
                    args.patientId,
                    binding.Name.text.toString(),
                    binding.LastName.text.toString(),
                    binding.Surname.text.toString(),
                    binding.Birthday.text.toString()
                )
                Toast.makeText(application, toastText, duration).show()
                binding.Name.text.clear()
                binding.LastName.text.clear()
                binding.Surname.text.clear()
                binding.Birthday.text.clear()
            } else {
                Toast.makeText(application, toastError, duration).show()
            }
        }


        return binding.root
    }

}