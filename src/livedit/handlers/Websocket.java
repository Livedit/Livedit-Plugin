// Created By Woong.

package livedit.handlers;

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
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Websocket extends BaseWebSocketHandler {
	
	String check = null;
	
	public void onOpen(WebSocketConnection connection) {
		System.out.println("connected");
	
		Display.getDefault().asyncExec(new Runnable(){
				@Override
	            public void run() {
								
					//Display.getCurrent().addFilter(SWT.MouseDown , new Listener(){		//마우스는 lineNumber가 늦음
					Display.getCurrent().addFilter(SWT.FOCUSED, new Listener(){
					
						@Override
						public void handleEvent(Event event) {
							
							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
							IWorkbenchPage page = win.getActivePage();
							IEditorPart editor = page.getActiveEditor();
							ITextEditor tEditor = (ITextEditor)editor;
							IDocumentProvider dp = tEditor.getDocumentProvider();
							IDocument du = dp.getDocument(tEditor.getEditorInput());
							
							//System.out.println(du.get());				//editor 내용 전체 긁어오기
							//System.out.println(" " + tEditor.getTitle());	//파일명+확장자
							
							try{
								
								check = Display.getCurrent().getActiveShell().toString();
								System.out.println("check : " + check);
								
								if( check.contains(".html") ) {
										
									ITextSelection textSelection = (ITextSelection) editor
									        .getSite().getSelectionProvider().getSelection();
									int offset = textSelection.getOffset();
									int lineNumber = du.getLineOfOffset(offset);
									
									int tempLine = textSelection.getStartLine();
									int column = 0;
									
									column = textSelection.getOffset() - du.getLineOffset(tempLine);
									
									System.out.println("Column : " + column);
									
									int length = 0;
									
									for(int i = 0; i <= lineNumber; i++){
										length += du.getLineLength(i);
									}
									
									String ahtml = du.get(0, length);
									//ahtml = ahtml.replaceAll("(\t|\r\n|\n)", "");
									Document doc = Jsoup.parse(ahtml);
									
									String lineDoc = du.get(length-du.getLineLength(lineNumber), du.getLineLength(lineNumber));
									
									lineDoc = lineDoc.toLowerCase();
									lineDoc = lineDoc.replaceAll("(\t|\r\n|\n)", "");
									
									Elements links = null;
									
									int j = 0;
									
									String tagName = null;
									
									System.out.println("lineDoc : " + lineDoc);
									
									for(; j < lineDoc.length(); j++){
										System.out.println("lineDoc : " + lineDoc.charAt(j));
										if(lineDoc.charAt(j) == '>' || lineDoc.charAt(j) == ' '){
											break;
										}
									}					
									
									System.out.println("lineDoc j : " + j);
									
									tagName = lineDoc.substring(1, j);
									
									if(tagName.contains("/")){
										tagName = tagName.replace("/","");
									}
								
									System.out.println("tagName : " + tagName);
									
									links = doc.select(tagName);
									
									System.out.println(links.size());
									
									Element linkTemp = links.get(links.size() - 1);
									
									System.out.println("selector1 : " + linkTemp.cssSelector());
									
									onMessage(connection,	"{ \"command\" : \"inspect\", \"nodeSelector\" : \"" + linkTemp.cssSelector() + "\"}");
									
									System.out.println("lineNumber : " + (lineNumber+1));
									
									System.out.println("line data : " + du.get(length-du.getLineLength(lineNumber), du.getLineLength(lineNumber)));
									
									System.out.println("----------------------------------------");
								}
								
							} catch(Exception e){
								e.printStackTrace();
							}
						}
					});
					
					Display.getCurrent().addFilter(SWT.FocusOut, new Listener(){
						@Override
						public void handleEvent(Event event) {
							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
							IWorkbenchPage page = win.getActivePage();
							IEditorPart editor = page.getActiveEditor();
							ITextEditor tEditor = (ITextEditor)editor;
							IDocumentProvider dp = tEditor.getDocumentProvider();
							IDocument du = dp.getDocument(tEditor.getEditorInput());
							
							page.saveEditor(editor, false);
							
							try {
							
								if ( check.contains(".html") ){
									
									String ahtml = du.get(0, du.getLength());
									ahtml = ahtml.replaceAll("(\t|\r\n|\n)", "");
									Document doc = Jsoup.parse(ahtml);
									
									
									
									String html = du.get().replaceAll("(\t|\r\n|\n)", "");
									html = html.replaceAll("\"", "'");
									
									System.out.println("why?");
									onMessage(connection,	"{ \"command\" : \"injectJavascript\"}");
									
								} else if ( check.contains(".js")){
	
									String js = du.get().replaceAll("(\t|\r\n|\n)", "");
									js = js.replaceAll("\"", "'");
																	
									onMessage(connection,	"{ \"command\" : \"injectJavascript\"}");
									
								} else if (check.contains(".css")){
									
									String css = du.get().replaceAll("(\t|\r\n|\n)", "");
									css = css.replaceAll("\"", "'");
									
									onMessage(connection,	"{ \"command\" : \"injectCss\", \"code\" : \"" + css + "\"}");
									
								}
							} catch (Exception e){
								e.printStackTrace();
							}
						}
					});
					
					Display.getCurrent().addFilter(SWT.KeyDown, new Listener(){
						@Override
						public void handleEvent(Event event) {
							String extension = null;
							
							
							check = Display.getCurrent().getActiveShell().toString();
							extension = check.replace("Shell {Resource - ", "").replace(" - Eclipse Platform}", "");
							
							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
							IWorkbenchPage page = win.getActivePage();
							IEditorPart editor = page.getActiveEditor();
							ITextEditor tEditor = (ITextEditor)editor;
							IDocumentProvider dp = tEditor.getDocumentProvider();
							IDocument du = dp.getDocument(tEditor.getEditorInput());
							
							try {
															
								/*String msg = Character.toString(event.character);
								onMessage(connection, msg);*/
								
								//System.out.println(event.keyCode);
								
								switch(event.keyCode){
									case 16777219:  case 16777220:	//case 16777217:	case 16777218:								//방향키
									case 65536:																						//alt
									//case 262144:																					//ctrl
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
										
										if ( check.contains(".html") ){
											
											String ahtml = du.get(0, du.getLength());
											ahtml = ahtml.replaceAll("(\t|\r\n|\n)", "");
											Document doc = Jsoup.parse(ahtml);
											
											String html = du.get().replaceAll("(\t|\r\n|\n)", "");
											html = html.replaceAll("\"", "'");
																						
											onMessage(connection,	"{ \"command\" : \"insert\", \"nodeSelector\" : \"html\", \"code\" : \"" + html + "\"}");
											//onMessage(connection,	"{ \"command\" : \"injectJavascript\", \"code\" : \"" + script + "\"}");
											
										} 
									}
								}
								
								
							} catch (Exception e) {
								e.printStackTrace();
							} 
							
						}	
					});
				
				}
			});
		
    }

    public void onClose(WebSocketConnection connection) {
    	connection.close();
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
    
	/*public void save(final IEditorPart editor){
		
		Display.getCurrent().asyncExec(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ProgressMonitorPart pmp = (ProgressMonitorPart)Display.getCurrent().getMonitors();
				System.out.println(Display.getCurrent().getMonitors());
				//editor.doSaveAs();
				editor.doSave(progressMonitor);
			}
			
		});
	}*/
}