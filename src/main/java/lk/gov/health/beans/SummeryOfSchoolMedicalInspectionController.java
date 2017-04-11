package lk.gov.health.beans;

import lk.gov.health.schoolhealth.SummeryOfSchoolMedicalInspection;
import lk.gov.health.beans.util.JsfUtil;
import lk.gov.health.beans.util.JsfUtil.PersistAction;
import lk.gov.health.faces.SummeryOfSchoolMedicalInspectionFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@SessionScoped
public class SummeryOfSchoolMedicalInspectionController implements Serializable {

    @EJB
    private lk.gov.health.faces.SummeryOfSchoolMedicalInspectionFacade ejbFacade;
    private List<SummeryOfSchoolMedicalInspection> items = null;
    private SummeryOfSchoolMedicalInspection selected;

    public String toAddSummery() {
        selected = new SummeryOfSchoolMedicalInspection();
        return "/summeryOfSchoolMedicalInspection/add";
    }

    public String saveSummery() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return "";
        }
        if (selected.getId() == null) {
            getFacade().create(selected);
            JsfUtil.addSuccessMessage("Saved");
            return "";
        } else {
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Updated");
            return "";
        }
    }

    public SummeryOfSchoolMedicalInspectionController() {
    }

    public SummeryOfSchoolMedicalInspection getSelected() {
        return selected;
    }

    public void setSelected(SummeryOfSchoolMedicalInspection selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SummeryOfSchoolMedicalInspectionFacade getFacade() {
        return ejbFacade;
    }

    public SummeryOfSchoolMedicalInspection prepareCreate() {
        selected = new SummeryOfSchoolMedicalInspection();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle2").getString("SummeryOfSchoolMedicalInspectionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle2").getString("SummeryOfSchoolMedicalInspectionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle2").getString("SummeryOfSchoolMedicalInspectionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<SummeryOfSchoolMedicalInspection> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<SummeryOfSchoolMedicalInspection> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SummeryOfSchoolMedicalInspection> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void calTotalNoOfChildren() {
        if (selected == null) {
            return;
        }
        selected.setTotalNoOfChildrenMale(
                selected.getTotalNoOfChildren1Male()
                + selected.getTotalNoOfChildren4Male()
                + selected.getTotalNoOfChildren7Male()
                + selected.getTotalNoOfChildren10Male()
                + selected.getTotalNoOfChildrenOtherMale()
        );
        selected.setTotalNoOfChildrenFemale(
                selected.getTotalNoOfChildren1Female()
                + selected.getTotalNoOfChildren4Female()
                + selected.getTotalNoOfChildren7Female()
                + selected.getTotalNoOfChildren10Female()
                + selected.getTotalNoOfChildrenOtherFemale()
        );
        int imp;
        int ifp;
        int tm = selected.getTotalNoOfChildrenMale();
        int tf = selected.getTotalNoOfChildrenFemale();
        if (tm + tf == 0) {
            imp=0;
            ifp=0;
        } else {
            imp = tm * 100 / (tm + tf);
            ifp = tf * 100 / (tm + tf);
        }
        selected.setTotalNoOfChildren1FemalePercentage(ifp);
        selected.setTotalNoOfChildrenMalePercentage(imp);
    }

    @FacesConverter(forClass = SummeryOfSchoolMedicalInspection.class)
    public static class SummeryOfSchoolMedicalInspectionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SummeryOfSchoolMedicalInspectionController controller = (SummeryOfSchoolMedicalInspectionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "summeryOfSchoolMedicalInspectionController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SummeryOfSchoolMedicalInspection) {
                SummeryOfSchoolMedicalInspection o = (SummeryOfSchoolMedicalInspection) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SummeryOfSchoolMedicalInspection.class.getName()});
                return null;
            }
        }

    }

}