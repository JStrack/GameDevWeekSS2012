package gdwUtils;

import java.nio.charset.Charset;

public abstract class DefaultCharSet
{
	protected static Charset set;
	static
	{
		DefaultCharSet.set = Charset.forName("UTF-8");
	}
	
	public final static Charset getDefaultCharset()
	{
		return DefaultCharSet.set;
	}
}
