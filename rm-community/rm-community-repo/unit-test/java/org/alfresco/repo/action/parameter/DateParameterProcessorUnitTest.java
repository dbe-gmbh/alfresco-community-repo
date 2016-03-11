package org.alfresco.repo.action.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the DateParameterProcessor
 *
 * @author Mark Hibbins
 * @since 2.2
 */
public class DateParameterProcessorUnitTest
{
    private DateParameterProcessor dateParameterProcessor;

    @Before
    public void setUp() throws Exception
    {
        this.dateParameterProcessor = new DateParameterProcessor();
        this.dateParameterProcessor.setName("date");
    }

    @Test
    public void testGetSubstitutionSuggestions_01()
    {
        List<String> suggestions = this.dateParameterProcessor.getSubstitutionSuggestions("date");
        assertTrue(suggestions.contains("date.day.short"));
        assertTrue(suggestions.contains("date.day"));
        assertTrue(suggestions.contains("date.day.long"));
        assertTrue(suggestions.contains("date.day.number"));
        assertTrue(suggestions.contains("date.day.month"));
        assertTrue(suggestions.contains("date.day.year"));
        assertTrue(suggestions.contains("date.month.short"));
        assertTrue(suggestions.contains("date.month"));
        assertTrue(suggestions.contains("date.month.long"));
        assertTrue(suggestions.contains("date.month.number"));
        assertEquals(10, suggestions.size());
    }

    @Test
    public void testGetSubstitutionSuggestions_02()
    {
        List<String> suggestions = this.dateParameterProcessor.getSubstitutionSuggestions("dat");
        assertTrue(suggestions.contains("date.day.short"));
        assertTrue(suggestions.contains("date.day"));
        assertTrue(suggestions.contains("date.day.long"));
        assertTrue(suggestions.contains("date.day.number"));
        assertTrue(suggestions.contains("date.day.month"));
        assertTrue(suggestions.contains("date.day.year"));
        assertTrue(suggestions.contains("date.month.short"));
        assertTrue(suggestions.contains("date.month"));
        assertTrue(suggestions.contains("date.month.long"));
        assertTrue(suggestions.contains("date.month.number"));
        assertEquals(10, suggestions.size());
    }

    @Test
    public void testGetSubstitutionSuggestions_03()
    {
        List<String> suggestions = this.dateParameterProcessor.getSubstitutionSuggestions("at");
        assertTrue(suggestions.contains("date.day.short"));
        assertTrue(suggestions.contains("date.day"));
        assertTrue(suggestions.contains("date.day.long"));
        assertTrue(suggestions.contains("date.day.number"));
        assertTrue(suggestions.contains("date.day.month"));
        assertTrue(suggestions.contains("date.day.year"));
        assertTrue(suggestions.contains("date.month.short"));
        assertTrue(suggestions.contains("date.month"));
        assertTrue(suggestions.contains("date.month.long"));
        assertTrue(suggestions.contains("date.month.number"));
        assertEquals(10, suggestions.size());
    }

    @Test
    public void testGetSubstitutionSuggestions_05()
    {
        List<String> suggestions = this.dateParameterProcessor.getSubstitutionSuggestions("ay");
        assertTrue(suggestions.contains("date.day.short"));
        assertTrue(suggestions.contains("date.day"));
        assertTrue(suggestions.contains("date.day.long"));
        assertTrue(suggestions.contains("date.day.number"));
        assertTrue(suggestions.contains("date.day.month"));
        assertTrue(suggestions.contains("date.day.year"));
        assertEquals(6, suggestions.size());
    }

    @Test
    public void testGetSubstitutionSuggestions_06()
    {
        List<String> suggestions = this.dateParameterProcessor.getSubstitutionSuggestions("on");
        assertTrue(suggestions.contains("date.day.long"));
        assertTrue(suggestions.contains("date.month.short"));
        assertTrue(suggestions.contains("date.month"));
        assertTrue(suggestions.contains("date.month.long"));
        assertTrue(suggestions.contains("date.month.number"));
        assertTrue(suggestions.contains("date.year.long"));
        assertTrue(suggestions.contains("date.day.month"));
        assertEquals(7, suggestions.size());
    }
}
