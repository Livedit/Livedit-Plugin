package livedit.handlers;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class Websocket extends BaseWebSocketHandler {
	
	public void onOpen(WebSocketConnection connection) {
		Display.getDefault().asyncExec(new Runnable(){
			@Override
            public void run() {
				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				IWorkbenchPage page = win.getActivePage();
				IEditorPart editor = page.getActiveEditor();
				ITextEditor tEditor = (ITextEditor)editor;
				IDocumentProvider dp = tEditor.getDocumentProvider();
				IDocument du = dp.getDocument(tEditor.getEditorInput());
				
				System.out.println(du.get());				//editor 내용 전체 긁어오기
			}
		});
		
		System.out.println("connected");
		
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("closed");
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) throws Exception {
    	connection.send(msg);
    }

    @Override
    public void onMessage(WebSocketConnection connection, byte[] msg) {
    	connection.send(msg);
    }
    
}