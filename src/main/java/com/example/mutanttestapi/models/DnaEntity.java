package com.example.mutanttestapi.models;


import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Entity(name = "dna")
public class DnaEntity {

    @Id
    private String id;

    private DnaType type;

    public DnaEntity(String sequence, DnaType type) {
        this.id = sequence;
        this.type = type;
    }

    public DnaEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DnaType getType() {
        return type;
    }

    public void setType(DnaType type) {
        this.type = type;
    }
}
