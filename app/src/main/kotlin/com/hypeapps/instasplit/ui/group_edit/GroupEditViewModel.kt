package com.hypeapps.instasplit.ui.group_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupEditViewModel(private val repository: InstaSplitRepository) : ViewModel() {
    private val _state = MutableStateFlow(GroupEditState())
    val state: StateFlow<GroupEditState> = _state.asStateFlow()

    init {
        viewModelScope.launch {

        }
    }
}
