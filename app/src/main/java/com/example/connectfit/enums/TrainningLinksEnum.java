package com.example.connectfit.enums;

public enum TrainningLinksEnum {
    CADEIRA_CHINESA("https://youtu.be/k9ETzvj6TIs"),
    FLEXÃO_DE_BRAÇOS("https://youtu.be/UkN0mvT4FaA"),
    ROSCA_CONCENTRADA("https://youtu.be/xlVDnVGn6CY"),
    ELEVAÇÃO_FRONTAL_COM_HALTERES("https://youtu.be/XixF3mtsjBs"),
    ELEVAÇÃO_LATERAL_COM_HALTERES("https://youtu.be/qDAoUOmdbi4"),
    HIPEREXTENSÃO_DE_LOMBAR_SOLO("https://youtu.be/jcQCfZ3saUo"),
    TRÍCEPS_FRANCÊS_UNILATERAL_COM_HALTERES("https://youtu.be/UHzfoMytaCg"),
    ROSCA_INVERSA_COM_HALTERES("https://youtu.be/pDGDCZiwrdo"),
    ABDOMINAL_REMADOR("https://youtu.be/xKKI1XTBx7M"),
    ABDOMINAL_ESCALADOR("https://youtu.be/E9489EcJbMY");

    private String value;

    TrainningLinksEnum() {}
    TrainningLinksEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
