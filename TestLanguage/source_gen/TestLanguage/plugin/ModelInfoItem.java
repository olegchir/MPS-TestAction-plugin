package TestLanguage.plugin;

/*Generated by MPS */

import org.jetbrains.mps.openapi.model.SModel;
import java.util.HashSet;
import java.util.ArrayList;
import jetbrains.mps.internal.collections.runtime.SetSequence;
import javax.swing.JList;

/**
 * Class entirely dedicated to create fancy-looking toString, and put to / get from JList as a sorted list
 */
public class ModelInfoItem implements Comparable<ModelInfoItem> {
  private SModel model;


  public ModelInfoItem(SModel model) {
    this.model = model;
  }




  @Override
  public String toString() {
    return model.getModelName().toString() + " (" + model.getModule().getModuleName().toString() + ")";
  }

  public int compare(ModelInfoItem model1, ModelInfoItem model2) {
    return model1.getModel().getModelName().compareTo(model2.getModel().getModelName());
  }

  public int compareTo(ModelInfoItem model) {
    return this.getModel().getModelName().toString().compareTo(getModel().getModelName().toString());
  }

  public SModel getModel() {
    return this.model;
  }




  public static ModelInfoItem[] arrayFromSModels(HashSet<SModel> models) {
    ArrayList<ModelInfoItem> imodels = new ArrayList<ModelInfoItem>();
    for (SModel model : SetSequence.fromSet(models)) {
      imodels.add(new ModelInfoItem(model));
    }
    return imodels.toArray(new ModelInfoItem[imodels.size()]);
  }

  public static HashSet<SModel> modelsFromArray(ModelInfoItem[] imodels) {
    HashSet<SModel> models = new HashSet<SModel>();
    for (ModelInfoItem imodel : imodels) {
      models.add(imodel.getModel());
    }
    return models;
  }

  public static ModelInfoItem[] arrayFromJList(JList jlist) {
    ArrayList<ModelInfoItem> imodels = new ArrayList<ModelInfoItem>();
    int[] selected = jlist.getSelectedIndices();
    for (int i = 0; i < selected.length; i++) {
      ModelInfoItem imodel = (ModelInfoItem) (jlist.getModel().getElementAt(selected[i]));
      imodels.add(imodel);
    }
    return imodels.toArray(new ModelInfoItem[imodels.size()]);
  }

  public static HashSet<SModel> modelsFromJList(JList jlist) {
    ModelInfoItem[] asArray = arrayFromJList(jlist);
    return modelsFromArray(asArray);
  }


}
