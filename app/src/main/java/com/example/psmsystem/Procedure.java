package com.example.psmsystem;

public class Procedure {



    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getProcedure_name() {
        return procedure_name;
    }

    public void setProcedure_name(String procedure_name) {
        this.procedure_name = procedure_name;
    }

    public Procedure(String procedure, String procedure_name) {
        this.procedure = procedure;
        this.procedure_name = procedure_name;
    }

    private String procedure;
    private String procedure_name;

    public Procedure() {

        this.procedure="";
        this.procedure_name="";



    }
}
