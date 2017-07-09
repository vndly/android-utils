package com.mauriciotogneri.androidutils.mock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public abstract class MockWebServer implements Runnable
{
    private final int port;
    private boolean isRunning;
    private ServerSocket serverSocket;
    private final List<EndPoint> endPoints;

    public MockWebServer(int port, List<EndPoint> endPoints)
    {
        this.port = port;
        this.endPoints = endPoints;
    }

    public void start()
    {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop()
    {
        try
        {
            isRunning = false;

            if (null != serverSocket)
            {
                serverSocket.close();
                serverSocket = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            serverSocket = new ServerSocket(port);

            while (isRunning)
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    handle(socket);
                    socket.close();
                }
                catch (Exception e)
                {
                    onError(e);
                }
            }
        }
        catch (Exception e)
        {
            onError(e);
        }
    }

    private void handle(Socket socket) throws IOException
    {
        HttpRequest httpRequest = HttpRequest.fromInputStream(socket.getInputStream());

        EndPoint endPoint = endPoint(httpRequest);

        if (endPoint != null)
        {
            endPoint.process(httpRequest).respond(socket.getOutputStream());
        }
        else
        {
            onNotFound(httpRequest);
        }
    }

    protected abstract void onNotFound(HttpRequest httpRequest);

    protected abstract void onError(Exception exception);

    private EndPoint endPoint(HttpRequest httpRequest)
    {
        for (EndPoint endPoint : endPoints)
        {
            if (endPoint.matches(httpRequest))
            {
                return endPoint;
            }
        }

        return null;
    }
}