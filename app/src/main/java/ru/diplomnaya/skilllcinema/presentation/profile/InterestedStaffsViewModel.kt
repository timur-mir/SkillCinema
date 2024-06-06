package ru.diplomnaya.skilllcinema.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.InterestedStaffRepository
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity

class InterestedStaffsViewModel private constructor(
    private val repository: InterestedStaffRepository
) : ViewModel() {
    constructor() : this(InterestedStaffRepository())
    private val existInterestedStaff = MutableLiveData<Boolean>()
    val existInterestedStaffLive: LiveData<Boolean>
        get() = existInterestedStaff
    private val interestedStaffs: MutableLiveData<List<InterestedStaffEntity>> = MutableLiveData()
    val interestedStaffsLiveData: LiveData<List<InterestedStaffEntity>>
        get() = interestedStaffs
    fun saveInterestedStaff(film: InterestedStaffEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInterestedStaff(film)
        }
    }
    suspend fun checkInterestedStaff(id: Int){
        try {
            existInterestedStaff.postValue(repository.checkInterestedStaff(id))
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
    fun getInterestedStaffs() {
        viewModelScope.launch {
            repository.getInterestedStaffs()
                .catch { e->
                    Log.d("error","${e.message}")
                }
                .collect { response ->
                    interestedStaffs.value=response
                }
        }
    }
    suspend fun removeAllInterestedStaffs(){
        viewModelScope.launch {
            try {
                repository.removeAllInterestedStaffs()
            } catch (t: Throwable) {

            }

        }
    }

}