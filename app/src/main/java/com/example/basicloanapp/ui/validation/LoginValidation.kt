package com.example.basicloanapp.ui.validation

import com.example.basicloanapp.R

enum class LoginValidation(val message: Int) {
    AWAITING(R.string.validation_awaiting),
    USER_NOT_FOUND(R.string.login_validation_user_not_found),
    NAME_EMPTY(R.string.validation_name_empty),
    PASSWORD_EMPTY(R.string.validation_password_empty),
    NETWORK(R.string.validation_network),
    GOOD(R.string.validation_good)
}