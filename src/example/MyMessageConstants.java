package example;

/**
 * Eine Klasse die die Codes für meine Netzwerkprotokol defniert.
 * Es handelt sich nur um einen byte-Wert. 
 * Ich empfehle auch soe eine Klasse zu implementieren um nicht irgendwo im Code eine nichts
 * aussagende Zahl stehen zu haben. Zumal es man es ja für Client und Server implementieren müsste ;)
 * @author firen
 *
 */
public final class MyMessageConstants
{
	public static final byte ChatMessage = 1;

	public static final byte PlayerConnected = 2;

	public static final byte PlayerDisconnected = 3;
}
