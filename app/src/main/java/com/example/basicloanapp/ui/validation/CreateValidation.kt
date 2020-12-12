package com.example.basicloanapp.ui.validation

import com.example.basicloanapp.R

enum class CreateValidation(val message: Int) {
    NETWORK(R.string.create_validation_network),
    AMOUNT_TYPE_ERROR(R.string.create_validation_amount_type_error),
    AMOUNT_SIZE_ERROR(R.string.create_validation_amount_size_error),
    NAME_EMPTY(R.string.create_validation_name_empty),
    SURNAME_EMPTY(R.string.create_validation_surname_empty),
    PHONE_EMPTY(R.string.create_validation_phone_empty),
    GOOD(R.string.validation_good)
}