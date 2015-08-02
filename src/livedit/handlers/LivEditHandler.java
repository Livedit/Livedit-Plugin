// Created By Woong.

package livedit.handlers;

import java.io.File;

import javax.swing.text.PlainView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.webbitserver.handler.StaticFileHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.eclipse.core.resources.ResourcesPlugin;

public class LivEditHandler extends AbstractHandler{
	protected boolean WBind = false;
	
	WebServer webServer = null;
	
	public LivEditHandler() {
	}

	public Object execute(ExecutionEvent event)  {
		try {
			if(!WBind){
				/*String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
				String check = Display.getCurrent().getActiveShell().toString();
				String extension = check.replace("Shell {Resource - ", "").replace(" - Eclipse Platform}", "");
				
				System.out.println(path+"/"+extension);
				
				File fPath = new File(path+"/"+extension);*/
				
				webServer = WebServers.createWebServer(63312)
	        			.add("/", new Websocket())
	        			//.add("/"+extension, new HttpHandlerImpl())
	                    .start()
	                    .get();
	        	WBind = true;
	        	System.out.println("Create WebSocket Server");
			} else{
				WBind = false;
				webServer.stop().get();
				System.out.println("Close WebSocket Server");
			}
		} catch (InterruptedException  ie) {
			System.out.println("InterruptedException");
		} catch (java.util.concurrent.ExecutionException ee){
			System.out.println("ExecutionException");
		}
        return null;
	}
}
