package com.gt.bmf.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class GfQueryLog implements Serializable {

	private static final long serialVersionUID = -1686320397097241613L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)

	private Long id;
    private Double upPrice;
    private Double downPrice;
    private Double upVariance;
    private Double downVariance;
    private Date logTime;
    private Integer quality;
    private String type;

    public Double getUpVariance() {
        return upVariance;
    }

    public void setUpVariance(Double upVariance) {
        this.upVariance = upVariance;
    }

    public Double getDownVariance() {
        return downVariance;
    }

    public void setDownVariance(Double downVariance) {
        this.downVariance = downVariance;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(Double upPrice) {
        this.upPrice = upPrice;
    }

    public Double getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(Double downPrice) {
        this.downPrice = downPrice;
    }



    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
