package com.example.customermodule;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Customer
{
    public Customer(String _message)
    {
        String path = "C:\\Programming\\Java\\test.txt";
        File file = new File(path);

        try
        {
            BufferedWriter writer = new BufferedWriter((new FileWriter(file)));
            writer.write(_message);
            writer.close();
        }
        catch(IOException _ioEx)
        {
            System.out.println("메시지 출력에 실패했습니다.");
        }
    }
}