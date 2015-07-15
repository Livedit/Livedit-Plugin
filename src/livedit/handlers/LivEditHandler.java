package livedit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

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
