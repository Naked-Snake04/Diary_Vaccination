package com.example.diary_vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.diary_vaccination.database.VaccinationDatabase
import com.example.diary_vaccination.databinding.FragmentSelectEntryBinding

class SelectEntryFragment : Fragment() {
    private lateinit var viewModelVaccine: VaccineViewModel
    private lateinit var viewModelEntry: EntryViewModel
    private lateinit var viewModelPatient: PatientViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dao = VaccinationDatabase.getInstance(application).getDiaryVaccinationDao()
        val viewModelVaccineFactory = VaccineViewModelFactory(dao, application)
        val viewModelEntryFactory = EntryViewModelFactory(dao, application)
        val viewModelPatientFactory = PatientViewModelFactory(dao, application)

        viewModelVaccine = ViewModelProvider(this, viewModelVaccineFactory)[VaccineViewModel::class.java]
        viewModelEntry = ViewModelProvider(this, viewModelEntryFactory)[EntryViewModel::class.java]
        viewModelPatient = ViewModelProvider(this, viewModelPatientFactory)[PatientViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentSelectEntryBinding>(
            inflater, R.layout.fragment_select_entry, container, false)

        val adapterEntry = AllEntryAdapterList(
            object: EntryOnClickListener{
                override fun entryOnClick(id: String) {
                    viewModelEntry.clearToEntry(id)
                }

                override fun navigateEdit(id: String) {
                    view?.let {
                        Navigation.findNavController(it)
                            .navigate(SelectEntryFragmentDirections
                                .actionSelectEntryFragmentToEditEntryFragment(id))
                    }
                }
            }
        )

        binding.selectEntries.adapter = adapterEntry
        viewModelEntry.entries.observe(viewLifecycleOwner, Observer { entries ->
            if (entries != null)
                adapterEntry.data = entries
        })

        binding.filterButton.setOnClickListener {
            val filter = EntryViewModel.FilterData()
            filter.lastName = binding.editTextFilterLastName.text.toString()
            filter.vaccineDate = binding.filterVaccineDate.text.toString()

            viewModelEntry.filter.postValue(filter)
        }

        return binding.root
    }

}