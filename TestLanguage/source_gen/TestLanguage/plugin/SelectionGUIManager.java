package TestLanguage.plugin;

/*Generated by MPS */

import jetbrains.mps.logging.Logger;
import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.HashSet;
import org.jetbrains.mps.openapi.model.SModel;
import gnu.trove.THashMap;
import java.util.Map;
import java.awt.Frame;
import javax.swing.JDialog;
import jetbrains.mps.progress.ProgressMonitorAdapter;
import jetbrains.mps.internal.collections.runtime.SetSequence;
import javax.swing.JOptionPane;

/**
 * Helper class for displaying GUI
 * Reason for the existence of this class only that spaghetti code
 * in the listeners looks too ugly and makes more difficult to read a more useful sources.
 * You can re-write it without listeners if you like :-)
 */
public class SelectionGUIManager {
  private static Logger LOG = Logger.getLogger(SelectionGUIManager.class);
  public static final String PROGRESS_BAR_CAPTION = "Generating Classes";
  public static final String PROGRESS_BAR_STEP_REFIX = "Generating class for ";
  public static final int INTENDED_MODEL_PROCESSING_INTERVAL = 2000;
  public static final int INTENDED_SLEEP_AFTER_PROCESSING_INTERVAL = 1000;
  public static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected error:";


  public static void showSelectionGUI(AnActionEvent event, HashSet<SModel> models) {
    final THashMap<String, Object> params = new THashMap<String, Object>();
    ProjectHelper.collectActionData(event, params);

    // Two subsequent action listeners. 
    // First fired by pressing button on Select Models dialog, and second fired immediately after it 
    ModelSelectionDialog.show(event, params, models, new ModelsSelectionActionListener() {
      public void actionPerformed(final AnActionEvent event, final Map<String, Object> _params, Frame mpsBaseFrame, JDialog ownerDialog, ProgressMonitorAdapter monitor, HashSet<SModel> approvedModels) {
        PreparationDialog.execute(event, _params, mpsBaseFrame, ownerDialog, approvedModels, new ModelsSelectionActionListener() {
          public void actionPerformed(final AnActionEvent event, final Map<String, Object> _params, Frame mpsBaseFrame, JDialog ownerDialog, ProgressMonitorAdapter monitor, HashSet<SModel> approvedModels) {

            // We passed ProgressMonitorAdapter through all this listeners,  
            // so it easy to track progress with start/advance/done 
            monitor.start(SelectionGUIManager.PROGRESS_BAR_CAPTION, approvedModels.size());
            for (SModel model : SetSequence.fromSet(approvedModels)) {
              String stepDescription = SelectionGUIManager.PROGRESS_BAR_STEP_REFIX + model.getModelName().toString();
              monitor.step(stepDescription);
              TestClassGenerator.generateTestClass(event, model);
              try {
                Thread.sleep(SelectionGUIManager.INTENDED_MODEL_PROCESSING_INTERVAL);
              } catch (Exception e) {
                JOptionPane.showMessageDialog(null, SelectionGUIManager.UNEXPECTED_ERROR_MESSAGE + e.toString());
                monitor.cancel();
              }
              monitor.advance(1);
            }

            // Little pause just after finishing generation process 
            // Especially for the user to be in time to see a dialogue with 100% of the work performed 
            try {
              Thread.sleep(SelectionGUIManager.INTENDED_SLEEP_AFTER_PROCESSING_INTERVAL);
            } catch (Exception e) {
            } finally {
              monitor.done();
            }
          }
        });
      }
    });
  }


}
