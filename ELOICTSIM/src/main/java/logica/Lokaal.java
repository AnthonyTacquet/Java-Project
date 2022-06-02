package logica;

public class Lokaal extends Coordinaten{
    private String naam;
    private String code;

    public Lokaal(){
        this.naam = "";
        this.code = "";
    }

    public Lokaal(int x, int y, int b, int h, String naam){
        super(x,y,b,h);
        this.naam = naam;
        this.code = "";
    }

    public Lokaal(String naam, String code, int x, int y, int b, int h ){
        super(x,y,b,h);
        this.naam = naam;
        this.code = code;
    }

    public String getNaam(){
        return this.naam;
    }

    public String getCode(){
        return this.code;
    }

    public void setNaam(String naam){
        this.naam = naam;
    }

    public void setCode(String code){
        this.code = code;
    }
}
