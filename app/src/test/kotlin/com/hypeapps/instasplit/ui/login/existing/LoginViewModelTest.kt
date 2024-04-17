package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.utils.LoginRequest
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
class LoginViewModelTest {

    private lateinit var repository: InstaSplitRepository
    private lateinit var userManager: UserManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        userManager = mockk(relaxed = true)
        viewModel = LoginViewModel(repository, userManager)
    }

    @Test
    fun `login with valid credentials results in success`() = runBlockingTest {
        val email = "user@example.com"
        val password = "password"
        val userId = 1
        val loginRequest = LoginRequest(email, password)

        coEvery { repository.login(loginRequest) } returns Result.success(User(userId = userId, userName = "User", email = email, password = password, phoneNumber = "1234567890"))

        viewModel.updateEmail(TextFieldValue(email))
        viewModel.updatePassword(TextFieldValue(password))

        val result = viewModel.login()

        Assert.assertEquals(LoginResult.SUCCESS, result)
        verify { userManager.currentUserId = userId }
    }

    @Test
    fun `login with empty fields results in empty fields error`() = runBlockingTest {
        viewModel.updateEmail(TextFieldValue(""))
        viewModel.updatePassword(TextFieldValue(""))

        val result = viewModel.login()

        Assert.assertEquals(LoginResult.EMPTY_FIELDS, result)
    }

    @Test
    fun `login with network error results in network error response`() = runBlockingTest {
        viewModel.updateEmail(TextFieldValue("user@example.com"))
        viewModel.updatePassword(TextFieldValue("password"))

        coEvery { repository.login(any()) } throws IOException("Network error")

        val result = viewModel.login()

        Assert.assertEquals(LoginResult.NETWORK_ERROR, result)
    }


    @Test
    fun `login with invalid credentials results in invalid credentials error`() = runBlockingTest {
        viewModel.updateEmail(TextFieldValue("user@example.com"))
        viewModel.updatePassword(TextFieldValue("wrongpassword"))

        coEvery { repository.login(any()) } returns Result.failure(Exception("Invalid credentials"))

        val result = viewModel.login()

        Assert.assertEquals(LoginResult.INVALID_CREDENTIALS, result)
    }

    @Test
    fun `login with server error results in network error response`() = runBlockingTest {
        viewModel.updateEmail(TextFieldValue("user@example.com"))
        viewModel.updatePassword(TextFieldValue("password"))

        coEvery { repository.login(any()) } throws RuntimeException("Server error")

        val result = viewModel.login()

        Assert.assertEquals(LoginResult.NETWORK_ERROR, result)
    }

    @Test
    fun `ensure remember me is updated correctly`() = runBlockingTest {
        viewModel.updateRememberMe(true)
        Assert.assertTrue(viewModel.state.value.rememberMe)

        viewModel.updateRememberMe(false)
        Assert.assertFalse(viewModel.state.value.rememberMe)
    }

    @Test
    fun `check state is cleared correctly`() = runBlockingTest {
        viewModel.updateEmail(TextFieldValue("user@example.com"))
        viewModel.updatePassword(TextFieldValue("password"))
        viewModel.updateRememberMe(true)

        viewModel.clearState()

        Assert.assertEquals("", viewModel.state.value.email.text)
        Assert.assertEquals("", viewModel.state.value.password.text)
        Assert.assertFalse(viewModel.state.value.rememberMe)
    }
}
