package study.customer.ni;

public final class NetworkLiteral
{
	public final static String NULL = "<NULL>";

	public final static String SUCCESS = "<SUCCESS>";
	public final static String FAILURE = "<FAILURE>";

	public final static String EOF = "<EOF>";
	public final static String ERROR = "<ERROR>";

	public static boolean isTrailer(String _line)
	{
		switch(_line)
		{
		case NetworkLiteral.EOF:
		case NetworkLiteral.ERROR:
			return true;
		default:
			return false;
		}
	}

	private NetworkLiteral()
	{
		
	}
}
