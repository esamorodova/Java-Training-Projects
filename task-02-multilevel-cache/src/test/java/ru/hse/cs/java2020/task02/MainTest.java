package ru.hse.cs.java2020.task02;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testPutGet() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(1024, 0, path, EvictionPolicy.LRU);
        cache.put(179L, "bbb");
        Assert.assertEquals(cache.get(179L), "bbb");
    }

    @Test
    public void testFixed() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(18, 100, path, EvictionPolicy.LRU);
        cache.put(179L, "b");
        cache.put(178L, "d");
        cache.put(178L, "dasdad");
        Assert.assertEquals(cache.get(179L), "b");
        Assert.assertEquals(cache.get(178L), "dasdad");
    }

    @Test
    public void testReadFromDisk() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(19, 100, path, EvictionPolicy.LRU);
        cache.put(179L, "b");
        cache.put(178L, "d");
        Assert.assertEquals(cache.get(179L), "b");
    }

    @Test
    public void getStringAfterUpdate() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(1024, 0, path, EvictionPolicy.LRU);
        cache.put(1L, "aaa");
        cache.put(1L, "bbb");
        cache.put(1L, "ccc");
        cache.put(1L, "ddd");

        Assert.assertEquals(cache.get(1L), "ddd");
    }

    @Test
    public void getAfterRemove() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(25, 0, path, EvictionPolicy.LRU);
        cache.put(1L, "a");
        cache.put(1L, "b");
        cache.put(1L, "c");
        Assert.assertEquals(cache.get(1L), "c");

        cache.put(2L, "d");
        cache.put(1L, "aaaaaaaa");
        assertNull(cache.get(2L));
    }

    @Test
    public void testLFU() {
        String path;
        try {
            path = Files.createTempDirectory(null).toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        CacheImpl cache = new CacheImpl(33, 0, path, EvictionPolicy.LFU);
        cache.put(1L, "a");
        cache.put(1L, "b");
        cache.put(1L, "c");
        cache.put(2L, "d");
        cache.put(2L, "e");

        cache.put(3L, "aaa");
        // должен выкинуть один элемент id = 2 и оставить id = 1

        Assert.assertEquals(cache.get(1L), "c"); // fail
        assertNull(cache.get(2L));
        Assert.assertEquals(cache.get(3L), "aaa");
    }
}
