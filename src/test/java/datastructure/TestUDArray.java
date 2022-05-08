package datastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestUDArray {
    private UDArray<Integer> udArray;

    @BeforeEach
    public void setup() {
        udArray = new UDArray<>();
        udArray.add(1);
        udArray.add(2);
        udArray.add(3);
        udArray.add(4);
        udArray.add(5);
        udArray.add(6);
    }

    @Test
    public void shouldAddANumber() {
        udArray.add(7);

        Assertions.assertEquals(7, udArray.get(6));
    }

    @Test
    public void shouldGetAnItemAtGivenIndex() {
        Assertions.assertEquals(3, udArray.get(2));
    }

    @Test
    public void shouldThrowIndexOutOfBoundException() {
        UDArray<Integer> mocked = Mockito.mock(udArray.getClass());
        Mockito.when(mocked.get(100)).thenThrow(new IndexOutOfBoundsException());
    }

    @Test
    public void shouldReturnSize() {
        Assertions.assertEquals(6, udArray.getSize());
    }

    @Test
    public void shouldGetContainer() {
        Assertions.assertEquals(8, udArray.getContainer().length);
    }
}
