package sn.dev.parrainageapp.entities;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    private int id;
    private String nom, prenom, login, password;
    private int actived;
    private Role profil;

}
