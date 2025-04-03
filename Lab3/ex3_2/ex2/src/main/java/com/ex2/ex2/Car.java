package com.ex2.ex2;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String maker;
    private String motorType;
    private String segment;
    private String origin;

    public Car() {
    }

    public Car(Long id, String model, String maker, String motorType, String segment, String origin) {
        this.id = id;
        this.model = model;
        this.maker = maker;
        this.motorType = motorType;
        this.segment = segment;
        this.origin = origin;
    }

    public Car(String model, String maker, String motorType, String segment, String origin) {
        this.model = model;
        this.maker = maker;
        this.motorType = motorType;
        this.segment = segment;
        this.origin = origin;
    }

    public Long getId() { return id; }
    public String getModel() { return model; }
    public String getMaker() { return maker; }

    @JsonProperty("type")
    public String getMotorType() { return motorType; }

    public String getSegment() { return segment; }
    public String getOrigin() { return origin; }

    public void setId(Long id) { this.id = id; }
    public void setModel(String model) { this.model = model; }
    public void setMaker(String maker) { this.maker = maker; }
    public void setMotorType(String motorType) { this.motorType = motorType; }
    public void setSegment(String segment) { this.segment = segment; }
    public void setOrigin(String origin) { this.origin = origin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) &&
                Objects.equals(model, car.model) &&
                Objects.equals(maker, car.maker) &&
                Objects.equals(motorType, car.motorType) &&
                Objects.equals(segment, car.segment) &&
                Objects.equals(origin, car.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, maker, motorType, segment, origin);
    }
}
