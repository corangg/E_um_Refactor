package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.data.mapper.toExternal
import com.domain.model.SignInResult
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @LocalDataSources private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : FirebaseRepository {
    override fun isUserLoggedIn() = flow {
        val auth = FirebaseAuth.getInstance()
        emit(auth.currentUser != null)
    }

    override suspend fun trySignUp(email: String, password: String) = withContext(ioDispatcher) {
        return@withContext try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) SignUpResult.Success
            else SignUpResult.Failure
        } catch (e: FirebaseAuthUserCollisionException) {
            SignUpResult.AlreadyExists
        } catch (e: Exception) {
            SignUpResult.Failure
        }
    }

    override suspend fun setUserData(userInfo: UserInfo) = withContext(ioDispatcher) {
        return@withContext try {
            firestore.collection("UserInfo").document(userInfo.email).set(userInfo).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun trySignIn(email: String, password: String) = withContext(ioDispatcher) {
        return@withContext try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                SignInResult.Success
            } else {
                SignInResult.Failure
            }
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> SignInResult.InvalidEmail
                "ERROR_USER_NOT_FOUND" -> SignInResult.UserNotFound
                "ERROR_WRONG_PASSWORD" -> SignInResult.InvalidPassword
                else -> SignInResult.Failure
            }
        } catch (e: Exception) {
            SignInResult.Failure
        }
    }

    override suspend fun getUserInfo() = withContext(ioDispatcher) {
        return@withContext try {
            val userEmail = auth.currentUser?.email ?: return@withContext null
            val docRef = firestore.collection("UserInfo").document(userEmail).get().await()
            val userDataMap = docRef.data ?: return@withContext null
            UserInfo(
                email = userDataMap["email"] as? String ?: "",
                password = userDataMap["password"] as? String ?: "",
                name = userDataMap["name"] as? String ?: "",
                nickname = userDataMap["nickname"] as? String ?: "",
                phone = userDataMap["phone"] as? String ?: "",
                zoneCode = userDataMap["zoneCode"] as? String ?: "",
                address = userDataMap["address"] as? String ?: "",
                imgUrl = userDataMap["imgUrl"] as? String ?: "",
                statusMessage = userDataMap["statusMessage"] as? String ?: "",
            )
        } catch (e: Exception) {
            null
        }
    }
}