package com.mn.service.api_gateway.threadpool;

import com.mn.service.api_gateway.logger.ServiceLogger;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        this.head = null;
        this.tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
        try
        {
            wait();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            ServiceLogger.LOGGER.warning("Thread interrupted");
        }
        ListNode node = new ListNode(clientRequest, null);
        if (this.isEmpty())
        {
            this.head = node;
            this.tail = node;
        }
        else
        {
            this.tail.setNext(node);
        }
    }

    public synchronized ClientRequest dequeue() {
        notify();
        if (this.isEmpty())
        {
            return null;
        }
        ClientRequest clientRequest = this.head.getClientRequest();
        if (this.head.getNext() == null)
        {
            this.tail = null;
        }
        this.head = this.head.getNext();
        return clientRequest;
    }

    boolean isEmpty()
    {
        return this.head == null && this.tail == null;
    }
}
