package TestLanguage.plugin;

/*Generated by MPS */

import jetbrains.mps.logging.Logger;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.Map;
import java.awt.Frame;
import javax.swing.JDialog;
import java.util.HashSet;
import org.jetbrains.mps.openapi.model.SModel;
import java.awt.Dialog;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import jetbrains.mps.internal.collections.runtime.MapSequence;
import com.sun.istack.internal.NotNull;
import com.intellij.openapi.progress.ProgressIndicator;
import jetbrains.mps.progress.ProgressMonitorAdapter;

/**
 * Helper class for displaying Preparation dialog and Class Generation progress bar
 * Reason for the existence of Preparation dialog only in illustrating that we can
 * show our own GUI, not only standard one, and process background tasks with our own methods.
 * Unfortunately, it seems that usage of SwingWorker directly leads to manual checking of concurrency.\n
 */
public class PreparationDialog {
  public static Logger LOG = Logger.getLogger(PreparationDialog.class);
  public static final String PREPARATION_MESSAGE = "Preparing files...";
  public static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error:";
  public static final String GENERATING_CLASSES_DIALOG_CAPTION = "Generating Classes";
  public static final String EXECUTE_METHOD_FAILED_MESSAGE = "User's action execute method failed. Action:TestAction";
  public static final int PREPARATION_DIALOG_WIDTH = 340;
  public static final int PREPARATION_DIALOG_HEIGHT = 200;
  public static final int PREPARATION_MESSAGE_WIDTH = 50;
  public static final int PREPARATION_MESSAGE_HEIGHT = 100;
  public static final int PREPARATION_SLEEP_INTERVAL = 2000;


  /**
   * Show preparation window, then show class generation progress bar, trigger processing of models
   * 
   * @param event current action event
   * @param _params previously create parameter map
   * @param mpsBaseFrame root frame in hierarchy of modal windows
   * @param ownerDialog parent dialog in hierarchy of modal windows
   * @param approvedModels models that should be processed
   * @param actionListener listener that will be triggred simultaneously with opening of progress bar window
   */
  public static void execute(final AnActionEvent event, final Map<String, Object> _params, final Frame mpsBaseFrame, JDialog ownerDialog, final HashSet<SModel> approvedModels, final ModelsSelectionActionListener actionListener) {

    // Create borderless modal Preparation dialog centered on the screen 
    final JDialog dialog = new JDialog(ownerDialog, Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setTitle("");
    dialog.setUndecorated(true);
    dialog.setSize(PreparationDialog.PREPARATION_DIALOG_WIDTH, PreparationDialog.PREPARATION_DIALOG_HEIGHT);
    ScreenHelper.centerOnScreen(dialog, true);
    dialog.setModal(true);

    // Add message about preparing classes to this dialog 
    final JLabel jlabel = new JLabel(PreparationDialog.PREPARATION_MESSAGE);
    jlabel.setPreferredSize(new Dimension(PreparationDialog.PREPARATION_MESSAGE_WIDTH, PreparationDialog.PREPARATION_MESSAGE_HEIGHT));
    jlabel.setHorizontalAlignment(SwingConstants.CENTER);
    dialog.add(jlabel, BorderLayout.CENTER);

    // Run sample task (for demonstation purposes it is just sleeping) in a separate thread using SwingWorker  
    (new SwingWorker() {
      protected Object doInBackground() throws Exception {
        try {
          Thread.sleep(PreparationDialog.PREPARATION_SLEEP_INTERVAL);
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, PreparationDialog.UNEXPECTED_ERROR_MESSAGE + e.toString());
        }
        return null;
      }

      @Override
      protected void done() {
        try {
          // Blocking wait 
          super.get();
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, PreparationDialog.UNEXPECTED_ERROR_MESSAGE + e.toString());
        } finally {
          // Close Preparation dialog 
          dialog.setVisible(false);

          try {
            // Run standard dialog (this code is copy-pasted from ReloadAll_Action in MPS (Tools->Reload All Classes) 
            ProgressManager.getInstance().run(new Task.Modal((Project) (MapSequence.fromMap(_params).get("project")), PreparationDialog.GENERATING_CLASSES_DIALOG_CAPTION, false) {
              public void run(@NotNull final ProgressIndicator indicator) {

                // Finally, we can process models 
                actionListener.actionPerformed(event, _params, mpsBaseFrame, dialog, new ProgressMonitorAdapter(indicator), approvedModels);
              }
            });
          } catch (Throwable t) {
            LOG.error(EXECUTE_METHOD_FAILED_MESSAGE, t);
          }
        }
      }
    }).execute();

    dialog.setVisible(true);

  }


}
