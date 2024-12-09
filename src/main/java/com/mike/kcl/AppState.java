package com.mike.kcl;

public class AppState {
    private Liquid liquid;
    private SolidMaterial solidMaterial;
    private LiquidMaterial liquidMaterial;
    private Vishelachivanie vishelachivanie;
    private HydrocycloneSolid hydrocycloneSolid;
    private HydrocycloneLiquid hydrocycloneLiquid;
    private CentrifugeSolid centrifugeSolid;
    private CentrifugeLiquid centrifugeLiquid;
    private Sushka sushka;
    // Constructors, Getters, and Setters

    public AppState() {
    }

    public AppState(Liquid liquid, SolidMaterial solidMaterial, LiquidMaterial liquidMaterial,
                    Vishelachivanie vishelachivanie, HydrocycloneSolid hydrocycloneSolid, HydrocycloneLiquid hydrocycloneLiquid, CentrifugeSolid centrifugeSolid, CentrifugeLiquid centrifugeLiquid, Sushka sushka) {
        this.liquid = liquid;
        this.solidMaterial = solidMaterial;
        this.liquidMaterial = liquidMaterial;
        this.vishelachivanie = vishelachivanie;
        this.hydrocycloneSolid = hydrocycloneSolid;
        this.hydrocycloneLiquid = hydrocycloneLiquid;
        this.centrifugeSolid = centrifugeSolid;
        this.centrifugeLiquid = centrifugeLiquid;
        this.sushka = sushka;
    }

    public Liquid getLiquid() {
        return liquid;
    }

    public void setLiquid(Liquid liquid) {
        this.liquid = liquid;
    }

    public SolidMaterial getSolidMaterial() {
        return solidMaterial;
    }

    public void setSolidMaterial(SolidMaterial solidMaterial) {
        this.solidMaterial = solidMaterial;
    }

    public LiquidMaterial getLiquidMaterial() {
        return liquidMaterial;
    }

    public void setLiquidMaterial(LiquidMaterial liquidMaterial) {
        this.liquidMaterial = liquidMaterial;
    }

    public Vishelachivanie getVishelachivanie() {
        return vishelachivanie;
    }

    public void setVishelachivanie(Vishelachivanie vishelachivanie) {
        this.vishelachivanie = vishelachivanie;
    }

    public HydrocycloneSolid getHydrocycloneSolid() {
        return hydrocycloneSolid;
    }

    public void setHydrocycloneSolid(HydrocycloneSolid hydrocycloneSolid) {
        this.hydrocycloneSolid = hydrocycloneSolid;
    }

    public HydrocycloneLiquid getHydrocycloneLiquid() {
        return hydrocycloneLiquid;
    }

    public void setHydrocycloneLiquid(HydrocycloneLiquid hydrocycloneLiquid) {
        this.hydrocycloneLiquid = hydrocycloneLiquid;
    }

    public CentrifugeSolid getCentrifugeSolid() {
        return centrifugeSolid;
    }

    public void setCentrifugeSolid(CentrifugeSolid centrifugeSolid) {
        this.centrifugeSolid = centrifugeSolid;
    }

    public CentrifugeLiquid getCentrifugeLiquid() {
        return centrifugeLiquid;
    }

    public void setCentrifugeLiquid(CentrifugeLiquid centrifugeLiquid) {
        this.centrifugeLiquid = centrifugeLiquid;
    }

    public Sushka getSushka() {
        return sushka;
    }

    public void setSushka(Sushka sushka) {
        this.sushka = sushka;
    }
}