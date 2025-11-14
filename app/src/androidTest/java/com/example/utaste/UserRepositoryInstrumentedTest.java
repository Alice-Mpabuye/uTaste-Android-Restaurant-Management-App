package com.example.utaste;

import static org.junit.Assert.*;
import java.util.List;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.utaste.data.UserRepository;
import com.example.utaste.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserRepositoryInstrumentedTest {

    private UserRepository repo;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext();

        UserRepository.init(context);
        repo = UserRepository.getInstance();
    }

    @Test
    public void testAdminExistsAfterReset() {
        repo.resetDatabase();
        try { Thread.sleep(200); } catch (Exception ignored) {}
        assertNotNull(repo.findByEmail("admin@local"));
    }

    @Test
    public void testChefExistsAfterReset() {
        repo.resetDatabase();
        try { Thread.sleep(200); } catch (Exception ignored) {}
        assertNotNull(repo.findByEmail("chef@local"));
    }

    @Test
    public void testAddUser() {
        User u = new User("test@local", "1234", User.Role.WAITER);
        boolean created = repo.addUser(u);

        assertTrue(created);
        assertNotNull(repo.findByEmail("test@local"));
    }

    @Test
    public void testPreventDuplicateUser() {
        User u1 = new User("dup@local", "111", User.Role.WAITER);
        User u2 = new User("dup@local", "222", User.Role.WAITER);

        assertTrue(repo.addUser(u1));
        assertFalse(repo.addUser(u2));
    }


    @Test
    public void testAuthenticateValid() {
        User u = new User("auth@local", "secret", User.Role.WAITER);
        repo.addUser(u);

        assertTrue(repo.authenticate("auth@local", "secret"));
    }

    @Test
    public void testAuthenticateInvalid() {
        assertFalse(repo.authenticate("nope@local", "zzz"));
    }

    @Test
    public void testDeleteUser() {
        User u = new User("delete@local", "x", User.Role.WAITER);
        repo.addUser(u);

        assertTrue(repo.deleteUser("delete@local"));
        assertNull(repo.findByEmail("delete@local"));
    }

    @Test
    public void testDeleteNonExistingUser() {
        assertFalse(repo.deleteUser("ghost@local"));
    }

    @Test
    public void testUpdatePassword() {
        User u = new User("pwd@local", "old", User.Role.WAITER);
        repo.addUser(u);

        User updated = new User("pwd@local", "newpwd", User.Role.WAITER);
        repo.updateUser("pwd@local", updated);

        assertEquals("newpwd", repo.findByEmail("pwd@local").getPassword());
    }


    @Test
    public void testUpdateUserName() {
        User u = new User("u@local", "x", User.Role.WAITER);
        repo.addUser(u);

        User updated = new User("u@local", "x", User.Role.WAITER);
        updated.setFirstName("Anna");

        boolean ok = repo.updateUser("u@local", updated);

        assertTrue(ok);
        assertEquals("Anna", repo.findByEmail("u@local").getFirstName());
    }

    @Test
    public void testAuthenticateWrongPassword() {
        repo.addUser(new User("x@local", "abc", User.Role.WAITER));
        assertFalse(repo.authenticate("x@local", "wrong"));
    }

    @Test
    public void testPasswordResetRules() {
        User u = new User("waiter1@local", "old", User.Role.WAITER);
        repo.addUser(u);

        // simulate admin reset
        u.setPassword("waiter-pwd");
        repo.updateUser("waiter1@local", u);

        assertEquals("waiter-pwd", repo.findByEmail("waiter1@local").getPassword());
    }
}
