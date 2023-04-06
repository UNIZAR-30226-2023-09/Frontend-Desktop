import java.util.HashMap;
import java.util.Map;

public class DatosPartida {

    public static boolean esMiTurnoDados;

    public static int vecesLanzadoDados;

    public static int casilla;

    // public static int casillaPrevia;

    public static boolean estoyCarcel;

    // public static int[] posicionesMonopoly;

    public static Map<String, String> mapaPropiedades = new HashMap<>();

    static {
        mapaPropiedades.put("Pos1", "1156,862");
        mapaPropiedades.put("Pos2", "1023,862");
        mapaPropiedades.put("Pos3", "924,862");
        mapaPropiedades.put("Pos4", "816,862");
        mapaPropiedades.put("Pos5", "714,862");
        mapaPropiedades.put("Pos6", "609,862");
        mapaPropiedades.put("Pos7", "505,862");
        mapaPropiedades.put("Pos8", "400,862");
        mapaPropiedades.put("Pos9", "298,862");
        mapaPropiedades.put("Pos10", "195,862");
        mapaPropiedades.put("Pos11", "14,862");
        mapaPropiedades.put("Pos12", "56,765");
        mapaPropiedades.put("Pos13", "56,688");
        mapaPropiedades.put("Pos14", "56,606");
        mapaPropiedades.put("Pos15", "56,530");
        mapaPropiedades.put("Pos16", "56,451");
        mapaPropiedades.put("Pos17", "56,374");
        mapaPropiedades.put("Pos18", "56,294");
        mapaPropiedades.put("Pos19", "56,216");
        mapaPropiedades.put("Pos20", "56,140");
        mapaPropiedades.put("Pos21", "56,35");
        mapaPropiedades.put("Pos22", "197,35");
        mapaPropiedades.put("Pos23", "298,35");
        mapaPropiedades.put("Pos24", "402,35");
        mapaPropiedades.put("Pos25", "505,35");
        mapaPropiedades.put("Pos26", "609,35");
        mapaPropiedades.put("Pos27", "716,35");
        mapaPropiedades.put("Pos28", "816,35");
        mapaPropiedades.put("Pos29", "917,35");
        mapaPropiedades.put("Pos30", "1018,35");
        mapaPropiedades.put("Pos31", "1161,35");
        mapaPropiedades.put("Pos32", "1161,140");
        mapaPropiedades.put("Pos33", "1161,216");
        mapaPropiedades.put("Pos34", "1161,295");
        mapaPropiedades.put("Pos35", "1161,371");
        mapaPropiedades.put("Pos36", "1161,451");
        mapaPropiedades.put("Pos37", "1161,530");
        mapaPropiedades.put("Pos38", "1161,608");
        mapaPropiedades.put("Pos39", "1161,684");
        mapaPropiedades.put("Pos40", "1161,763");

    }

    /*
     * EN CASO DE NECESITAR LAS CASILLA PARA PRUEBAS ANTES DE QUE FUNCIONE EL JUGAR
     * PARTIDA
     * public static Map<String, Boolean> propiedadesAdquiridas = new HashMap<>();
     * 
     * static {
     * propiedadesAdquiridas.put("Pos1", false);
     * propiedadesAdquiridas.put("Pos2", false);
     * propiedadesAdquiridas.put("Pos3", false);
     * propiedadesAdquiridas.put("Pos4", false);
     * propiedadesAdquiridas.put("Pos5", false);
     * propiedadesAdquiridas.put("Pos6", false);
     * propiedadesAdquiridas.put("Pos7", false);
     * propiedadesAdquiridas.put("Pos8", false);
     * propiedadesAdquiridas.put("Pos9", false);
     * propiedadesAdquiridas.put("Pos10", false);
     * propiedadesAdquiridas.put("Pos11", false);
     * propiedadesAdquiridas.put("Pos12", false);
     * propiedadesAdquiridas.put("Pos13", false);
     * propiedadesAdquiridas.put("Pos14", false);
     * propiedadesAdquiridas.put("Pos15", false);
     * propiedadesAdquiridas.put("Pos16", false);
     * propiedadesAdquiridas.put("Pos17", false);
     * propiedadesAdquiridas.put("Pos18", false);
     * propiedadesAdquiridas.put("Pos19", false);
     * propiedadesAdquiridas.put("Pos20", false);
     * propiedadesAdquiridas.put("Pos21", false);
     * propiedadesAdquiridas.put("Pos22", false);
     * propiedadesAdquiridas.put("Pos23", false);
     * propiedadesAdquiridas.put("Pos24", false);
     * propiedadesAdquiridas.put("Pos25", false);
     * propiedadesAdquiridas.put("Pos26", false);
     * propiedadesAdquiridas.put("Pos27", false);
     * propiedadesAdquiridas.put("Pos28", false);
     * propiedadesAdquiridas.put("Pos29", false);
     * propiedadesAdquiridas.put("Pos30", false);
     * propiedadesAdquiridas.put("Pos31", false);
     * propiedadesAdquiridas.put("Pos32", false);
     * propiedadesAdquiridas.put("Pos33", false);
     * propiedadesAdquiridas.put("Pos34", false);
     * propiedadesAdquiridas.put("Pos35", false);
     * propiedadesAdquiridas.put("Pos36", false);
     * propiedadesAdquiridas.put("Pos37", false);
     * propiedadesAdquiridas.put("Pos38", false);
     * propiedadesAdquiridas.put("Pos39", false);
     * propiedadesAdquiridas.put("Pos40", false);
     * 
     * 
     * }
     */

}
