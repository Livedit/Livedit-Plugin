package livedit.handlers;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
				
				ITextSelection textSelection = (ITextSelection) editor
				        .getSite().getSelectionProvider().getSelection();
				int offset = textSelection.getOffset();
				
				//System.out.println(du.get());				//editor 내용 전체 긁어오기
				Display.getCurrent().addFilter(SWT.MouseDown, new Listener(){
				//Display.getCurrent().addFilter(SWT.FOCUSED, new Listener(){
				
					@Override
					public void handleEvent(Event event) {
						try{
							int lineNumber = du.getLineOfOffset(offset);
							lineNumber = du.getLineOfOffset(offset);
							
							
							System.out.println(du.getNumberOfLines(1, 15));
							//System.out.println(du.getLineInformation(offset));
							//System.out.println(lineNumber);
						}catch(Exception e){
							
						}
					}
				});
								
				Display.getCurrent().addFilter(SWT.KeyDown, new Listener(){
					@Override
					public void handleEvent(Event event) {
						try {
							/*String msg = Character.toString(event.character);
							onMessage(connection, msg);*/
							
							//System.out.println(event.keyCode);
							
							switch(event.keyCode){
								case 16777217:	case 16777218:	case 16777219:  case 16777220:									//방향키
								case 65536:																						//alt
								case 262144:																					//ctrl
								case 131072:																					//shift
								case 9:																							//tab
								case 16777298:																					//capslock
								case 27:																						//esc
								case 16777226:	case 16777227:	case 16777228:	case 16777229:	case 16777230:	case 16777231:	//f1~f6
								case 16777232:	case 16777233:	case 16777234:	case 16777235:	case 16777236:	case 16777237:	//f7~f12
								case 16777300:																					//scroll lock
								case 16777301:																					//pause break
								case 16777221:			 																		//page up
								case 16777222:																					//page down
									break;
								default: {
									
									
									String html = du.get(0, du.getLength());
									html = html.replaceAll("(\t|\r\n|\n)", "");
									Document doc = Jsoup.parse(html);
									System.out.println(doc.toString());
									/*String html = du.get().replaceAll("(\t|\r\n|\n)", "");
									html = html.replaceAll("\"", "'");
									onMessage(connection, "{ \"command\" : \"insert\", \"nodeSelector\" : \"html\", \"code\" : \"" + html + "\"}");*/								
								}
							}
							
							
						} catch (Exception e) {
							
						} 
						
					}	
				});
			
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