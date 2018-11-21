package org.ws.soap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SOAPWebServiceTests {

    private List<String> mockList;

    @Before
    public void classSetup() {
        mockList = mock(List.class);
        when(mockList.get(0)).thenReturn("first");
        when(mockList.add("second")).thenAnswer(invocation -> {
            mockList.add(2, "second");
            return false;
        });
    }

    @Test
    public void whenMockedListGetFirstElementTest() {
        Assert.assertEquals("first", mockList.get(0));
    }


    @Test
    public void whenMockedListAddElementToIndexTest() {
        Assert.assertFalse(mockList.add("second"));
    }

}

class Inner {
    private List<Double> doubles = new ArrayList<Double>(10);

    public List<Double> getDoubles() {
        return Collections.unmodifiableList(doubles);
    }

    public boolean addDouble(Double d) {
        return doubles.add(d);
    }

    public boolean removeDouble(Double d) {
        return doubles.remove(d);
    }

}
