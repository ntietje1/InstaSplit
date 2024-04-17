package com.hypeapps.instasplit.ui.login.register

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.utils.RegisterRequest
import com.hypeapps.instasplit.core.utils.UserManager
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    private lateinit var instaSplitRepository: InstaSplitRepository
    private lateinit var userManager: UserManager
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        instaSplitRepository = mockk(relaxed = true)
        userManager = mockk(relaxed = true)
        registerViewModel = RegisterViewModel(instaSplitRepository, userManager)
    }

    @Test
    fun `test registration with valid fields`() = runBlockingTest {
        val registerRequest = RegisterRequest("name", "email", "password", "phoneNumber")
        val expectedUserId = 1

        coEvery { instaSplitRepository.register(registerRequest) } returns Result.success(User(
            userId = expectedUserId,
            userName = "name",
            email = "email",
            password = "password",
            phoneNumber = "phoneNumber"
        ))

        registerViewModel.updateName(TextFieldValue("name"))
        registerViewModel.updateEmail(TextFieldValue("email"))
        registerViewModel.updatePassword(TextFieldValue("password"))
        registerViewModel.updatePhoneNumber(TextFieldValue("phoneNumber"))

        val result = registerViewModel.login()

        Assert.assertEquals(RegisterResult.SUCCESS, result)
        verify { userManager.setUserId(expectedUserId) }
    }

    @Test
    fun `test registration with empty fields`() = runBlockingTest {
        registerViewModel.updateName(TextFieldValue(""))
        registerViewModel.updateEmail(TextFieldValue(""))
        registerViewModel.updatePassword(TextFieldValue(""))
        registerViewModel.updatePhoneNumber(TextFieldValue(""))

        val result = registerViewModel.login()

        Assert.assertEquals(RegisterResult.EMPTY_FIELDS, result)
    }

    @Test
    fun `test registration with network error`() = runBlockingTest {
        val registerRequest = RegisterRequest("name", "email", "password", "phoneNumber")

        coEvery { instaSplitRepository.register(registerRequest) } returns Result.failure(
            IOException()
        )

        registerViewModel.updateName(TextFieldValue("name"))
        registerViewModel.updateEmail(TextFieldValue("email"))
        registerViewModel.updatePassword(TextFieldValue("password"))
        registerViewModel.updatePhoneNumber(TextFieldValue("phoneNumber"))

        val result = registerViewModel.login()

        Assert.assertEquals(RegisterResult.NETWORK_ERROR, result)
    }

    @Test
    fun `test registration when user already exists`() = runBlockingTest {
        val registerRequest = RegisterRequest("name", "email", "password", "phoneNumber")

        coEvery { instaSplitRepository.register(registerRequest) } returns Result.failure(Exception("User already exists"))

        registerViewModel.updateName(TextFieldValue("name"))
        registerViewModel.updateEmail(TextFieldValue("email"))
        registerViewModel.updatePassword(TextFieldValue("password"))
        registerViewModel.updatePhoneNumber(TextFieldValue("phoneNumber"))

        val result = registerViewModel.login()

        Assert.assertEquals(RegisterResult.ALREADY_EXISTS, result)
    }
}
