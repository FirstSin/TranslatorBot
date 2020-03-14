package domain.utils;

import domain.commands.SetMyLangCommand;
import domain.commands.ToLangCommand;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentRequesterTest {
    private ArgumentRequester argumentRequester;

    @Before
    public void setUp() {
        argumentRequester = ArgumentRequester.getInstance();
        argumentRequester.requestArgument(123, new ToLangCommand());
        argumentRequester.requestArgument(555, new SetMyLangCommand());
        argumentRequester.requestArgument(321, new ToLangCommand());
    }

    @Test
    public void whenRequestedIdIsRequestedReturnTrue() {
        assertTrue(argumentRequester.isRequested(123));
        assertTrue(argumentRequester.isRequested(555));
        assertTrue(argumentRequester.isRequested(321));
    }

    @Test
    public void whenCreateNewRequestMapContainsNewId() {
        int id = 111;

        argumentRequester.requestArgument(id, new SetMyLangCommand());

        assertTrue(argumentRequester.isRequested(id));
    }

    @Test
    public void whenRemoveRequestMapNotContainsNewId() {
        int id = 321;

        argumentRequester.getRequestedCommand(id);

        assertFalse(argumentRequester.isRequested(id));
    }

    @Test
    public void getRequestedCommandReturnCorrectCommandType() {
        Class<?> expected = ToLangCommand.class;

        Class<?> actual = argumentRequester.getRequestedCommand(123).getClass();

        assertEquals(expected, actual);
    }
}