package com.mn.service.api_gateway.threadpool;

import com.mn.service.api_gateway.client.MicroServiceClient;
import com.mn.service.api_gateway.logger.ServiceLogger;
import com.mn.service.api_gateway.utility.ResponseManager;
import com.mn.service.api_gateway.models.ResponseModel;


public class Worker extends Thread {
    private int id;
    private ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process()
    {
        ClientRequest request = threadPool.remove();
        if (request != null)
        {
            try
            {
                ResponseModel response = MicroServiceClient.invoke(request);
                ResponseManager.insertResponse(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ServiceLogger.LOGGER.severe("Something went horribly wrong. Request is going to be ignored.");
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                process();
                Thread.sleep(1800);
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
