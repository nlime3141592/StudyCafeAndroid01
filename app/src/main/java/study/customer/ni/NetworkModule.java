package study.customer.ni;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public final class NetworkModule implements INetworkModule
{
	private Socket m_socket;
	private boolean m_isClosed;

	private InputStreamReader m_isr;
	private BufferedReader m_br;

	private OutputStreamWriter m_osw;
	private BufferedWriter m_bw;

	public NetworkModule(Socket _socket)
	{
		m_socket = _socket;
		m_isClosed = _socket.isClosed();

		try
		{
			m_isr = new InputStreamReader(_socket.getInputStream());
			m_br = new BufferedReader(m_isr);
			m_osw = new OutputStreamWriter(_socket.getOutputStream());
			m_bw = new BufferedWriter(m_osw);
		}
		catch (IOException e)
		{
			m_isClosed = true;
		}
	}

	public final boolean isClosed()
	{
		return m_isClosed;
	}

	public final void stop()
	{
		try
		{
			m_bw.close();
			m_osw.close();
			m_br.close();
			m_isr.close();
			m_socket.close();
			m_isClosed = true;
		}
		catch (IOException e)
		{
			m_isClosed = true;
		}
	}

	public final String readLine()
	{
		if(m_isClosed)
			return null;

		try
		{
			String line = m_br.readLine();
			
			if(line != null)
				return line;
			
			m_isClosed = true;
			return null;
		}
		catch (IOException e)
		{
			m_isClosed = true;
			return null;
		}
	}

	public final int readByte()
	{
		if(m_isClosed)
			return -1;

		try
		{
			int value = m_socket.getInputStream().read();
			
			if(value != -1)
				return value;
			
			m_isClosed = true;
			return -1;
		}
		catch (IOException e)
		{
			m_isClosed = true;
			return -1;
		} 
	}

	public final void writeLine(String _line)
	{
		if(m_isClosed)
			return;

		try
		{
			if(_line == null)
				m_bw.write(NetworkLiteral.NULL + "\n");
			else
				m_bw.write(_line + "\n");

			m_bw.flush();
		}
		catch (IOException e)
		{
			m_isClosed = true;
		}
	}

	public final void writeByte(int _byte)
	{
		if(m_isClosed)
			return;

		try
		{
			m_socket.getOutputStream().write(_byte);
			m_socket.getOutputStream().flush();
		}
		catch (IOException e)
		{
			m_isClosed = true;
		}
	}
}
