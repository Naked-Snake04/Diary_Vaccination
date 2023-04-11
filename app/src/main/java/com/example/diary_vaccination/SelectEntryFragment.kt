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

        val binding = DataBindingUtil.inflate<FragmentSelectEntryBinding>(
            inflater, R.layout.fragment_select_entry, container, false)

        val adapterEntry = AllEntryAdapterList(
            object: EntryOnClickListener{
                override fun entryOnClick(id: String) {
                    viewModelEntry.clearToEntry(id)
                }

                override fun navigateEdit() {
                    view?.let {
                        Navigation.findNavController(it)
                            .navigate(R.id.action_selectEntryFragment_to_editEntryFragment)
                    }
                }
            }
        )

        binding.selectEntries.adapter = adapterEntry
        viewModelEntry.entries.observe(viewLifecycleOwner, Observer { entries ->
            if (entries != null)
                adapterEntry.data = entries
        })

        return binding.root
    }

}