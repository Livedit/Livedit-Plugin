package livedit.handlers;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class Websocket extends WebSocketServer{
	
	private static int counter = 0;
	
	public Websocket( int port , Draft d ) throws UnknownHostException {
		super( new InetSocketAddress( port ), Collections.singletonList( d ) );
	}
	
	public Websocket( InetSocketAddress address, Draft d ) {
		super( address, Collections.singletonList( d ) );
	}
	
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		counter++;
		System.out.println( "///////////Opened connection number" + counter );
	}

	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		System.out.println( "closed" );
	}

	public void onError( WebSocket conn, Exception ex ) {
		System.out.println( "Error:" );
		ex.printStackTrace();
	}

	public void onMessage( WebSocket conn, String message ) {
		conn.send( message );
	}

	public void onMessage( WebSocket conn, ByteBuffer blob ) {
		conn.send( blob );
	}

	public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
		FrameBuilder builder = (FrameBuilder) frame;
		builder.setTransferemasked( false );
		conn.sendFrame( frame );
	}
	
	public static void init() throws UnknownHostException {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		WebSocketImpl.DEBUG = false;
		int port = 9003;
		new Websocket( port, new Draft_17() ).start();
	}
}