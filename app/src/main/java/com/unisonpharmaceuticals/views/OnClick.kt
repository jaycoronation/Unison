package com.unisonpharmaceuticals.views

import com.unisonpharmaceuticals.model.VariationResponse

interface OnClick {
    fun clickQuantity(item: VariationResponse.VariationsBean, quantity: String, index: Int)
}