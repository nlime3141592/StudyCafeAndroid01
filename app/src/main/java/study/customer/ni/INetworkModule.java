package study.customer.ni;

public interface INetworkModule
{
	boolean isClosed();

	String readLine();
	int readByte();
	void writeLine(String _line);
	void writeByte(int _byte);
}
