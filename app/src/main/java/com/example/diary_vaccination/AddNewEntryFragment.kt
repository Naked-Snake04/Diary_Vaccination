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
import com.example.diary_vaccination.databinding.FragmentAddNewEntryBinding

private lateinit var viewModel: AddNewEntryViewModel

class AddNewEntryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application //Инициализация класса для общения с бд
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelFactory = AddNewEntryViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddNewEntryViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentAddNewEntryBinding>(
            inflater, R.layout.fragment_add_new_entry, container, false)

        val toastAddNewEntry = "Entry added"
        val toastError = "Fields is not filled. Try again"
        val duration = Toast.LENGTH_SHORT

        /**
         * TODO: Добавить адаптеры
         */

        binding.addNewEntry.setOnClickListener {
            if(binding.vaccinationId.selectedItemId >= 0 &&
                    binding.patientId.selectedItemId >= 0 &&
                    binding.editTextDate.text.toString() != "" &&
                    binding.editTextTime.text.toString() != ""){
                viewModel.initNewEntry(binding.patientId.selectedItemId,
                    binding.vaccinationId.selectedItemId,
                    binding.componentNumber.selectedItemId + 1,
                    binding.editTextDate.text.toString(),
                    binding.editTextTime.text.toString()
                )
                Toast.makeText(application, toastAddNewEntry, duration).show()
                binding.editTextDate.text.clear()
                binding.editTextTime.text.clear()
            } else {
                Toast.makeText(application, toastError, duration).show()
            }
        }

        return binding.root
    }
}