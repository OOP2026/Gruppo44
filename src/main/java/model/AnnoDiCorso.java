package model;

public class AnnoDiCorso {

     enum Anno {
        PRIMO,
        SECONDO,
        TERZO
    }

    Anno AnnoAccademico;

     AnnoDiCorso(Anno AnnoAccademico){
         this.AnnoAccademico = AnnoAccademico;
     }



}
