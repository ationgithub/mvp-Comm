package com.company.project.utils;
import java.util.ArrayList;

public class Hash {

    // 最开始的最大容量
    private static final int MAX_SIZE = 21;
    /*
     * 扩容因子 当达到最大容量*0.75时扩容倍
     */
    private static final float LOAD_FACTOR = 0.75f;
    /*
     * 链表节点
     */
    class Entry {
        private int key;
        private int value;
        private Entry next;

        public Entry(int key, int value, Entry next) {
            super();
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Entry table[] = new Entry[MAX_SIZE];
    private long szie = 0;
    private long use = 0;
    private ArrayList<Integer> keyset = new ArrayList<Integer>();

    /*
     * 通过除留取余法计算key在表中的hash位置
     */
    private int hash(int key) {
        return key % this.table.length;
    }

    /*
     * 将数据以key，value方式插入
     */
    public void put(int key, int value) {
        int index = hash(key);
        // 首先判断数组在index位置是否已有数据
        if (table[index] == null) {
            table[index] = new Entry(-1, -1, null);
        }
        Entry e = table[index];
        if (e.next == null) {
            table[index].next = new Entry(key, value, null);
            szie++;
            use++;
            addKey(key);
            // 扩容
            if (use >= table.length * LOAD_FACTOR) {
                resize();
            }
        } else {
            for (e = e.next; e != null; e = e.next) {
                int k = e.key;
                if (k == key) {// 如果已经有该key则改变value值
                    e.value = value;
                    return;
                }
            }

            Entry temp = table[index].next;
            Entry newEntry = new Entry(key, value, temp);// 头插法
            table[index].next = newEntry;
            szie++;
            addKey(key);
        }
    }

    /*
     * 删除
     */
    public void remove(int key) {
        int index = hash(key);
        Entry e = table[index];
        Entry pre = table[index];
        if (e != null && e.next != null) {
            for (e = e.next; e != null; pre = e, e = e.next) {
                int k = e.key;
                if (k == key) {
                    removeSetKey(key);
                    pre.next = e.next;
                    szie--;
                    return;
                }
            }
        }
    }

    /*
     * 通过key获取value
     *
     */
    public int get(int key) {
        int index = hash(key);
        Entry e = table[index];
        if (e != null && e.next != null) {
            for (e = e.next; e != null; e = e.next) {
                int k = e.key;
                if (key == k) {
                    return e.value;
                }
            }
        }
        return -1;
    }

    /*
     * 获取元素个数
     */
    public long size() {
        return this.szie;
    }

    /* 扩容 */
    private void resize() {
        int newlength = table.length * 2;
        Entry[] oldTable = table;
        table = new Entry[newlength];
        use = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && oldTable[i].next != null) {
                Entry e = oldTable[i];
                while (null != e.next) {
                    Entry next = e.next;
                    int index = hash(next.key);
                    if (table[index] == null) {
                        use++;
                        table[index] = new Entry(-1, -1, null);
                    }
                    Entry temp = table[index].next;
                    Entry newEntry = new Entry(next.key, next.value, temp);// 头插法
                    table[index].next = newEntry;
                    e = next;
                }
            }
        }
    }

    public ArrayList<Integer> Keyset() {
        return keyset;
    }

    private void addKey(Integer key) {

        this.keyset.add(key);
    }

    private void removeSetKey(Integer key) {

        this.keyset.remove(key);
    }

    public static void main(String[] args) {
        Hash test = new Hash();
        test.put(1, 2);
        test.put(3, 5);
        test.put(2, 5);
        test.put(9, 3);

        for (int x : test.Keyset()) {
            System.out.println(x);
        }
        System.out.println("===============================");
        test.remove(3);
        for (int x : test.Keyset()) {
            System.out.println(x);
        }
    }

}


