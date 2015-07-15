package livedit.handlers;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.netty.NettyWebServer;

public class LivEditHandler extends AbstractHandler{
	protected boolean WBind = false;
	
	WebServer webServer = null;
	
	public LivEditHandler() {
	}

	public Object execute(ExecutionEvent event)  {
		try {
			if(!WBind){
	        	webServer = WebServers.createWebServer(8080)
	        			.add("/", new Websocket())
	                    .start()
	                    .get();
	        	WBind = true;
	        	System.out.println("Create WebSocket Server");
			} else{
				WBind = false;
				webServer.stop();
				System.out.println("Close WebSocket Server");
			}
		} catch (InterruptedException | java.util.concurrent.ExecutionException e) {
		}
        
		return null;
	}
}
