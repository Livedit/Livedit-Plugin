package livedit.handlers;

import java.net.UnknownHostException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class LivEditHandler extends AbstractHandler {

	public LivEditHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Websocket.init();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
}
