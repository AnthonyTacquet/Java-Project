package logica;

public enum Beroepsprofiel {
    NULL,
    TELECOMMUNICATIONS_ENGINEER,
    INTERNET_OF_THINGS_DEVELOPER,
    NETWORK_SECURITY_ENGINEER,
    SOFTWARE_AI_DEVELOPER,
    WEB_MOBILE_DEVELOPER;

    public String toString(){
        return this.name().toLowerCase();
    }
}
