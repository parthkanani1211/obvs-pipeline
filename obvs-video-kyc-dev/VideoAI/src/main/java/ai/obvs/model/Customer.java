package ai.obvs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer extends User {

    private String address;

//    @ManyToMany
//    @JoinTable(name = "customer_services",
//    joinColumns = @JoinColumn(name = "customer_id"),
//    inverseJoinColumns = @JoinColumn(name = "service_id"))
//    private Set<App> apps;

    @OneToMany(mappedBy="customer")
    @JsonBackReference
    private Set<VideoKYC> videoKYCRequests;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<VideoKYC> getVideoKYCRequests() {
        return videoKYCRequests;
    }

    public void setVideoKYCRequests(Set<VideoKYC> videoKYCRequests) {
        this.videoKYCRequests = videoKYCRequests;
    }
//    public Set<App> getApps() {
//        return apps;
//    }
//
//    public void setApps(Set<App> apps) {
//        this.apps = apps;
//    }
}
