package com.voidcom.videoproject.viewModel.videoFilter

import com.voidcom.v_base.ui.BaseViewModel
import com.voidcom.videoproject.model.videoFilter.FiltersModel

/**
 * Created by voidcom on 2022/3/28 20:51
 * Description:
 */
class FiltersViewModel : BaseViewModel() {
    private val filtersModel: FiltersModel by lazy { FiltersModel() }

}