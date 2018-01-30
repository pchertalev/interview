package com.interview.esphere.domain.model;

import com.interview.esphere.openweather.OpenWeatherMap;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "execution_time")
    private Timestamp executionTime;

    private String params;

    @Lob
    private String result;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private OpenWeatherMap openWeatherMap;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Timestamp getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Timestamp executionTime) {
        this.executionTime = executionTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public OpenWeatherMap getOpenWeatherMap() {
        return openWeatherMap;
    }

    public void setOpenWeatherMap(OpenWeatherMap openWeatherMap) {
        this.openWeatherMap = openWeatherMap;
    }
}
