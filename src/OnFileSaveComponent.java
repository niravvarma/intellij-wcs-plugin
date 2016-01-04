import com.fatwire.wem.sso.SSOException;
import com.intellij.AppTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import csdt.CSDPUtil;
import csdt.Preferences;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by NB20308 on 23/12/2015.
 */
public class OnFileSaveComponent implements ApplicationComponent {

    String workspace;
    @Override
    public void initComponent() {

        MessageBus bus = ApplicationManager.getApplication().getMessageBus();

        MessageBusConnection connection = bus.connect();

        Preferences.setContextPath("ContentServer");
        Preferences.setUsername("fwadmin");
        Preferences.setPassword("1fwadmin2");
        //Preferences.setWorkspace("/c/Projects/EOP/eop/workspace/envision/cs_workspace");

        workspace="C:/Projects/EOP/eop/workspace/envision/cs_workspace";
        Preferences.setWorkspace(workspace);
        Preferences.setHost("http://eop.loc");
        Preferences.setPort("8080");
        Preferences.setDataStoreName("cs_workspace");

        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC,
                new FileDocumentManagerAdapter() {
                    @Override
                    public void beforeAllDocumentsSaving(){
                        System.out.println("beforeAllDocumentsSaving: ");
                    }

                    @Override
                    public void beforeDocumentSaving(Document document) {
                        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
                        VirtualFile virtualFile = fileDocumentManager.getFile(document);

                        if("JSP".equals(virtualFile.getFileType().getName())){

                            System.out.println("*** WCS: start sync ***");
                            try {


                                String[] filename = virtualFile.getPath().split(workspace+"/");
                                System.out.println("*** FILNAME: "+filename[1]+" ***");
                                CSDPUtil.callImport(filename[1], false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SSOException e) {
                                e.printStackTrace();
                            }

                            System.out.println("beforeDocumentSaving: "+document.getClass().getSimpleName());
                            System.out.println("*** WCS: end sync ***");
                        }

                    }

                    @Override
                    public void fileContentLoaded(@NotNull VirtualFile file, @NotNull Document document) {
                        System.out.println("fileContentLoaded: "+document.getClass().getName());
                        System.out.println("fileContentLoaded: "+file.getName());
                        System.out.println("fileContentLoaded: "+file.getFileSystem());
                        System.out.println("fileContentLoaded: "+file.getFileType());
                    }


                    @Override
                    public void fileContentReloaded(VirtualFile file,Document document) {

                        System.out.println("fileContentReloaded: "+document.getClass().getName());
                        System.out.println("fileContentReloaded: "+file.getName());
                        System.out.println("fileContentReloaded: "+file.getFileSystem());
                        System.out.println("fileContentReloaded: "+file.getFileType());
                    }
                });
    }


    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "WCS onSave Component interceptor";
    }
}
