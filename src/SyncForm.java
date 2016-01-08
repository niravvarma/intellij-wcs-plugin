import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;

/**
 * Created by NB20308 on 04/01/2016.
 */
public class SyncForm extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        //TextConsoleBuidlerFactory.getInstance().createBuilder(project).getConsole()
        Project project = event.getData(PlatformDataKeys.PROJECT);
        JFrame frame = WindowManager.getInstance().getFrame(project);
        final SyncWindowForm syncWindowForm = new SyncWindowForm();
        syncWindowForm.display(frame);
        //JFrame form= new JFrame("Synchronize Data");
        //form.setIconImage();
        //frame.setContentPane(new SyncWindowForm().getMainPanel());

        //frame.setSize(1000,700);
        //frame.setMinimumSize(new Dimension(1000,700));
        //frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
        // TODO: insert action logic here
    }

    @Override
    public void update(AnActionEvent e) {
        if (PropertiesComponent.getInstance().getBoolean("wcs-plugin-active")){
            Object navigatable = e.getData(CommonDataKeys.NAVIGATABLE);
            e.getPresentation().setEnabledAndVisible(navigatable != null);
        }else{
            return;
        }
    }
}
