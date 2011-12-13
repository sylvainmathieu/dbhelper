package fr.zenexity.dbhelper;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class JdbcAdapterTest extends TestingDatabase {

    private JdbcAdapter adapter;

    @Before
    public void register() {
        adapter = JdbcAdapter.defaultAdapter;
    }

    @Test
    public void testNormalizeValueFromSql() {
        assertEquals(new Integer(213), adapter.normalizeValueFromSql(new Integer(213)));
        assertEquals(new Long(213), adapter.normalizeValueFromSql(new Long(213)));
        assertEquals(new Long(213), adapter.normalizeValueFromSql(new BigDecimal(213)));
    }

    @Test
    public void testNormalizeValueForSql() {
        assertEquals(new Integer(213), adapter.normalizeValueForSql(new Integer(213)));
        assertEquals(new Long(213), adapter.normalizeValueForSql(new Long(213)));
        assertEquals(new BigDecimal(213), adapter.normalizeValueForSql(new BigDecimal(213)));
    }

    @Test
    public void testCastValue() {
        assertEquals(new Integer(213), adapter.cast(Integer.class, new Integer(213)));
        assertEquals(new BigDecimal(213), adapter.cast(Number.class, new BigDecimal(213)));
    }

    @Test
    public void testNumberConverter() {
        adapter = JdbcAdapter.defaultBuilder()
                .register(new JdbcAdapter.NumberConverter())
                .create();
        assertEquals(new Integer(123), adapter.cast(Integer.class, new Byte((byte)123)));
        assertEquals(new Integer(213), adapter.cast(Integer.class, new Integer(213)));
        assertEquals(new Byte((byte)123), adapter.cast(Byte.class, new Integer(123)));
        assertEquals(new Byte((byte)123), adapter.cast(Byte.class, new BigDecimal(123)));
        assertEquals(new Short((short)213), adapter.cast(Short.class, new BigDecimal(213)));
        assertEquals(new Integer(213), adapter.cast(Integer.class, new BigDecimal(213)));
        assertEquals(new Long(213), adapter.cast(Long.class, new BigDecimal(213)));
    }

    @Test
    public void testEnumCastValue() {
        assertEquals(Entry.DistType.DEBIAN, adapter.cast(Entry.DistType.class, "DEBIAN"));
        assertEquals(Entry.DistType.UBUNTU, adapter.cast(Entry.DistType.class, Entry.DistType.UBUNTU.ordinal()));
        assertEquals(Entry.DistType.FEDORA, adapter.cast(Entry.DistType.class, Entry.DistType.FEDORA));
    }

    @Test
    public void testDateCastDateValue() {
        Date date = new Date();
        assertEquals(date, adapter.cast(Date.class, new Timestamp(date.getTime())));
    }

}
