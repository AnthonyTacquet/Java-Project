package logica;

public enum Pijltjes {
    LINKS, RECHTS, BOVEN, ONDER;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
