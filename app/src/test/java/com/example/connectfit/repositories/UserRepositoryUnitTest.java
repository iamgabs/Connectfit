package com.example.connectfit.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.connectfit.database.UserConfigSingleton;
import com.example.connectfit.enums.UserGroupEnum;
import com.example.connectfit.models.entities.UserEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Config.OLDEST_SDK})
public class UserRepositoryUnitTest {

    @Mock
    private FirebaseUser firebaseUser;

    @Mock
    private Task<AuthResult> authResultTask;

    @Mock
    private DocumentReference userRef;

    @Captor
    private ArgumentCaptor<OnCompleteListener<AuthResult>> authResultListenerCaptor;

    @Captor
    private ArgumentCaptor<EventListener<DocumentSnapshot>> snapshotListenerCaptor;

    @InjectMocks
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
    }

    @Test
    public void saveUserSuccessfullyAndCheckIfTheCurrentUserHasTheFirebaseID() {
        UserEntity user = new UserEntity();
        user.setEmail("user@teste.com");
        user.setName("testeuser");
        user.setPassword("teste123");
        user.setUserGroupEnum(UserGroupEnum.STUDENT);

        FirebaseAuth mAuth = mock(FirebaseAuth.class);
        when(mAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("userId");
        when(mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(authResultTask);
        when(authResultTask.addOnCompleteListener(authResultListenerCaptor.capture())).thenAnswer(invocation -> {
            OnCompleteListener<AuthResult> listener = invocation.getArgument(0);
            listener.onComplete(authResultTask);
            return null;
        });

        doAnswer((Answer<Void>) invocation -> {
            user.setId("userId");
            UserConfigSingleton.getInstance().setInstanceOfCurrentUser(user);
            return null;
        }).when(userRepository).saveUser(any(UserEntity.class));

        userRepository.saveUser(user);

        ArgumentCaptor<Task<AuthResult>> authResultTaskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(authResultTask).addOnCompleteListener(authResultListenerCaptor.capture());

        authResultListenerCaptor.getValue().onComplete(mock(Task.class));

        verify(mAuth).createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
        verify(mAuth).getCurrentUser();

        // GET DATA OF userRef
        verify(userRef).set(anyMap(), eq(SetOptions.merge()));
        verify(userRef).addSnapshotListener(snapshotListenerCaptor.capture());

        snapshotListenerCaptor.getValue().onEvent(mock(DocumentSnapshot.class), null);

        // CHECK IF USER WAS CREATED WITH THE ID
        assertEquals("userId", user.getId());
    }
}
