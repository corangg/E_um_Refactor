package com.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.util.getLocalTimeToString
import com.data.datasource.LocalDataSource
import com.domain.model.ResponseFriendRequestData
import com.domain.model.SignInResult
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val database: FirebaseDatabase,
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
        val userEmail = auth.currentUser?.email ?: return@withContext null
        getFirebaseUserInfo(userEmail)
    }

    override suspend fun updateProfileImage(uri: String) = withContext(ioDispatcher) {
        return@withContext try {
            val userEmail = auth.currentUser?.email ?: return@withContext ""
            val storagePath = firebaseStorage.reference.child("userProfile").child(userEmail)
                .child("profile_img.jpg")
            storagePath.putFile(uri.toUri()).await()
            storagePath.downloadUrl.await().toString()
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun checkPassword(password: String) = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext false
        val credential = EmailAuthProvider.getCredential(userEmail, password)
        return@withContext try {
            auth.currentUser?.reauthenticate(credential)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun changePassword(password: String) = withContext(ioDispatcher) {
        return@withContext try {
            auth.currentUser?.updatePassword(password)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun trySignOut() = withContext(ioDispatcher) {
        auth.signOut()
    }

    override suspend fun getFriendList() = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext listOf()
        val docRef = firestore.collection("FriendList").document(userEmail).get().await()
        return@withContext docRef.data?.keys?.toList() ?: return@withContext listOf()
    }

    override suspend fun requestFriend(email: String) = withContext(ioDispatcher) {
        val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@withContext false
        val replaceEmail = email.replace(".", "_")
        try {
            val reference = database.reference.child("friendRequest").child(replaceEmail).child(replaceUserEmail)
            reference.setValue(getLocalTimeToString()).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getEmailInfo(email: String) = withContext(ioDispatcher) {
        getFirebaseUserInfo(email)
    }

    private suspend fun getFirebaseUserInfo(email: String): UserInfo? {
        return try {
            val docRef = firestore.collection("UserInfo").document(email).get().await()
            val userDataMap = docRef.data ?: return null
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

    override fun getFirebaseRequestFriendAlarmData() = callbackFlow {
        val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@callbackFlow
        val reference = database.getReference("friendRequest").child(replaceUserEmail)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull { it.key }
                trySend(dataList).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    override suspend fun updateFriendValue(email: String) = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext false
        return@withContext addFriend(userEmail, email) && addFriend(email, userEmail)
    }

    override suspend fun deleteRequestAlarmMessage(email: String) = withContext(ioDispatcher) {
        return@withContext try {
            val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@withContext false
            database.getReference("friendRequest").child(replaceUserEmail).child(email).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun responseFriendRequest(email: String, value: Boolean) = withContext(ioDispatcher) {
        val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@withContext false
        try {
            val reference = database.reference.child("responseFriendRequest").child(email)
                .child(replaceUserEmail)
            reference.setValue(value).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getFirebaseResponseFriendRequestAlarmData() = callbackFlow {
        val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@callbackFlow
        val reference = database.getReference("responseFriendRequest").child(replaceUserEmail)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull {
                    ResponseFriendRequestData(
                        it.key ?: "",
                        it.getValue(Boolean::class.java) ?: false
                    )
                }
                trySend(dataList).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    private suspend fun addFriend(userEmail: String, friendEmail: String): Boolean{
        return try {
            firestore.collection("FriendList").document(userEmail).set(mapOf(friendEmail to getLocalTimeToString())).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}