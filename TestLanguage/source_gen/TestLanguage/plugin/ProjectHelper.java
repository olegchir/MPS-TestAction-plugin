package TestLanguage.plugin;

/*Generated by MPS */

import com.intellij.openapi.actionSystem.AnActionEvent;
import java.util.Map;
import jetbrains.mps.smodel.ModelAccess;
import jetbrains.mps.internal.collections.runtime.MapSequence;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import jetbrains.mps.ide.actions.MPSCommonDataKeys;
import jetbrains.mps.workbench.MPSDataKeys;
import jetbrains.mps.project.MPSProject;

/**
 * Helper for retrieving information about projects and actions
 */
public class ProjectHelper {
  public static final String ACTION_PROPERTY_PROJECT = "project";
  public static final String ACTION_PROPERTY_FRAME = "frame";
  public static final String ACTION_PROPERTY_CONTEXT = "context";
  public static final String ACTION_PROPERTY_MODELS = "models";
  public static final String ACTION_PROPERTY_MODULES = "modules";


  public static void collectActionData(final AnActionEvent event, final Map<String, Object> _params) {
    ModelAccess.instance().runReadAction(new Runnable() {
      public void run() {
        collectActionDataImpl(event, _params);
      }
    });
  }

  /**
   * Put common action data to a string-object map
   * (Code of this method was copy-pasted from RunMigrationSripts_Action and BaseAction)
   * 
   * @param event AnActionEvent
   * @param _params map of params that already exist
   * @return true
   */
  public static boolean collectActionDataImpl(final AnActionEvent event, final Map<String, Object> _params) {
    MapSequence.fromMap(_params).put(ProjectHelper.ACTION_PROPERTY_PROJECT, event.getData(PlatformDataKeys.PROJECT));
    if (MapSequence.fromMap(_params).get(ProjectHelper.ACTION_PROPERTY_PROJECT) == null) {
      return false;
    }
    MapSequence.fromMap(_params).put(ProjectHelper.ACTION_PROPERTY_FRAME, event.getData(MPSCommonDataKeys.FRAME));
    if (MapSequence.fromMap(_params).get(ProjectHelper.ACTION_PROPERTY_FRAME) == null) {
      return false;
    }
    MapSequence.fromMap(_params).put(ProjectHelper.ACTION_PROPERTY_CONTEXT, event.getData(MPSCommonDataKeys.OPERATION_CONTEXT));
    if (MapSequence.fromMap(_params).get(ProjectHelper.ACTION_PROPERTY_CONTEXT) == null) {
      return false;
    }
    MapSequence.fromMap(_params).put(ProjectHelper.ACTION_PROPERTY_MODELS, event.getData(MPSCommonDataKeys.MODELS));
    MapSequence.fromMap(_params).put(ProjectHelper.ACTION_PROPERTY_MODULES, event.getData(MPSDataKeys.MODULES));
    return true;
  }

  /**
   * Get current project as MPSProject.
   * (Idea of where to get project id from here:
   * http://tomaszdziurko.pl/2011/09/developing-plugin-intellij-idea-some-tips-and-links/)
   * 
   * @param event current action event
   * @return current project as MPSProject
   */
  public static MPSProject getCurrentProject(final AnActionEvent event) {
    return MPSCommonDataKeys.MPS_PROJECT.getData(event.getDataContext());
  }


}
