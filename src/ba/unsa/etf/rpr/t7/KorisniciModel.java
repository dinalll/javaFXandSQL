package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class KorisniciModel {
    private Connection conn;
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();
    private PreparedStatement ucitajIzBaze,obrisiKorisnika,updateBazu;

    public KorisniciModel() {
        String url = "jdbc:sqlite:korisnici.db";
        try {
            conn = DriverManager.getConnection(url);
            ucitajIzBaze = conn.prepareStatement("Select * from korisnik");
            obrisiKorisnika = conn.prepareStatement("Delete from korisnik where username=?");
            updateBazu= conn.prepareStatement("Update korisnik set ime=?,prezime=?,email=?,username=?,password=? where username=?");

        } catch (SQLException throwables) {
            System.out.println("Greska u pri konektovanju na bazu korisnici.db");
        }

    }

    public void napuni() {
        // Ako je lista već bila napunjena, praznimo je
        // Na taj način se metoda napuni() može pozivati više puta u testovima
        korisnici.clear();
        try {
            ResultSet rs=ucitajIzBaze.executeQuery();
            while(rs.next()){
                korisnici.add(new Korisnik(rs.getString(1),rs.getString(2), rs.getString(3),
                        rs.getString(4),rs.getString(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        trenutniKorisnik.set(null);
    }
    public void vratiNaDefault() {
        // Dodali smo metodu vratiNaDefault koja trenutno ne radi ništa, a kada prebacite Model na DAO onda
        // implementirajte ovu metodu
        // Razlog za ovo je da polazni testovi ne bi padali nakon što dodate bazu
        try {
            Statement s= conn.createStatement();
            s.execute("Delete from korisnik;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ucitajDefault();

    }
    public void ucitajDefault(){
        Scanner querry=null;
        try {
            querry=new Scanner(new FileInputStream("korisnici.sql"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String upit="";
        while(querry.hasNext()){
            upit+=querry.nextLine();
            if(upit.contains(";")){
                Statement stmn=null;
                try {
                    stmn=conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmn.execute(upit);
                    upit="";
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        querry.close();
    }
    public void updateBazu(Korisnik k){
        try {
            System.out.println(k.getIme()+k.getPrezime()+k.getUsername());
            updateBazu.setString(1,k.getIme());
            updateBazu.setString(2,k.getPrezime());
            updateBazu.setString(3,k.getEmail());
            updateBazu.setString(4,k.getUsername());
            updateBazu.setString(5,k.getPassword());
            updateBazu.setString(6,k.getUsername());
            updateBazu.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void diskonektuj() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ObservableList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ObservableList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik.get();
    }

    public SimpleObjectProperty<Korisnik> trenutniKorisnikProperty() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        if(this.getTrenutniKorisnik()!=null)updateBazu(this.trenutniKorisnik.get());
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        if(this.getTrenutniKorisnik()!=null)updateBazu(trenutniKorisnik.get());
        this.trenutniKorisnik.set(korisnici.get(i));
    }
    public void obrisi(Korisnik k){
        try {
            obrisiKorisnika.setString(1,k.getUsername());
            obrisiKorisnika.executeUpdate();
            setTrenutniKorisnik(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
