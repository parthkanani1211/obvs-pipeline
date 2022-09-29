package ai.obvs.services.CKYC.model;

import ai.obvs.dto.CKYC.AddressFields;
import ai.obvs.dto.CKYC.NameFields;

import java.util.List;

public class RequestData {
    private NameFields cust_nm;
    private NameFields father_nm;
    private NameFields mother_nm;
    private String cus_nm_as_per_uidi;
    private String cust_photo;
    private String cust_sign;
    private String cust_selfie;
    private String date_of_birth;
    private String gender;
    private String marital_status;
    private String nationality;
    private String occupation;
    private String community;
    private String pan_no;
    private String uidi;
    private String residency_status;
    private String constitution;
    private AddressFields permanent_add;
    private String same_as_permanent_add;
    private AddressFields local_add;
    private String prof_of_address_doc_img;
    private String mkr_user_id;
    private String mkr_entered_dt;
    private String ckr_user_id;
    private String ckr_verify_dt;
    private String status;
    private String reject_reason;
    private List<DocDetail> doc_dtl;

//    private NameFields CUST_NM;
//    private NameFields FATHER_NM;
//    private NameFields MOTHER_NM;
//    private String CUS_NM_AS_PER_UIDI;
//    private String CUST_PHOTO;
//    private String CUST_SIGN;
//    private String CUST_SELFIE;
//    private String DATE_OF_BIRTH;
//    private String GENDER;
//    private String MARITAL_STATUS;
//    private String NATIONALITY;
//    private String OCCUPATION;
//    private String COMMUNITY;
//    private String PAN_NO;
//    private String UIDI;
//    private String RESIDENCY_STATUS;
//    private String CONSTITUTION;
//    private AddressFields PERMANENT_ADD;
//    private String SAME_AS_PERMANENT_ADD;
//    private AddressFields LOCAL_ADD;
//    private String PROF_OF_ADDRESS_DOC_IMG;
//    private String MKR_USER_ID;
//    private String MKR_ENTERED_DT;
//    private String CKR_USER_ID;
//    private String CKR_VERIFY_DT;
//    private String STATUS;
//    private String REJECT_REASON;
//    private List<DocDetail> DOC_DTL;


    public NameFields getCust_nm() {
        return cust_nm;
    }

    public void setCust_nm(NameFields cust_nm) {
        this.cust_nm = cust_nm;
    }

    public NameFields getFather_nm() {
        return father_nm;
    }

    public void setFather_nm(NameFields father_nm) {
        this.father_nm = father_nm;
    }

    public NameFields getMother_nm() {
        return mother_nm;
    }

    public void setMother_nm(NameFields mother_nm) {
        this.mother_nm = mother_nm;
    }

    public String getCus_nm_as_per_uidi() {
        return cus_nm_as_per_uidi;
    }

    public void setCus_nm_as_per_uidi(String cus_nm_as_per_uidi) {
        this.cus_nm_as_per_uidi = cus_nm_as_per_uidi;
    }

    public String getCust_photo() {
        return cust_photo;
    }

    public void setCust_photo(String cust_photo) {
        this.cust_photo = cust_photo;
    }

    public String getCust_sign() {
        return cust_sign;
    }

    public void setCust_sign(String cust_sign) {
        this.cust_sign = cust_sign;
    }

    public String getCust_selfie() {
        return cust_selfie;
    }

    public void setCust_selfie(String cust_selfie) {
        this.cust_selfie = cust_selfie;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getPan_no() {
        return pan_no;
    }

    public void setPan_no(String pan_no) {
        this.pan_no = pan_no;
    }

    public String getUidi() {
        return uidi;
    }

    public void setUidi(String uidi) {
        this.uidi = uidi;
    }

    public String getResidency_status() {
        return residency_status;
    }

    public void setResidency_status(String residency_status) {
        this.residency_status = residency_status;
    }

    public String getConstitution() {
        return constitution;
    }

    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public AddressFields getPermanent_add() {
        return permanent_add;
    }

    public void setPermanent_add(AddressFields permanent_add) {
        this.permanent_add = permanent_add;
    }

    public String getSame_as_permanent_add() {
        return same_as_permanent_add;
    }

    public void setSame_as_permanent_add(String same_as_permanent_add) {
        this.same_as_permanent_add = same_as_permanent_add;
    }

    public AddressFields getLocal_add() {
        return local_add;
    }

    public void setLocal_add(AddressFields local_add) {
        this.local_add = local_add;
    }

    public String getProf_of_address_doc_img() {
        return prof_of_address_doc_img;
    }

    public void setProf_of_address_doc_img(String prof_of_address_doc_img) {
        this.prof_of_address_doc_img = prof_of_address_doc_img;
    }

    public String getMkr_user_id() {
        return mkr_user_id;
    }

    public void setMkr_user_id(String mkr_user_id) {
        this.mkr_user_id = mkr_user_id;
    }

    public String getMkr_entered_dt() {
        return mkr_entered_dt;
    }

    public void setMkr_entered_dt(String mkr_entered_dt) {
        this.mkr_entered_dt = mkr_entered_dt;
    }

    public String getCkr_user_id() {
        return ckr_user_id;
    }

    public void setCkr_user_id(String ckr_user_id) {
        this.ckr_user_id = ckr_user_id;
    }

    public String getCkr_verify_dt() {
        return ckr_verify_dt;
    }

    public void setCkr_verify_dt(String ckr_verify_dt) {
        this.ckr_verify_dt = ckr_verify_dt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public List<DocDetail> getDoc_dtl() {
        return doc_dtl;
    }

    public void setDoc_dtl(List<DocDetail> doc_dtl) {
        this.doc_dtl = doc_dtl;
    }
}
