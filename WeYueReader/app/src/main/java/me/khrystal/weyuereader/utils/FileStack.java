package me.khrystal.weyuereader.utils;

import java.io.File;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class FileStack {
    private Node node = null;
    private int count = 0;

    public void push(FileSnapshot fileSnapshot) {
        if (fileSnapshot == null)
            return;
        Node fileNode = new Node();
        fileNode.fileSnapshot = fileSnapshot;
        fileNode.next = node;
        node = fileNode;
        ++count;
    }

    public FileSnapshot pop() {
        Node fileNode = node;
        if (fileNode == null) {
            return null;
        }
        FileSnapshot fileSnapshot = fileNode.fileSnapshot;
        node = fileNode.next;
        --count;
        return fileSnapshot;
    }

    public int getSize() {
        return count;
    }

    public class Node {
        FileSnapshot fileSnapshot;
        Node next;
    }

    public static class FileSnapshot {
        public String filePath;
        public List<File> files;
        public int scrollOffset;
    }
}
