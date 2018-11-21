package com.staffgenics.training.banking.client;

import com.staffgenics.training.banking.account.AccountEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Encja z danymi klienta.
 */
@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
@Where(clause = "inactive='false'")
public class ClientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  private String surname;

  private String name;

  private String pesel;

  private String secondName;

  private boolean vip;

  private boolean resident;

  @Column(name = "inactive")
  private boolean isInactive;

  @OneToMany
  @JoinColumn(name = "clientId", referencedColumnName = "id")
  private List<AccountEntity> accounts = new ArrayList<>();

  public ClientEntity(Long id, String surname, String name, String pesel, String secondName, boolean vip, boolean resident) {
    this.id = id;
    this.surname = surname;
    this.name = name;
    this.pesel = pesel;
    this.secondName = secondName;
    this.vip = vip;
    this.resident = resident;
  }

  static ClientEntity createInstance(ClientDto clientDto) {
    ClientEntity clientEntity = new ClientEntity();
    clientEntity.setSurname(clientDto.getSurname());
    clientEntity.setName(clientDto.getName());
    clientEntity.setPesel(clientDto.getPesel());
    clientEntity.setSecondName(clientDto.getSecondName());
    clientEntity.setVip(clientDto.isVip());
    clientEntity.setResident(clientDto.isResident());
    return clientEntity;
  }

  void update(ClientDto clientDto) {
    setSurname(clientDto.getSurname());
    setName(clientDto.getName());
    setPesel(clientDto.getPesel());
    setSecondName(clientDto.getSecondName());
    setVip(clientDto.isVip());
  }

  void deactivate(){
    this.setInactive(true);
  }
}
