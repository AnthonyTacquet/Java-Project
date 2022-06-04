package logica;

public enum Beroepsprofiel {
    NULL,
    TELECOMMUNICATIONS_ENGINEER,
    INTERNET_OF_THINGS_DEVELOPER,
    NETWORK_SECURITY_ENGINEER,
    SOFTWARE_AI_DEVELOPER,
    WEB_MOBILE_DEVELOPER;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }

    /*public Beroepsprofiel valueOf(String beroepsprofiel){
        switch (beroepsprofiel.toUpperCase()){
            case "NULL" : return Beroepsprofiel.NULL;
            case "TELECOMMUNICATIONS_ENGINEER" : return Beroepsprofiel.TELECOMMUNICATIONS_ENGINEER;
            case "INTERNET_OF_THINGS_DEVELOPER" : return Beroepsprofiel.INTERNET_OF_THINGS_DEVELOPER;
            case "NETWORK_SECURITY_ENGINEER" : return Beroepsprofiel.NETWORK_SECURITY_ENGINEER;
            case "SOFTWARE_AI_DEVELOPER" : return Beroepsprofiel.SOFTWARE_AI_DEVELOPER;
            case "WEB_MOBILE_DEVELOPER" : return Beroepsprofiel.WEB_MOBILE_DEVELOPER;
        }
    }*/
}
